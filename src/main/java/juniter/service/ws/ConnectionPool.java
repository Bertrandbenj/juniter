package juniter.service.ws;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Controller;

import com.google.common.collect.Lists;

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
@Order(1)
public class ConnectionPool implements CommandLineRunner, GlobalValid {

	private static final Logger LOG = LogManager.getLogger();

	@Value("#{'${juniter.network.trusted}'.split(',')}")
	private List<String> trustedSources;

	@Value("${juniter.network.webSocketPoolSize:5}")
	private Integer webSocketPoolSize;

	public List<WS2PClient> list = new ArrayList<WS2PClient>();

	@Autowired
	private BlockRepository blockRepo;
	List<CINDEX> indexCG = Lists.newArrayList();
	List<MINDEX> indexMG = Lists.newArrayList();
	List<IINDEX> indexIG = Lists.newArrayList();
	List<SINDEX> indexSG = Lists.newArrayList();

	@Override
	public boolean commit(Set<IINDEX> indexi, Set<MINDEX> indexm, Set<CINDEX> indexc, Set<SINDEX> indexs) {
		indexCG.addAll(indexc);
		indexMG.addAll(indexm);
		indexIG.addAll(indexi);
		indexSG.addAll(indexs);
		LOG.info("Commited Certs: " + indexc.size() + ", Membship: " + indexm.size() + ", Idty: " + indexi.size()
				+ ", TXInOut: " + indexs.size());

		return true;
	}

	@Override
	public Stream<CINDEX> indexCGlobal() {
		return indexCG.stream();
	}

	@Override
	public Stream<IINDEX> indexIGlobal() {
		return indexIG.stream();
	}

	@Override
	public Stream<MINDEX> indexMGlobal() {
		return indexMG.stream();
	}

	@Override
	public Stream<SINDEX> indexSGlobal() {
		return indexSG.stream();
	}

	private URI makeURI(String source) throws URISyntaxException {

		return new URI(source.replace("https", "wss") + "ws");
	}

	@Override
	public void run(String... args) throws Exception {

		final var client1 = new WS2PClient(new URI("wss://g1-monit.librelois.fr:443/ws2p"));
//		final var client2 = new WS2PClient(new URI("wss://80.118.154.251:20900/"));

//		client1.connect();

		for (int i = 0; i < 10; i++) {
			final var block = blockRepo.block(i).get();

			LOG.info("Indexing Block \n" + block);
			if (validate(block)) {
				LOG.info("Validated " + block);
			} else {
				LOG.warn("NOT Valid " + block);
			}
		}

	}

}