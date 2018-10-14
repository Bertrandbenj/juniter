package juniter.service.duniter;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import juniter.model.bma.WithWrapper;
import juniter.repository.BlockRepository;

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
@ConditionalOnExpression("${juniter.simpleloader.enabled:false}")
@Component
@Order(3)
public class UsefulBlocksLoader implements CommandLineRunner {

	private static final Logger logger = LogManager.getLogger();

	@Autowired
	BlockRepository blockRepo;

	@Autowired
	private TrustedLoader trustedLoader;

	@Autowired
	private RestTemplate restTemplate;

	@Transactional
	private List<Integer> fetchUsefullBlocks() {
		final String url = trustedLoader.any() + "blockchain/with/";
		logger.info("Loading from : " + url);
		final List<Integer> res = new ArrayList<>();
		try {

			var ww = restTemplate.getForObject(url + "tx", WithWrapper.class);
			logger.info(" - Fetching Tx List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());

			ww = restTemplate.getForObject(url + "certs", WithWrapper.class);
			logger.info(" - Fetching Certs List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());

			ww = restTemplate.getForObject(url + "newcomers", WithWrapper.class);
			logger.info(" - Fetching NewComers List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());

			ww = restTemplate.getForObject(url + "leavers", WithWrapper.class);
			logger.info(" - Fetching Leavers List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());

			ww = restTemplate.getForObject(url + "revoked", WithWrapper.class);
			logger.info(" - Fetching Revoked List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());

			ww = restTemplate.getForObject(url + "excluded", WithWrapper.class);
			logger.info(" - Fetching Excluded List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());

			ww = restTemplate.getForObject(url + "actives", WithWrapper.class);
			logger.info(" - Fetching Actives List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());

			ww = restTemplate.getForObject(url + "ud", WithWrapper.class);
			logger.info(" - Fetching UD List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());

			final var list = res.stream().distinct().sorted().collect(toList());
			logger.info(" - Fetching Total " + list.size());
			return list;
		} catch (final Exception e) {
			logger.error(e);
		}
		return new ArrayList<>();
	}

	@Override
	public void run(String... args) throws Exception {
		final var start = System.nanoTime();
		trustedLoader.bulkLoad();

		fetchUsefullBlocks() //
				.stream() //
				.parallel() //
				.map(i -> blockRepo.block(i).orElseGet(() -> trustedLoader.fetchAndSaveBlock(i))) //
				.collect(toList());

		final var elapsed = Long.divideUnsigned(System.nanoTime() - start, 1000000);
		logger.info("Elapsed time: " + elapsed + "ms");
	}

}
