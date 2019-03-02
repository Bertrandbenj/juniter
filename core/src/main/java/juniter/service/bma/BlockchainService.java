package juniter.service.bma;

import juniter.core.model.DBBlock;
import juniter.core.model.business.ChainParameters;
import juniter.core.model.business.ChainParametersDTO;
import juniter.core.model.dto.*;
import juniter.core.model.index.MINDEX;
import juniter.repository.jpa.BlockRepository;
import juniter.repository.jpa.index.MINDEXRepository;
import juniter.service.bma.loader.BlockLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

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
 */
@RestController
@ConditionalOnExpression("${juniter.useBMA:false}")
@RequestMapping("/blockchain")
public class BlockchainService {

    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private BlockRepository blockRepo;


    @Autowired
    private MINDEXRepository mRepo;

    @Autowired
    private BlockLoader defaultLoader;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public List<DBBlock> all() {

        LOG.info("Entering /blockchain/all");

        try (Stream<DBBlock> items = blockRepo.findTop10ByOrderByNumberDesc()) {
            return items.collect(toList());
        } catch (final Exception e) {
            LOG.error(e);
            return null;
        }
    }

    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/block/{id}", method = RequestMethod.GET)
    public Block block(@PathVariable("id") Integer id) {

        LOG.info("Entering /blockchain/block/{number=" + id + "}");
        final var block = blockRepo
                .findTop1ByNumber(id)
                .orElseGet(() -> defaultLoader.fetchAndSaveBlock(id));

        return modelMapper.map(block, Block.class);
    }

    @CrossOrigin(origins = "*")
    @Transactional
    @RequestMapping(value = "/blocks/{count}/{from}", method = RequestMethod.GET)
    public List<Block> blocks(@PathVariable("count") Integer count, @PathVariable("from") Integer from) {

        LOG.info("Entering /blockchain/blocks/{count=" + count + "}/{from=" + from + "}");

        final List<Integer> blocksToFind = IntStream.range(from, from + count).boxed().collect(toList());
        LOG.debug("---blocksToFind: " + blocksToFind);

        final List<DBBlock> knownBlocks = blockRepo.findByNumberIn(blocksToFind).collect(toList());
        LOG.debug("---known blocks: " + knownBlocks.stream().map(DBBlock::getNumber).collect(toList()));

        final List<DBBlock> blocksToSave = blocksToFind.stream()
                .filter(b -> knownBlocks.stream().noneMatch(kb -> kb.getNumber().equals(b)))
                .map(lg -> defaultLoader.fetchAndSaveBlock(lg)).collect(toList());

        LOG.debug("---fetchTrimmed blocks: " + Stream.concat(blocksToSave.stream(), knownBlocks.stream())
                .map(b -> b.getNumber().toString()).collect(joining(",")));

        blockRepo.saveAll(blocksToSave);

        return Stream.concat(blocksToSave.stream(), knownBlocks.stream()) //
                .map(b -> modelMapper.map(b, Block.class)) //
                .collect(toList());
    }


    @CrossOrigin(origins = "*")
    @Transactional
    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public Block current() {
        LOG.info("Entering /blockchain/current");
        final var b = blockRepo.current()
                .orElse(defaultLoader.fetchAndSaveBlock("current"));

        return modelMapper.map(b, Block.class);
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
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/with/{what}", method = RequestMethod.GET)
    @Transactional(readOnly = true)
    public WithDTO with(@PathVariable("what") String what) {

        LOG.info("Entering /blockchain/with/{newcomers,certs,actives,leavers,excluded,ud,tx}");
        Stream<DBBlock> st;
        switch (what) {
            case "newcomers":
                return new WithDTO(blockRepo.withNewCommers());
            case "certs":
                return new WithDTO(blockRepo.withCertifications());
            case "actives":
            case "renewed":
                return new WithDTO(blockRepo.withRenewed());
            case "leavers":
                return new WithDTO(blockRepo.withLeavers());
            case "excluded":
                return new WithDTO(blockRepo.withExcluded());
            case "ud":
                return new WithDTO(blockRepo.withUD());
            case "tx":
                return new WithDTO(blockRepo.withTx());

            default:
                st = blockRepo.with(block -> !block.getTransactions().isEmpty());
        }

        try (Stream<Integer> items = st.map(DBBlock::getNumber)) {
            return new WithDTO(items.collect(toList()));
        } catch (final Exception e) {
            LOG.error(e);
            return null;
        }

    }

    // ======= POST =======

    @RequestMapping(value = "/membership", method = RequestMethod.POST)
    ResponseEntity<MembershipDTO> membership(HttpServletRequest request, HttpServletResponse response) {

        LOG.info("POSTING /blockchain/membership ..." + request.getRemoteHost());

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            LOG.info(in.lines().collect(Collectors.joining("\n")));
        } catch (Exception e) {
            LOG.error("error reading blockchain/membership inputStream ", e);
        }

        MembershipDTO membership = new MembershipDTO();
        final var headers = new HttpHeaders();

        return new ResponseEntity<>(membership, headers, HttpStatus.OK);
    }


    @Transactional(readOnly = true)
    @RequestMapping(value = "/memberships/{search}", method = RequestMethod.GET)
    Stream<MINDEX> memberships(@PathVariable("search") String search) {
        return mRepo.search(search);
    }


    @RequestMapping(value = "/block", method = RequestMethod.POST)
    ResponseEntity<DBBlock> block(HttpServletRequest request, HttpServletResponse response) {

        LOG.info("POSTING /blockchain/block ..." + request.getRemoteHost());


        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            LOG.info(in.lines().collect(Collectors.joining("\n")));
        } catch (Exception e) {
            LOG.error("error reading blockchain/block inputStream ", e);
        }


        DBBlock block = new DBBlock();
        final var headers = new HttpHeaders();

        return new ResponseEntity<>(block, headers, HttpStatus.OK);
    }


    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/parameters", method = RequestMethod.GET)
    ChainParametersDTO parameters() {
        return modelMapper.map(new ChainParameters(), ChainParametersDTO.class);
    }

    @CrossOrigin(origins = "*")
    @Transactional(readOnly = true)
    @RequestMapping(value = "/difficulties", method = RequestMethod.GET)
    DifficultiesDTO difficulties() {
        return new DifficultiesDTO(blockRepo.currentBlockNumber(), List.of(new Difficulty("", 99)));
    }

    @CrossOrigin(origins = "*")
    @Transactional(readOnly = true)
    @RequestMapping(value = "/hardship", method = RequestMethod.GET)
    HardshipDTO hardship() {
        return new HardshipDTO(blockRepo.currentBlockNumber(), 99);
    }


    @RequestMapping(value = "/branches", method = RequestMethod.GET)
    List<Block> branches() {
        return List.of();
    }

}
