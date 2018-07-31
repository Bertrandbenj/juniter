package juniter.service.ws;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import juniter.model.net.Greeting;
import juniter.model.net.HelloMessage;

/**
 * read https://spring.io/guides/gs/messaging-stomp-websocket/
 * 
 * 
 * @author ben
 *
 */
@ConditionalOnExpression("${juniter.ws2p.enabled:false}")
@Controller
public class WS2PHandler {
	
	private static final Logger logger = LogManager.getLogger();

    @MessageMapping("/hello")
    @SendTo("/ws2p/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
    	
    	 logger.info("Entering /hello... ");
    	
        Thread.sleep(1000); // simulated delay
       
        
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

}