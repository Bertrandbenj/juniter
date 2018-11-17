package juniter.service.ws2p;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * read https://git.duniter.org/nodes/common/doc/blob/master/rfc/0004_ws2p_v1.md
 *
 *
 * @author ben
 *
 */
@ConditionalOnExpression("${juniter.ws2p.enabled:false}")
@Component
@Order(10)
public class ConnectionPool  {

	private static final Logger LOG = LogManager.getLogger();

	@Value("#{'${juniter.network.trusted}'.split(',')}")
	private List<String> configuredNodes;

	@Value("${juniter.network.webSocketPoolSize:5}")
	private Integer webSocketPoolSize;

	public List<WS2PClient> list = new ArrayList<WS2PClient>();


	@Scheduled(fixedDelay =  10 * 60 * 1000)
	public void run()  {

		LOG.info("Starting WebSocket ConnectionPool");

		try {
			final var client1 = new WS2PClient(new URI("wss://g1-monit.librelois.fr:443/ws2p"));
		} catch (URISyntaxException e) {
			LOG.error("ConnectionPool.run " , e);
		}
		//		final var client2 = new WS2PClient(new URI("wss://80.118.154.251:20900/"));

		//		client1.connect();


	}



}