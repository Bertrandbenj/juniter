package juniter.conf;

import com.fasterxml.jackson.databind.ObjectMapper;
import juniter.core.model.dto.Block;
import juniter.core.model.index.BINDEX;
import juniter.repository.jpa.BlockRepository;
import juniter.repository.jpa.index.BINDEXRepository;
import juniter.service.bma.NetworkService;
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
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.PerConnectionWebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@EnableWebSocket
@Configuration
public class GVAConf implements WebSocketConfigurer {
    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BlockRepository blockRepo;

    @Autowired
    private NetworkService netService;


    @Autowired
    private BINDEXRepository bRepo;

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

                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                        if (request.getURI().getPath().endsWith("/graphql")) {
                            return false;
                        } else {
                            simpleWsDispatch(request, response, wsHandler);
                            return true;
                        }
                    }

                    @Override
                    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

                        simpleWsDispatch(request, response, wsHandler);
                    }
                })

        // .withSockJS()
        ;

    }

    public void simpleWsDispatch(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler) {
        LOG.info("Simple dispatcher " + request.getURI().getPath() + " will use " + wsHandler.getClass().getCanonicalName());

        if (request.getURI().getPath().endsWith("/ws/block") || request.getURI().getPath().endsWith("/block")) {

            blockRepo.current().ifPresent(bl -> {
                try {
                    var block = modelMapper.map(bl, Block.class);

                    response.getBody().write(objectMapper.writeValueAsBytes(block));
                    LOG.info("sending block " + block.getNumber() + " " + response.getBody());
                } catch (Exception e) {
                    LOG.error(e);
                }
            });

        }

        if (request.getURI().getPath().endsWith("/ws/peer") || request.getURI().getPath().endsWith("/peer")) {

            bRepo.head().map(BINDEX::getNumber).ifPresent(bl -> {
                try {
                    var peer = objectMapper.writeValueAsString(netService.endPointPeer(bl));
                    response.getBody().write(new TextMessage(peer).asBytes());
                    LOG.info("sending block " + peer.toString() + " " + response.getBody());

                } catch (Exception e) {
                    LOG.error(e);
                }
            });

        }
    }

    @Bean
    public WebSocketHandler webSocketGraphQLHandler() {
        return new PerConnectionWebSocketHandler(GVASubscriptionHandler.class);
    }

    @Bean
    public HandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping mapping = new RequestMappingHandlerMapping();
        mapping.setUseSuffixPatternMatch(false);
        return mapping;
    }


    @Bean
    public WebSocketHandler wsPeerHandler() {
        return new PerConnectionWebSocketHandler(WSPeer.class);
    }

    @Bean
    public WebSocketHandler wsBlockHandler() {
        return new PerConnectionWebSocketHandler(WSBlock.class);
    }


}
