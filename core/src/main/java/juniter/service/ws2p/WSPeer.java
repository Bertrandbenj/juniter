package juniter.service.ws2p;

import com.fasterxml.jackson.databind.ObjectMapper;
import juniter.core.model.index.BINDEX;
import juniter.repository.jpa.BlockRepository;
import juniter.repository.jpa.index.BINDEXRepository;
import juniter.service.bma.NetworkService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WSPeer extends TextWebSocketHandler {

    private static final Logger LOG = LogManager.getLogger();


    private List<WebSocketSession> sessions = new CopyOnWriteArrayList();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    BlockRepository blockRepo;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOG.info("afterConnectionEstablished" + session);
        // keep all sessions (for broadcast)
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOG.info("afterConnectionClosed" +status+" - "+ session);
        sessions.remove(session);
    }



    @Autowired
    private NetworkService netService;


    @Autowired
    private BINDEXRepository bRepo;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {

        bRepo.head().map(BINDEX::getNumber).ifPresent(bl -> {
            try {
                var peer = objectMapper.writeValueAsString(netService.endPointPeer(bl));
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
