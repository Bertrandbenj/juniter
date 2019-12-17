package juniter.service.ws2p;

import com.fasterxml.jackson.databind.ObjectMapper;
import juniter.core.model.dbo.index.BINDEX;
import juniter.service.core.Index;
import juniter.service.core.PeerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WSPeer extends TextWebSocketHandler {

    private static final Logger LOG = LogManager.getLogger(WSPeer.class);

    @Autowired
    private PeerService peerService;

    @Autowired
    private Index index;

    @Autowired
    private ObjectMapper objectMapper;

    private List<WebSocketSession> sessions = new CopyOnWriteArrayList();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOG.info("afterConnectionEstablished" + session);
        // keep all sessions (for broadcast)
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOG.info("afterConnectionClosed" + status + " - " + session);
        sessions.remove(session);
    }


    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

        index.head().map(BINDEX::getNumber).ifPresent(bl -> {
            try {
                var peer = objectMapper.writeValueAsString(peerService.endPointPeer(bl));
                session.sendMessage(new TextMessage(peer));
            } catch (Exception e) {
                LOG.error(e);
            }
        });
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        LOG.error("handleTransportError" + session, exception);

        session.close(CloseStatus.SERVER_ERROR);
    }

}
