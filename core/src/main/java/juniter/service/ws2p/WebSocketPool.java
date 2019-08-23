package juniter.service.ws2p;

import com.fasterxml.jackson.databind.ObjectMapper;
import juniter.core.model.dbo.net.EndPointType;
import juniter.service.BlockService;
import juniter.service.Index;
import juniter.service.bma.PeerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * read https://git.duniter.org/nodes/common/doc/blob/master/rfc/0004_ws2p_v1.md
 *
 * @author ben
 */
@ConditionalOnExpression("${juniter.useWS2P:false}")
@Service
@Order(10)
public class WebSocketPool {

    private static final Logger LOG = LogManager.getLogger(WebSocketPool.class);


    private AtomicBoolean running = new AtomicBoolean(true);


    @Value("${juniter.network.webSocketPoolSize:5}")
    private Integer WEB_SOCKET_POOL_SIZE;

    BlockingQueue<WS2PClient> clients = new LinkedBlockingDeque<>();


    @Autowired
    private PeerService peerService;

    @Autowired
    public ObjectMapper jsonMapper;

    @Autowired
    public ApplicationEventPublisher coreEventBus;

    @Autowired
    public BlockService blockService;

    @Autowired
    public Index index;


    @PostConstruct
    public void start() {
        clients = new LinkedBlockingDeque<>(WEB_SOCKET_POOL_SIZE);
    }

    @Async
    public void restart() {
        running.lazySet(true);
        reconnectWebSockets();
    }


    @Transactional
    @Scheduled(initialDelay = 3 * 60 * 1000, fixedDelay = 20 * 1000)
    public void refreshCurrents() {

        if (running.get())
            clients.stream()
                    .peek(c -> LOG.info("Refreshing Current "
                            + running + " on wss://" + c.getURI()
                            + " " + clients.remainingCapacity() + " " + status()))
                    .forEach(c -> c.send(new Request().getCurrent()));
    }

    @Async
    public void stop() {
        LOG.info("Stopping websockets");
        running.set(false);
        clients.forEach(c -> c.close(1000, "Cause I decided to "));
    }


    @Transactional
    @Scheduled(initialDelay = 60 * 1000, fixedDelay = 10 * 1000)//, initialDelay = 10 * 60 * 1000)
    public void reconnectWebSockets() {

        while (clients.remainingCapacity() > 0 && running.get()) {
            peerService.nextHost(EndPointType.WS2P).ifPresent(ep -> {
                var client = new WS2PClient(URI.create(ep.getHost()), this);
                LOG.debug("Connecting to WS endpoint " + client.getURI());

                try {
                    client.connectBlocking(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    if (running.get())
                        LOG.error("reconnectWebSockets ", e);
                    else
                        LOG.warn("reconnectWebSockets "); // may be ignored

                }
            });
        }

    }


    public String status() {
        return " Pool status : " + clients.size() + "/" + WEB_SOCKET_POOL_SIZE + " - " + clients.remainingCapacity()
                + " - " + clients.stream().map(WebSocketClient::getURI).collect(Collectors.toList());
    }


}