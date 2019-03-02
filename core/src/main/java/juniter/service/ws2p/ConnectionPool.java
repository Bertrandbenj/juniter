package juniter.service.ws2p;

import juniter.repository.jpa.EndPointsRepository;
import juniter.service.bma.loader.PeerLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import java.net.URISyntaxException;
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
public class ConnectionPool {

    private static final Logger LOG = LogManager.getLogger();


    @Value("${juniter.network.webSocketPoolSize:5}")
    private Integer webSocketPoolSize;

    private BlockingQueue<WS2PClient> clients = new LinkedBlockingDeque<>(10);

    private BlockingQueue<WS2PClient> server = new LinkedBlockingDeque<>(10);


    @Value("${server.port:8443}")
    private Integer port;

    @Value("${server.name:localhost}")
    private String host;


    @Autowired
    private EndPointsRepository endPointRepo;

    @Autowired
    private PeerLoader peerLoader;


    @PostConstruct
    public void startServer() {
//
//        WebSocketServer server = new WSServer(new InetSocketAddress(host, 8181));
//
//        new Thread(server).start();

    }


    @Async
    @Transactional
    @Scheduled(fixedDelay = 60 * 60 * 1000)//, initialDelay = 10 * 60 * 1000)
    public void startWebSockets() {

        LOG.info("Starting WebSocket ConnectionPool");

        endPointRepo.endpointsWS2P().forEach(ep -> {
            try {
                final var client = new WS2PClient(new URI(ep.url()));
                clients.put(client);
                LOG.info("Pushed WS client : "+ep.url() + "  " + client);
            } catch (URISyntaxException e) {
                LOG.error("start WebSocket client URISyntaxException " + ep.url());
            } catch (InterruptedException e) {
                LOG.error("start WebSocket client InterruptedException " + ep.url());
            }

        });


    }


}