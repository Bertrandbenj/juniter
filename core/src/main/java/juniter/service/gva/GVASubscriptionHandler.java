package juniter.service.gva;

/*-
 * #%L
 * SUMARiS:: Server
 * %%
 * Copyright (C) 2018 SUMARiS Consortium
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class GVASubscriptionHandler extends TextWebSocketHandler {

    private static final Logger LOG = LogManager.getLogger(GVASubscriptionHandler.class);

    private final AtomicReference<Subscription> subscriptionRef = new AtomicReference<>();


    private final boolean debug;

    private List<WebSocketSession> sessions = new CopyOnWriteArrayList();

    @Autowired
    private GraphQL graphQL;

    @Autowired
    private ObjectMapper objectMapper;


    @Autowired
    public GVASubscriptionHandler() {
        this.debug = LOG.isDebugEnabled();
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        LOG.info("afterConnectionEstablished" + session);
        // keep all sessions (for broadcast)
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LOG.info("afterConnectionClosed " + status + " " + session);
        sessions.remove(session);
        if (subscriptionRef.get() != null) subscriptionRef.get().cancel();
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        LOG.info("handleTextMessage " + session+" \n" + message);

        Map<String, Object> request;
        try {
            request = objectMapper.readValue(message.asBytes(), Map.class);

        }
        catch(IOException e) {
            LOG.error(e.getMessage(),e);
            return;
        }

        String type = Objects.toString(request.get("type"), "start");
        if ("connection_init".equals(type)) {
            handleInitConnection(session, request);
        }
        else if ("stop".equals(type)) {
            if (subscriptionRef.get() != null) subscriptionRef.get().cancel();
        }
        else if ("start".equals(type)) {
            handleStartConnection(session, request);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        LOG.error("handleTransportError" + session, exception);

        session.close(CloseStatus.SERVER_ERROR);
    }

    /* -- protected methods -- */

    protected void handleInitConnection(WebSocketSession session, Map<String, Object> request) {
        LOG.info("handleInitConnection " + session + "\n" + request);


        Map<String, Object> payload = (Map<String, Object>) request.get("payload");
        String authToken = MapUtils.getString(payload, "authToken","");

        // Has token: try to authenticate
        if (authToken.isBlank()) {

            // try to authenticate
//            try {
//                Optional<AuthUser> authUser = authService.authenticate(authToken);
//                // If success
//                if (authUser.isPresent()) {
//                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(authUser.get().getUsername(), authToken, authUser.get().getAuthorities());
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                    return; // OK
//                }
//            }
//            catch(AuthenticationException e) {
//                log.warn("Unable to authenticate websocket session, using token: " + e.getMessage());
//                // Continue
//            }
        }

        // Not auth: send a new challenge
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(
                    ImmutableMap.of(
                            "type", "error",
                            "payload", ImmutableMap.of("message", "blabla",
                                    "challenge", "challegneeeee")
                    ))));
        } catch (IOException e) {
            LOG.error(e);
        }
    }

    protected void handleStartConnection(WebSocketSession session, Map<String, Object> request) {
        LOG.info("handleStartConnection " + session );

        Map<String, Object> payload = (Map<String, Object>) request.get("payload");
        final Object opId = request.get("id");

        // Check authenticated
//        if (!isAuthenticated()) {
//            try {
//                session.close(CloseStatus.SERVICE_RESTARTED);
//            } catch (IOException e) {
//                // continue
//            }
//            return;
//        }


        String query = Objects.toString(payload.get("query"));
        String op = (String) payload.get("operationName");
        LOG.info("Operations  " + op + " - " + query );

        ExecutionResult executionResult = graphQL.execute(ExecutionInput.newExecutionInput()
                .query(query)

                .operationName(op)

                .variables(GraphQLHelper.getVariables(payload, objectMapper))
                .build());

        // If error: send error then disconnect
        if (CollectionUtils.isNotEmpty(executionResult.getErrors())) {
            sendResponse(session,
                         ImmutableMap.of(
                                "id", opId,
                                "type", "error",
                                "payload", GraphQLHelper.processExecutionResult(executionResult))
                );
            return;
        }

        Publisher<ExecutionResult> stream = executionResult.getData();

        stream.subscribe(new Subscriber<ExecutionResult>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                subscriptionRef.set(subscription);
                if (subscriptionRef.get() != null) subscriptionRef.get().request(1);
            }

            @Override
            public void onNext(ExecutionResult result) {
                sendResponse(session, ImmutableMap.of(
                        "id", opId,
                        "type", "data",
                        "payload", GraphQLHelper.processExecutionResult(result))
                );

                if (subscriptionRef.get() != null) subscriptionRef.get().request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                sendResponse(session,
                        ImmutableMap.of(
                                "id", opId,
                                "type", "error",
                                "payload", GraphQLHelper.processError(throwable))
                );
            }

            @Override
            public void onComplete() {
                try {
                    session.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        });
    }

    protected boolean isAuthenticated() {
        LOG.info("isAuthenticated");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated());
    }

    protected void sendResponse(WebSocketSession session, Object value) {
        LOG.info("sendResponse to " + session.getId() + " " + session.getRemoteAddress());
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(value)));
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }


}
