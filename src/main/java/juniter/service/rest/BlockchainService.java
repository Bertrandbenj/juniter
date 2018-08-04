package juniter.service.rest;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import juniter.model.Block;
import juniter.model.bma.BlockDTO;
import juniter.model.bma.WithWrapper;
import juniter.repository.BlockRepository;
import juniter.service.utils.TrustedLoader;

/**
 *
 * Blockchain sub-root of the HTTP API
 *
 * <pre>
 * |-- blockchain/
 * |   |-- parameters
 * |   |-- membership
 * |   |-- with/
 * |       |-- newcomers
 * |       |-- certs
 * |       |-- joiners
 * |       |-- actives
 * |       |-- leavers
 * |       |-- excluded
 * |       |-- ud
 * |       `-- tx
 * |   |-- hardship
 * |   |   `-- [PUBKEY]
 * |   |-- block
 * |   |   `-- [NUMBER]
 * |   |-- difficulties
 * |   `-- current
 * </pre>
 *
 * @author ben
 *
 */
@RestController
@ConditionalOnExpression("${juniter.bma.enabled:false}")
@RequestMapping("/blockchain")
public class BlockchainService {

	private static final Logger logger = LogManager.getLogger();

	@Autowired
	private BlockRepository repository;

	@Autowired
	private TrustedLoader trustedLoader;

	@Autowired
	private ModelMapper modelMapper;

	@Transactional(readOnly = true)
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public List<Block> all() {

		logger.info("Entering /blockchain/all");

		try (Stream<Block> items = repository.findTop10ByOrderByNumberDesc()) {
			return items.collect(toList());
		} catch (final Exception e) {
			logger.error(e);
			return null;
		}

	}

	@RequestMapping(value = "/block/{id}", method = RequestMethod.GET)
	public BlockDTO block(@PathVariable("id") Integer id) {

		logger.info("Entering /blockchain/block/{number=" + id + "}");
		final var block = repository.findTop1ByNumber(id).orElseGet(() -> trustedLoader.fetchAndSaveBlock(id));

		return convertToDto(block);
	}

	@Transactional
	@RequestMapping(value = "/blocks/{count}/{from}", method = RequestMethod.GET)
	public List<BlockDTO> block(@PathVariable("count") Integer count, @PathVariable("from") Integer from) {

		logger.info("Entering /blockchain/blocks/{count=" + count + "}/{from=" + from + "}");

		final List<Integer> blocksToFind = IntStream.range(from, from + count).boxed().collect(toList());
		logger.info("---blocksToFind: " + blocksToFind);

		final List<Block> knownBlocks = repository.findByNumberIn(blocksToFind).collect(toList());
		logger.info("---known blocks: " + knownBlocks.stream().map(b -> b.getNumber()).collect(toList()));

		final List<Block> blocksToSave = blocksToFind.stream()
				.filter(b -> !knownBlocks.stream().anyMatch(kb -> kb.getNumber().equals(b)))
				.map(lg -> trustedLoader.fetchAndSaveBlock(lg)).collect(toList());

		logger.info("---fetch blocks: " + Stream.concat(blocksToSave.stream(), knownBlocks.stream())
				.map(b -> b.getNumber().toString()).collect(joining(",")));

		repository.saveAll(blocksToSave);

		return Stream.concat(blocksToSave.stream(), knownBlocks.stream()) //
				.map(b -> convertToDto(b)) //
				.collect(toList());
	}

	private BlockDTO convertToDto(Block block) {
		logger.info(" - Converting block " + block);

		final BlockDTO postDto = modelMapper.map(block, BlockDTO.class);

		return postDto;
	}

	@Transactional
	@RequestMapping(value = "/current", method = RequestMethod.GET)
	public BlockDTO current() {
		logger.info("Entering /blockchain/current");
		final var b = repository.findTop1ByOrderByNumberDesc()//
				.orElse(trustedLoader.fetchAndSaveBlock("current"));

		return convertToDto(b);
	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	void handle(HttpServletResponse response) throws IOException {
		response.sendRedirect("/html/");
	}

	/**
	 * /blockchain/with/{what=[newcomers,certs,actives,leavers,excluded,ud,tx]}
	 *
	 * <p>
	 * Filters according to 'what' you desire
	 * </p>
	 * <p>
	 * Sort by number
	 * </p>
	 *
	 * @param what
	 * @return A Wrapped List of Blocks
	 */
	@RequestMapping(value = "/with/{what}", method = RequestMethod.GET)
	@Transactional(readOnly = true)
	public WithWrapper with(@PathVariable("what") String what) {

		logger.info("Entering /blockchain/with/{newcomers,certs,actives,leavers,excluded,ud,tx}");
		Stream<Block> st;
		switch (what) {
		case "newcomers":
			st = repository.with(block -> !block.getJoiners().isEmpty());
			break;
		case "certs":
			st = repository.with(block -> !block.getCertifications().isEmpty());
			break;
		case "actives":
			st = repository.with(block -> !block.getActives().isEmpty());
			break;
		case "leavers":
			st = repository.with(block -> !block.getLeavers().isEmpty());
			break;
		case "excluded":
			st = repository.with(block -> !block.getExcluded().isEmpty());
			break;
		case "ud":
			st = repository.with(block -> block.getDividend() != null);
			break;
		case "tx":
		default:
			st = repository.with(block -> !block.getTransactions().isEmpty());
		}

		try (Stream<Integer> items = st.map(b -> b.getNumber())) {
			return new WithWrapper(items.collect(toList()));
		} catch (final Exception e) {
			logger.error(e);
			return null;
		}

	}
}
