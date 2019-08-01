package juniter.service.ws2p;

import com.fasterxml.jackson.databind.ObjectMapper;
import juniter.core.model.dbo.net.EndPoint;
import juniter.repository.jpa.block.BlockRepository;
import juniter.repository.jpa.net.EndPointsRepository;
import juniter.service.bma.loader.PeerLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

/**
 * read https://git.duniter.org/nodes/common/doc/blob/master/rfc/0004_ws2p_v1.md
 *
 * @author ben
 */
@ConditionalOnExpression("${juniter.useWS2P:false}")
@Component
@Order(10)
public class WebSocketPool {

    private static final Logger LOG = LogManager.getLogger(WebSocketPool.class);


    @Value("${juniter.network.webSocketPoolSize:5}")
    private Integer WEB_SOCKET_POOL_SIZE;

    BlockingQueue<WS2PClient> clients = new LinkedBlockingDeque<>();


    @Value("${server.port:8443}")
    private Integer port;

    @Value("${server.name:localhost}")
    private String host;


    @Autowired
    private EndPointsRepository endPointRepo;

    @Autowired
    private PeerLoader peerLoader;

    @Autowired
    public ObjectMapper jsonMapper;

    @Autowired
    public BlockRepository blockRepo;


    ExecutorService pool = Executors.newCachedThreadPool();

    @PostConstruct
    public void start() {
        clients = new LinkedBlockingDeque<>(WEB_SOCKET_POOL_SIZE);

//        for (int i = 1; i <= 4; i++) {
//            final int id = i;
//            Runnable run = () -> {
//                try {
//                    send(id);
//
//                } catch (URISyntaxException | InterruptedException e) {
//                    LOG.error(e, e);
//                }
//
//            };
//
//            pool.execute(run);
//        }
//        pool.shutdown();
    }


    @Async
    @Transactional
    @Scheduled(initialDelay = 2 * 60 * 1000, fixedDelay = 2 * 60 * 1000)//, initialDelay = 10 * 60 * 1000)
    public void reconnectWebSockets() {

        LOG.info("Starting WebSocketPool on wss://" + host + ":" + port
                + " " + clients.remainingCapacity() + " " + status());

        endPointRepo.endpointsWS2P()
                .map(EndPoint::url)
                .distinct()
                .map(url -> new WS2PClient(URI.create(url), this))
                .peek(ep -> LOG.info("Connecting to WS endpoint " + ep.getURI()))
                .forEach(this::connectTo)
        ;

    }

    public void connectTo(WS2PClient client) {

        if (!(clients.remainingCapacity() > 0))
            return;

        try {
            client.connect();
        } catch (Exception e) {
            LOG.error("reconnectWebSockets ", e);
        }

    }

    public String status() {
        return " Pool status : " + clients.size() + "/" + WEB_SOCKET_POOL_SIZE + " - " + clients.remainingCapacity()
                + " - " + clients.stream().map(WebSocketClient::getURI).collect(Collectors.toList());
    }


    private Runnable consumer = () -> {
        while (true) {

            try {
                clients.take().connect();
                Thread.sleep(1000);

            } catch (Exception e) {
                LOG.error(e, e);
                break;
            }

        }
    };


}