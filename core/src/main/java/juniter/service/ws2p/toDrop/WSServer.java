package juniter.service.ws2p.toDrop;

//
//public class WSServer extends WebSocketServer {
//
//    private static final Logger LOG = LogManager.getLogger();
//
//
//    public WSServer(InetSocketAddress address) {
//        super(address);
//    }
//
//    @Override
//    public void onOpen(WebSocket conn, ClientHandshake handshake) {
//        conn.send("Welcome to the BLOCK server!"); //This method sends a message to the new client
//
//        //broadcast( "new connection: " + handshake.getResourceDescriptor() ); //This method sends a message to all clients connected
//
//
//        LOG.info("new connection to " + conn.getRemoteSocketAddress());
//    }
//
//    @Override
//    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
//        LOG.info("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
//    }
//
//    @Override
//    public void onMessage(WebSocket conn, String message) {
//        LOG.info("received message from "	+ conn.getRemoteSocketAddress() + ": " + message);
//    }
//
//    @Override
//    public void onMessage( WebSocket conn, ByteBuffer message ) {
//        LOG.info("received ByteBuffer from "	+ conn.getRemoteSocketAddress());
//    }
//
//    @Override
//    public void onError(WebSocket conn, Exception ex) {
//        LOG.error("an error occured on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
//    }
//
//    @Override
//    public void onStart() {
//        LOG.info("server started successfully");
//    }
//
//
//}
