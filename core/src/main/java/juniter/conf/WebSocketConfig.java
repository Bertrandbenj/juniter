package juniter.conf;

//@Configuration
//@EnableWebSocketMessageBroker
//public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
//    private static final Logger LOG = LogManager.getLogger();
//
//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry config) {
//    	config.setApplicationDestinationPrefixes("/juniter");
//    	config.enableSimpleBroker("/ws2p");
//        LOG.info("configured /juniter /ws2p");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/endpoint").withSockJS();
//        LOG.info("registerStompEndpoints /endpoint");
//
//    }
//
//}