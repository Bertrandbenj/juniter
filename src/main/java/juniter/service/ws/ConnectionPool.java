package juniter.service.ws;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Controller;

import juniter.repository.BlockRepository;
import juniter.utils.GlobalValid;

/**
 * read https://git.duniter.org/nodes/common/doc/blob/master/rfc/0004_ws2p_v1.md
 *
 *
 * @author ben
 *
 */
@ConditionalOnExpression("${juniter.ws2p.enabled:false}")
@Controller
public class ConnectionPool implements CommandLineRunner, GlobalValid {

	private static final Logger LOG = LogManager.getLogger();

	@Value("#{'${juniter.network.trusted}'.split(',')}")
	private List<String> trustedSources;

	@Value("${juniter.network.webSocketPoolSize:5}")
	private Integer webSocketPoolSize;

	public List<WS2PClient> list = new ArrayList<WS2PClient>();

	@Autowired
	private BlockRepository blockRepo;

	@Override
	public Integer computeIssuersCount() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getTransactionDepth() {
		// TODO Auto-generated method stub

	}

	@Override
	public BINDEX[] indexB() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CINDEX[] indexC() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream<CINDEX> indexCGlobal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IINDEX[] indexI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream<IINDEX> indexIGlobal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MINDEX[] indexM() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream<MINDEX> indexMGlobal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public SINDEX[] indexS() {
		// TODO Auto-generated method stub
		return null;
	}

	private URI makeURI(String source) throws URISyntaxException {

		return new URI(source.replace("https", "wss") + "ws");
	}

	@Override
	public void run(String... args) throws Exception {
		final var client1 = new WS2PClient(new URI("wss://g1-monit.librelois.fr:443/ws2p"));
		final var client2 = new WS2PClient(new URI("wss://80.118.154.251:20900/"));

		client1.connect();

	}

}