package juniter.service.dev;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import juniter.service.bma.DefaultLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import juniter.core.utils.TimeUtils;
import juniter.repository.jpa.BlockRepository;
import juniter.service.bma.model.WithWrapper;

/**
 * <pre>
 * This thread spawns with the application and does the following
 *
 * 1 - Access a remote trusted node for /blockchain/with/*
 * 2 - Sort & distinct the list of block numbers
 * 3 - Download those blocks and persist them
 * 4 - Measure elapsed time then die
 * </pre>
 *
 * @author ben
 *
 */
@ConditionalOnExpression("${juniter.usefulloader.enabled:false}")
@Component
@Order(4)
public class UsefulBlocksLoader implements CommandLineRunner {

	private static final Logger LOG = LogManager.getLogger();

	@Autowired
	BlockRepository blockRepo;

	@Autowired
	private DefaultLoader defaultLoader;

	@Autowired
	private RestTemplate restTemplate;

	@Transactional
	private List<Integer> fetchUsefullBlocks() {
		final String url = defaultLoader.anyNotIn(null).get() + "blockchain/with/";
		LOG.info("Loading from : " + url);
		final List<Integer> res = new ArrayList<>();
		try {

			var ww = restTemplate.getForObject(url + "tx", WithWrapper.class);
			LOG.info(" - Fetching Tx List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());

			ww = restTemplate.getForObject(url + "certs", WithWrapper.class);
			LOG.info(" - Fetching Certs List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());

			ww = restTemplate.getForObject(url + "newcomers", WithWrapper.class);
			LOG.info(" - Fetching NewComers List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());

			ww = restTemplate.getForObject(url + "leavers", WithWrapper.class);
			LOG.info(" - Fetching Leavers List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());

			ww = restTemplate.getForObject(url + "revoked", WithWrapper.class);
			LOG.info(" - Fetching Revoked List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());

			ww = restTemplate.getForObject(url + "excluded", WithWrapper.class);
			LOG.info(" - Fetching Excluded List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());

			ww = restTemplate.getForObject(url + "actives", WithWrapper.class);
			LOG.info(" - Fetching Actives List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());

			ww = restTemplate.getForObject(url + "ud", WithWrapper.class);
			LOG.info(" - Fetching UD List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());

			final var list = res.stream().distinct().sorted().collect(toList());
			LOG.info(" - Fetching Total " + list.size());
			return list;
		} catch (final Exception e) {
			LOG.error(e);
		}
		return new ArrayList<>();
	}

	@Override
	public void run(String... args) throws Exception {
		final var start = System.nanoTime();

		fetchUsefullBlocks() //
		.stream() //
		.parallel() //
		.map(i -> blockRepo.block(i).orElseGet(() -> defaultLoader.fetchAndSaveBlock(i))) //
		.collect(toList());

		final var elapsed = Long.divideUnsigned(System.nanoTime() - start, 1000000);
		LOG.info("Elapsed time: " + TimeUtils.format(elapsed));
	}

}
