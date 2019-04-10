package juniter.conf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodReturnValueHandler;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurationSupport;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import java.util.List;


//@Configuration
//@EnableWebSocketMessageBroker
public class WSConf extends WebSocketMessageBrokerConfigurationSupport implements WebSocketMessageBrokerConfigurer {


    private static final Logger LOG = LogManager.getLogger(WSConf.class);

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //config.enableSimpleBroker("/topic");
        //config.setApplicationDestinationPrefixes("/wstest"); // use with @MessageMapping
        //config.setPathMatcher(new AntPathMatcher("/"));

    }


    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(100);


    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptorAdapter() {

            @Override
            public boolean preReceive(MessageChannel channel) {
                LOG.info("preReceive channel : " + channel + "  " );
                return super.preReceive(channel);
            }

            @Override
            public Message<?> postReceive(Message<?> message, MessageChannel channel) {

                LOG.info("postReceive message : " + message + "  " );

                return super.postReceive(message, channel);
            }
        });
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        super.configureClientOutboundChannel(registration);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
    }

    @Override
    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers) {
    super.addReturnValueHandlers(returnValueHandlers);
    }

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        return super.configureMessageConverters(messageConverters);
    }


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        LOG.info("configure stomp  /gs-guide-websocket   /ws");
        registry.addEndpoint("/gs-guide-websocket", "/ws").setAllowedOrigins("*").withSockJS();
    }








}
