package juniter.service.ws2p;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;

/**
 * read https://git.duniter.org/nodes/common/doc/blob/master/rfc/0004_ws2p_v1.md
 *
 *
 * @author ben
 *
 */
@ConditionalOnExpression("${juniter.ws2p.enabled:false}")
@Controller
@Order(1)
public class ConnectionPool implements CommandLineRunner {

	private static final Logger LOG = LogManager.getLogger();

	@Value("#{'${juniter.network.trusted}'.split(',')}")
	private List<String> trustedSources;

	@Value("${juniter.network.webSocketPoolSize:5}")
	private Integer webSocketPoolSize;

	public List<WS2PClient> list = new ArrayList<WS2PClient>();


	@Override
	public void run(String... args) throws Exception {

		LOG.info("Starting WebSocket ConnectionPool");

		final var client1 = new WS2PClient(new URI("wss://g1-monit.librelois.fr:443/ws2p"));
		//		final var client2 = new WS2PClient(new URI("wss://80.118.154.251:20900/"));

		//		client1.connect();


	}

}