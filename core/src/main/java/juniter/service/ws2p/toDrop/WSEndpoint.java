package juniter.service.ws2p.toDrop;

//
//@ServerEndpoint(value = "/wst/{username}", decoders = WSEndpoint.MessageDecoder.class, encoders = WSEndpoint.MessageEncoder.class)
//public class WSEndpoint {
//
//    private static final Logger LOG = LogManager.getLogger();
//
//    @OnOpen
//    public void onOpen(Session session) throws IOException {
//        LOG.info(" Get session and WebSocket connection " + session);
//    }
//
//    @OnMessage
//    public void onMessage(Session session, Message message) throws IOException {
//        LOG.info("Handle new messages " + session + " " + message);
//
//    }
//
//    @OnClose
//    public void onClose(Session session) throws IOException {
//        LOG.info("WebSocket connection closes " + session);
//
//    }
//
//    @OnError
//    public void onError(Session session, Throwable throwable) {
//        LOG.error("Do error handling here" + session, throwable);
//
//    }
//
//
//    public class Message {
//        private String from;
//        private String to;
//        private String content;
//
//        //standard constructors, getters, setters
//    }
//
//    public class MessageDecoder implements Decoder.Text<Message> {
//
//        private Gson gson = new Gson();
//
//        @Override
//        public Message decode(String s) throws DecodeException {
//            Message message = gson.fromJson(s, Message.class);
//            return message;
//        }
//
//        @Override
//        public boolean willDecode(String s) {
//            return (s != null);
//        }
//
//        @Override
//        public void init(EndpointConfig endpointConfig) {
//            // Custom initialization logic
//        }
//
//        @Override
//        public void destroy() {
//            // Close resources
//        }
//    }
//
//    public class MessageEncoder implements Encoder.Text<Message> {
//
//        private Gson gson = new Gson();
//
//        @Override
//        public String encode(Message message) throws EncodeException {
//            String json = gson.toJson(message);
//            return json;
//        }
//
//        @Override
//        public void init(EndpointConfig endpointConfig) {
//            // Custom initialization logic
//        }
//
//        @Override
//        public void destroy() {
//            // Close resources
//        }
//    }
//}
