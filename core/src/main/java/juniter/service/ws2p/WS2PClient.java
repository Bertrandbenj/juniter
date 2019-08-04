package juniter.service.ws2p;

import antlr.generated.JuniterLexer;
import antlr.generated.JuniterParser;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import juniter.core.event.Indexing;
import juniter.core.model.wso.ResponseBlock;
import juniter.core.model.wso.ResponseBlocks;
import juniter.core.model.wso.ResponseWotPending;
import juniter.grammar.JuniterGrammar;
import org.antlr.v4.runtime.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.nio.channels.NotYetConnectedException;
import java.util.HashMap;
import java.util.Map;

/**
 * This example demonstrates how to create a websocket connection to a server.
 * Only the most important callbacks are overloaded.
 */
public class WS2PClient extends WebSocketClient {

    private static final Logger LOG = LogManager.getLogger(WS2PClient.class);
    private Map<String, Request> sentRequests = new HashMap<String, Request>();


    private WebSocketPool webSocketPool;


    public WS2PClient(URI uri, WebSocketPool webSocketPool) {
        super(uri);
        this.webSocketPool = webSocketPool;
    }

//    public WS2PClient(URI serverUri, Draft draft) {
//        super(serverUri, draft);
//    }
//
//    public WS2PClient(URI serverUri, Map<String, String> httpHeaders) {
//        super(serverUri, httpHeaders);
//    }

    private void actionOnConnect() {
        //send(new Request().getBlock(1));
        //send(new Request().getBlocks(2, 3));
        send(new Request().getCurrent());
        //send(new Request().getRequirementsPending(5));


    }

    private void handleChallenge(String message) {

        try {

            final var challenge = webSocketPool.jsonMapper.readValue(message, Connect.class);

            LOG.debug("Challenge ... " + challenge);

            if (challenge.isACK()) {
                send(challenge.okJson());
                LOG.info("ACK, sending OK ");
                return;
            }

            if (challenge.isConnect()) {
                send(challenge.ackJson());
                LOG.info("CONNECT, sent ACK ");
                return;
            }

            if (challenge.isOK()) {
                LOG.info("OK, connected !! ");
                webSocketPool.clients.offer(this);
                actionOnConnect();
                return;
            }
        } catch (final Exception e) {
            LOG.error("Exception ", e);
        }

        return;

    }

    void handleDUP(String message) {
        LOG.info("handle Document");
        final var parser = juniterParser(CharStreams.fromString(message));
        final var doc = new JuniterGrammar().visitDoc(parser.doc());

    }

    private void handleRequest(String message) {
        LOG.info("handle Request " + webSocketPool.status());
        try {
            final var request = webSocketPool.jsonMapper.readValue(message, Request.class);
            // TODO respond
        } catch (final JsonParseException e) {
            LOG.error("handleRequest JSON parsing error ", e);
        } catch (final JsonMappingException e) {
            LOG.error("handleRequest JSON mapping error ", e);
        } catch (final IOException e) {
            LOG.error("handleRequest IO mapping error ", e);
        }
    }

    private void handleResponse(String message) {
        LOG.info("handle Response");
        try {
            final var resid = message.substring(10, 18);

            final var req = sentRequests.remove(resid);
            final var params = req.getBody().getParams();
            final var jsonMapper = webSocketPool.jsonMapper;

            switch (req.getBody().getName()) {
                case "BLOCK_BY_NUMBER":
                    final var block = jsonMapper.readValue(message, ResponseBlock.class);
                    LOG.info("BLOCK_BY_NUMBER " + block.getBody());
                    break;
                case "BLOCKS_CHUNK":
                    final var blocks = jsonMapper.readValue(message, ResponseBlocks.class);
                    LOG.info("BLOCKS_CHUNK " + blocks.getBody());
                    break;
                case "CURRENT":
                    final var current = jsonMapper.readValue(message, ResponseBlock.class);
                    LOG.info("CURRENT " + current.getBody());
                    webSocketPool.applicationEventPublisher.publishEvent(new Indexing(true));

                    webSocketPool.blockService.localSave(current.getBody());
                    webSocketPool.index.indexUntil(Integer.MAX_VALUE, false);

                    LOG.info("SAVED # " + current.getBody().getNumber());
                    break;
                case "WOT_REQUIREMENTS_OF_PENDING":
                    final var wot = jsonMapper.readValue(message, ResponseWotPending.class);
                    LOG.info("WOT_REQUIREMENTS_OF_PENDING " + wot.getBody());
                    break;
                default:
                    LOG.warn("Unknown Response Name " + req.getBody().getName());
            }
        } catch (final JsonParseException e) {
            LOG.error("handleResponse JSON parsing error ", e);
        } catch (final JsonMappingException e) {
            LOG.error("handleResponse JSON mapping error ", e);
        } catch (final IOException e) {
            LOG.error("handleResponse IO mapping error ", e);
        }
    }

    private JuniterParser juniterParser(CharStream file) {
        final JuniterLexer l = new JuniterLexer(file);
        final JuniterParser p = new JuniterParser(new CommonTokenStream(l));

        p.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line,
                                    int charPositionInLine, String msg, RecognitionException e) {
                throw new IllegalStateException("failed to parse at line " + line + " due to " + msg, e);
            }
        });

        return p;
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // The codecodes are documented in class org.java_websocket.framing.CloseFrame
        LOG.info("Connection closed by " + (remote ? "remote peer" : "us") + " Code: " + code + " on URI " + getURI());
        LOG.info(" - Reason: " + reason);
        LOG.info(" - " + webSocketPool.status());

        webSocketPool.clients.remove(this);

    }

    @Override
    public void onError(Exception ex) {
        if (ex instanceof SSLException) {
            LOG.warn("WS SSL onError " + getURI() + ex);
        } else if (ex instanceof SSLHandshakeException) {
            LOG.warn("WS SSL Handshake onError " + getURI() + ex);
        } else if (ex instanceof ConnectException) {
            LOG.warn("WS ConnectException onError " + getURI() + ex);
        } else {
            LOG.error("WS onError " + getURI(), ex);

        }
    }

    @Override
    public void onMessage(String message) {
        LOG.info("received: " + message.substring(0, 50) + "...");

        if (message.startsWith("{\"auth\":")) {
            handleChallenge(message);
        } else if (message.startsWith("{\"resId")) {
            handleResponse(message);
        } else if (message.startsWith("{\"reqId")) {
            handleRequest(message);
        } else if (message.startsWith("Version:")) {
            handleDUP(message);
        } else {
            LOG.warn("received unknown message: " + message);
        }
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        LOG.info("WS connection - http " + handshakedata.getHttpStatus() + " - " + webSocketPool.status());

//		while (true) {
//			try {
//				Thread.sleep(5000);
//				LOG.info("PINGING !! ");
//				sendPing();
//
//			} catch (final InterruptedException e) {
//				e.printStackTrace();
//				close(1000, "InterruptedException ");
//			}
//		}
        // final var sendChallenge = Connect.make().connectJson();
        // send(sendChallenge);

        // LOG.info("sent " + sendChallenge);
        // if you plan to refuse connection based on ip or httpfields overload:
        // onWebsocketHandshakeReceivedAsClient
    }

    void send(Request req) {
        try {
            sentRequests.put(req.getReqId(), req); // save for reuse

            send(webSocketPool.jsonMapper.writeValueAsString(req));
        } catch (NotYetConnectedException | JsonProcessingException e) {
            LOG.error(e);
        }
    }

    @Override
    public void send(String text) throws NotYetConnectedException {
        LOG.info("sending : " + text);
        super.send(text);
    }

}