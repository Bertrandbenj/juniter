package juniter.service.ws2p;

import com.fasterxml.jackson.databind.ObjectMapper;
import juniter.core.model.dbo.net.EndPointType;
import juniter.core.model.wso.Wrapper;
import juniter.service.core.BlockService;
import juniter.service.core.PeerService;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.security.KeyStore;
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
// Create a trust manager that does not validate certificate chains like the default

    TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    //No need to implement.
                }

                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    //No need to implement.
                }
            }
    };


    @Getter
    private AtomicBoolean running = new AtomicBoolean(true);


    @Value("${juniter.network.webSocketPoolSize:5}")
    private Integer WEB_SOCKET_POOL_SIZE;

    @Getter
    private BlockingQueue<WS2PClient> clients = new LinkedBlockingDeque<>();


    @Autowired
    PeerService peerService;

    @Autowired
    public ObjectMapper jsonMapper;

    @Autowired
    public ApplicationEventPublisher coreEventBus;

    @Autowired
    public BlockService blockService;

    SSLSocketFactory factory;

    @Value("${server.ssl.keyStoreType}")
    private String STORETYPE;

    @Value("${server.ssl.key-store}")
    private String KEYSTORE;

    @Value("${server.ssl.key-store-password}")
    private String STOREPASSWORD;


    @PostConstruct
    public void start() {

        try {

            KeyStore ks = KeyStore.getInstance(STORETYPE);
            File kf = new File(KEYSTORE);
            ks.load(new FileInputStream(kf), STOREPASSWORD.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, STOREPASSWORD.toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(ks);

            SSLContext sslContext = null;
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), trustAllCerts, null);
            // sslContext.init( null, null, null ); // will use java's default key and trust store which is sufficient unless you deal with self-signed certificates

            factory = sslContext.getSocketFactory();// (SSLSocketFactory) SSLSocketFactory.getDefault();

        } catch (Exception e) {
            LOG.error("problem setup ssl", e);
        }

        clients = new LinkedBlockingDeque<>(WEB_SOCKET_POOL_SIZE);

    }


    @Transactional
    @Scheduled(initialDelay = 3 * 60 * 1000, fixedDelay = 10 * 1000)
    public void refreshCurrents() {
        LOG.info("Refreshing Current ");
        if (running.get())
            clients.parallelStream()
                    //.peek(c -> LOG.info("Refreshing Current " + running + " on " + c.getURI() + status()))
                    .forEach(c -> c.send(Wrapper.buildPeerDoc(peerService.endPointPeer(blockService.currentBlockNumber()).toDUP(true))));
    }

    @Transactional
    @Scheduled(initialDelay = 60 * 1000, fixedDelay = 60 * 1000)//, initialDelay = 10 * 60 * 1000)
    public void reconnectWebSockets() {

        if(!running.get())
            return;
       // while (clients.remainingCapacity() > 0 && running.get()) {
            peerService.nextHosts(EndPointType.WS2P, clients.remainingCapacity())
                    .parallelStream()
                    .forEach(ep -> {
                        var client = new WS2PClient(URI.create(ep.getHost()), this);
                        LOG.debug("Connecting to WS endpoint " + client.getURI());

                        try {
                            client.setConnectionLostTimeout(0);
                            client.connectBlocking();
                        } catch (Exception e) {
                            if (running.get())
                                LOG.error("reconnectWebSockets ", e);
                            else
                                LOG.warn("reconnectWebSockets "); // may be ignored
                        }
                    });
        //}


    }


    @Scheduled(fixedRate = 1000 * 60, initialDelay = 60 * 1000)
    public void renormalize() {
        if(running.get())
            peerService.renormalize(EndPointType.WS2P);
    }


    public String status() {
        return "WS pool : " + clients.size() + "/" + WEB_SOCKET_POOL_SIZE + " - " + clients.stream().map(WebSocketClient::getURI).collect(Collectors.toList());
    }


}