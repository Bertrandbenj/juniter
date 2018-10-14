package juniter.service.duniter;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import juniter.model.persistence.Block;
import juniter.repository.BlockRepository;
import juniter.utils.Constants;

/**
 * Controller connecting to the comma separated properties
 *
 * <pre>
 * juniter.network.trusted=node1,node2,node3
 * </pre>
 *
 * @author ben
 *
 */
@Controller
@Order(2)
public class TrustedLoader {

	private static final Logger logger = LogManager.getLogger();

	@Value("#{'${juniter.network.trusted}'.split(',')}")
	private List<String> trustedSources;

	@Value("${juniter.network.bulkSize:50}")
	private Integer bulkSize;

	private AtomicInteger id = new AtomicInteger();

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private BlockRepository blockRepo;

	public String any() {
		return trustedSources.stream().findAny().get();
	}

	@Transactional
	public void bulkLoad() {

		final var start = System.nanoTime();
		final var lastBLock = fetchAndSaveBlock("current").getNumber();
		if (blockRepo.count() > 8000) {
			logger.warn(" = Ignore bulk loading " + blockRepo.count() + " blocks");
			return;
		}

		logger.info(" ======== Start BulkLoading ======== " + blockRepo.count() + " blocks");

		final var nbPackage = Integer.divideUnsigned(lastBLock, bulkSize);

		final var result = IntStream.range(0, nbPackage)// get nbPackage Integers
				.map(nbb -> (nbb * bulkSize)) // with an offset of bulkSize
				.boxed() //
				.sorted() //
				.parallel() // parallel stream if needed
				.map(i -> rotating() + "blockchain/blocks/" + bulkSize + "/" + i) // url
				.map(url -> fetchBlocks(url)) // fetch the list of blocks
				.flatMap(list -> list.stream()) // blocks individually
				.map(b -> blockRepo.save(b)) // persist
//				.map(bl -> bl.getNumber()) // collect as list of block number
				.collect(Collectors.toList()); //

		final var elapsed = Long.divideUnsigned(System.nanoTime() - start, 1000000);

		logger.info("Bulkloaded " + result.size() + " in " + elapsed + " ms");
	}

	/**
	 * Wrapper for fetchAndSaveBlock(String)
	 *
	 * @param number
	 * @return
	 */
	public Block fetchAndSaveBlock(Integer number) {
		return fetchAndSaveBlock("block/" + number);
	}

	/**
	 * Fetch a block and save it synchronously
	 *
	 * @param id the block id
	 */
	@Transactional
	public Block fetchAndSaveBlock(String id) {
		final var node = any();
		final var url = node + "blockchain/" + id;
		logger.info("Fetching block : " + url);
		Block block = null;
		try {
			TimeUnit.MILLISECONDS.sleep(200);
			block = restTemplate.getForObject(url, Block.class);
			block = blockRepo.block(block.getNumber()).orElse(block);
			block = blockRepo.save(block);

			logger.info("... saved block : " + block);

		} catch (final Exception e) {
//			synchronized (trustedSources) {
//				trustedSources.remove(node);
//			}
			logger.error("Problem on node " + node + " " + trustedSources, e);
		}

		return block;
	}

	/**
	 * uses /blockchain/blocks/[count]/[from]
	 *
	 * @param url
	 * @return
	 */
	@Transactional
	public List<Block> fetchBlocks(String url) {
		try {
			TimeUnit.MILLISECONDS.sleep(30);

			final var responseEntity = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<Block>>() {
					});
			final var body = responseEntity.getBody();
			final var contentType = responseEntity.getHeaders().getContentType().toString();
			final var statusCode = responseEntity.getStatusCode().getReasonPhrase();

			logger.info("Fetched: " + url + "... Status: " + statusCode + " ContentType: " + contentType);
			return body;

		} catch (final InterruptedException e) {
			logger.error(Constants.Logs.INTERRUPTED);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String rotating() {
		final var ai = id.incrementAndGet() % trustedSources.size();

		return trustedSources.get(ai);
	}
}
