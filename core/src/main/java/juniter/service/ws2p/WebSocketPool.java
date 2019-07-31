package juniter.service.ws2p;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.concurrent.LinkedBlockingDeque;

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


    @PostConstruct
    public void start() {
        clients = new LinkedBlockingDeque<>(WEB_SOCKET_POOL_SIZE);

//
//        // Init consumers
//        for (int i = 0; i < WEB_SOCKET_POOL_SIZE; i++) {
//            new Thread(consumer, "WebSock-" + i).start();
//        }
    }


    @Async
    @Transactional
    @Scheduled(fixedDelay = 1 * 60 * 1000)//, initialDelay = 10 * 60 * 1000)
    public void startWebSockets() {

        LOG.info("Starting WebSocketPool on wss://" + host + ":" + port +" "+ status());

        endPointRepo.endpointsWS2P()

                .peek(c -> LOG.info("pushing client endpoint " + c.toString()))
                .map(ep -> new WS2PClient(URI.create(ep.url()), this))
//                .peek(client-> clients.offer(client))
                .peek(WebSocketClient::connect)
                .forEach(client -> LOG.info("Connecting to : " + client.getURI() + status()))
        ;


    }

    public String status() {
        return " Pool status : " + clients.size() + "/" + WEB_SOCKET_POOL_SIZE;
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