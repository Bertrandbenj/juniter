package juniter.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import juniter.core.model.dbo.index.BINDEX;
import juniter.core.model.dto.node.Block;
import juniter.service.core.BlockService;
import juniter.service.core.Index;
import juniter.service.core.PeerService;
import juniter.service.gva.GVASubscriptionHandler;
import juniter.service.ws2p.WSBlock;
import juniter.service.ws2p.WSPeer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


@EnableWebSocket
@Configuration
public class GVAConf implements WebSocketConfigurer {
    private static final Logger LOG = LogManager.getLogger(GVAConf.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BlockService blockService;

    @Autowired
    private PeerService peerService;

    @Autowired
    private Index index;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        LOG.info("Starting GraphQL websocket handler at /graphql  ...");

        registry.addHandler(webSocketGraphQLHandler(), "/graphql")
                .addHandler(wsBlockHandler(), "/ws/block", "/block")  // # problem  with /websocket suffix
                .addHandler(wsPeerHandler(), "/ws/peer", "/peer")   // # problem  with /websocket suffix
                .setAllowedOrigins("*")
//                .setHandshakeHandler((request, response, wsHandler, attributes) -> {
//                    LOG.info("doHandshake " + request.getURI().getPath() + " will use " + wsHandler.getClass().getCanonicalName());
//                    simpleWsDispatch(request, response);
//
//                    return true;
//                })
                .addInterceptors(new HandshakeInterceptor() {
                    // whether to proceed with the handshake ({@code true}) or abort ({@code false})
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                        LOG.info("before handshake " + request.getURI());
                        if (request.getURI().getPath().endsWith("/graphql")) {
                            //new DefaultHandshakeHandler(new TomcatRequestUpgradeStrategy()).doHandshake()
                            return true;
                        } else {

                            return simpleWsDispatch(request, response, wsHandler);
                        }
                    }

                    @Override
                    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
                        LOG.info("after handshake ");

                        simpleWsDispatch(request, response, wsHandler);
                    }
                })

        //.withSockJS()
        ;

    }

    private boolean simpleWsDispatch(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler) {
        LOG.info("Simple dispatcher " + request.getURI().getPath() + " will use " + wsHandler.getClass().getCanonicalName());
        AtomicBoolean res = new AtomicBoolean(false);

        if (request.getURI().getPath().endsWith("/ws/block") || request.getURI().getPath().endsWith("/block")) {

            blockService.current().ifPresent(bl -> {
                try {
                    var block = modelMapper.map(bl, Block.class);

                    response.getBody().write(objectMapper.writeValueAsBytes(block));
                    LOG.info("sending node " + block.getNumber() + " " + response.getBody());
                    res.set(true);
                } catch (Exception e) {
                    LOG.error("simpleWsDispatch ERROR", e);
                }
            });

        }

        if (request.getURI().getPath().endsWith("/ws/peer") || request.getURI().getPath().endsWith("/peer")) {

            index.head().map(BINDEX::getNumber).ifPresent(bl -> {
                try {
                    var peer = objectMapper.writeValueAsString(peerService.endPointPeer(bl));
                    response.getBody().write(new TextMessage(peer).asBytes());
                    LOG.info("sending peer " + peer.toString() + " " + response.getBody());
                    res.set(true);
                } catch (Exception e) {
                    LOG.error("simpleWsDispatch ERROR: ", e);
                }
            });
        }
        return res.get();
    }

    @Bean
    public WebSocketHandler webSocketGraphQLHandler() {
        return new PerConnectionWebSocketHandler(GVASubscriptionHandler.class);
    }


    @Bean
    public WebSocketHandler wsPeerHandler() {
        return new PerConnectionWebSocketHandler(WSPeer.class);
    }

    @Bean
    public WebSocketHandler wsBlockHandler() {
        return new WSBlock();
    }


}
