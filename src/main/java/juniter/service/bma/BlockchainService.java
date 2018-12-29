package juniter.service.bma;

import juniter.repository.jpa.BlockRepository;
import juniter.service.bma.loader.BlockLoader;
import juniter.service.bma.dto.Block;
import juniter.service.bma.dto.MembershipDTO;
import juniter.service.bma.dto.WithDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

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

	private static final Logger LOG = LogManager.getLogger();

	@Autowired
	private BlockRepository blockRepo;

	@Autowired
	private BlockLoader defaultLoader;

	@Autowired
	private ModelMapper modelMapper;

	@Transactional(readOnly = true)
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public List<juniter.core.model.Block> all() {

		LOG.info("Entering /blockchain/all");

		try (Stream<juniter.core.model.Block> items = blockRepo.findTop10ByOrderByNumberDesc()) {
			return items.collect(toList());
		} catch (final Exception e) {
			LOG.error(e);
			return null;
		}

	}

	@RequestMapping(value = "/block/{id}", method = RequestMethod.GET)
	public Block block(@PathVariable("id") Integer id) {

		LOG.info("Entering /blockchain/block/{number=" + id + "}");
		final var block = blockRepo.findTop1ByNumber(id).orElseGet(() -> defaultLoader.fetchAndSaveBlock(id));

		return convertToDto(block);
	}

	@Transactional
	@RequestMapping(value = "/blocks/{count}/{from}", method = RequestMethod.GET)
	public List<Block> block(@PathVariable("count") Integer count, @PathVariable("from") Integer from) {

		LOG.info("Entering /blockchain/blocks/{count=" + count + "}/{from=" + from + "}");

		final List<Integer> blocksToFind = IntStream.range(from, from + count).boxed().collect(toList());
		LOG.debug("---blocksToFind: " + blocksToFind);

		final List<juniter.core.model.Block> knownBlocks = blockRepo.findByNumberIn(blocksToFind).collect(toList());
		LOG.debug("---known blocks: " + knownBlocks.stream().map(b -> b.getNumber()).collect(toList()));

		final List<juniter.core.model.Block> blocksToSave = blocksToFind.stream()
				.filter(b -> !knownBlocks.stream().anyMatch(kb -> kb.getNumber().equals(b)))
				.map(lg -> defaultLoader.fetchAndSaveBlock(lg)).collect(toList());

		LOG.debug("---fetch blocks: " + Stream.concat(blocksToSave.stream(), knownBlocks.stream())
		.map(b -> b.getNumber().toString()).collect(joining(",")));

		blockRepo.saveAll(blocksToSave);

		return Stream.concat(blocksToSave.stream(), knownBlocks.stream()) //
				.map(b -> convertToDto(b)) //
				.collect(toList());
	}

	private Block convertToDto(juniter.core.model.Block block) {
		//		LOG.debug(" - Converting block " + block);

		final Block postDto = modelMapper.map(block, Block.class);

		return postDto;
	}

	@Transactional
	@RequestMapping(value = "/current", method = RequestMethod.GET)
	public Block current() {
		LOG.info("Entering /blockchain/current");
		final var b = blockRepo.findTop1ByOrderByNumberDesc()//
				.orElse(defaultLoader.fetchAndSaveBlock("current"));

		return convertToDto(b);
	}

	@RequestMapping(value = "/deleteBlock/{id}", method = RequestMethod.GET)
	public Block deleteBlock(@PathVariable("id") Integer id) {
		LOG.warn("Entering /blockchain/deleteBlock/{id=" + id + "}");

		blockRepo.block(id).ifPresent(block -> {
			blockRepo.delete(block);
		});

		return convertToDto(defaultLoader.fetchAndSaveBlock(id));
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
	public WithDTO with(@PathVariable("what") String what) {

		LOG.info("Entering /blockchain/with/{newcomers,certs,actives,leavers,excluded,ud,tx}");
		Stream<juniter.core.model.Block> st;
		switch (what) {
		case "newcomers":
			st = blockRepo.with(block -> !block.getJoiners().isEmpty());
			break;
		case "certs":
			st = blockRepo.with(block -> !block.getCertifications().isEmpty());
			break;
		case "actives":
			st = blockRepo.with(block -> !block.getRenewed().isEmpty());
			break;
		case "leavers":
			st = blockRepo.with(block -> !block.getLeavers().isEmpty());
			break;
		case "excluded":
			st = blockRepo.with(block -> !block.getExcluded().isEmpty());
			break;
		case "ud":
			st = blockRepo.with(block -> block.getDividend() != null);
			break;
		case "tx":
		default:
			st = blockRepo.with(block -> !block.getTransactions().isEmpty());
		}

		try (Stream<Integer> items = st.map(b -> b.getNumber())) {
			return new WithDTO(items.collect(toList()));
		} catch (final Exception e) {
			LOG.error(e);
			return null;
		}

	}

    @RequestMapping(value = "/membership", method = RequestMethod.POST)
    ResponseEntity<MembershipDTO> membership (HttpServletRequest request, HttpServletResponse response) {

        LOG.info("POSTING /blockchain/membership ..." + request.getRemoteHost());


		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
			LOG.info(in.lines().collect(Collectors.joining("\n")));
		}catch (Exception e ){
			LOG.error("error reading blockchain/membership inputStream ", e);
		}


		MembershipDTO membership = new MembershipDTO();
        final var headers = new HttpHeaders();


        return  new ResponseEntity<>(membership, headers, HttpStatus.OK);
    }


    @RequestMapping(value = "/block", method = RequestMethod.POST)
    ResponseEntity<juniter.core.model.Block> block (HttpServletRequest request, HttpServletResponse response) {

        LOG.info("POSTING /blockchain/block ..." + request.getRemoteHost());


		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
			LOG.info(in.lines().collect(Collectors.joining("\n")));
		}catch (Exception e ){
			LOG.error("error reading blockchain/block inputStream ", e);
		}


		juniter.core.model.Block block = new juniter.core.model.Block();
        final var headers = new HttpHeaders();


        return  new ResponseEntity<>(block, headers, HttpStatus.OK);
    }

}
