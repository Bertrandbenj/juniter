package juniter.service.ws2p;

import juniter.repository.jpa.EndPointsRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

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

    private List<WS2PClient> list = new ArrayList<>();

    @Autowired
    private EndPointsRepository endPointRepo;

    @Transactional
    @Scheduled(fixedDelay = 10 * 60 * 1000)
    public void startWebSockets() {

        LOG.info("Starting WebSocket ConnectionPool");

        for (int i = 0; i < webSocketPoolSize; i++) {

            endPointRepo.endpointsWS2P().forEach(ep -> {
                var url = "wss://" + ep.getEndpoint() + ":" + ep.getPort() + ep.getOther();
                try {
                    final var client = new WS2PClient(new URI(url));
                    list.add(client);
                } catch (URISyntaxException e) {
                    LOG.error("startWebSockets URISyntaxException " + url);
                }

            });

        }


        //		final var client2 = new WS2PClient(new URI("wss://80.118.154.251:20900/"));

        //		client1.connect();


    }


}