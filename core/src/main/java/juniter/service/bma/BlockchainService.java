package juniter.service.bma;

import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.ChainParameters;
import juniter.core.model.dbo.ChainParametersDTO;
import juniter.core.model.dbo.index.MINDEX;
import juniter.core.model.dto.node.Block;
import juniter.core.model.dto.node.WithDTO;
import juniter.core.model.dto.net.DifficultiesDTO;
import juniter.core.model.dto.net.Difficulty;
import juniter.core.model.dto.net.HardshipDTO;
import juniter.core.model.dto.wot.MembershipDTO;
import juniter.repository.jpa.block.*;
import juniter.repository.jpa.index.MINDEXRepository;
import juniter.service.BlockService;
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
 * |   |-- node
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

    private static final Logger LOG = LogManager.getLogger(BlockchainService.class);

    @Autowired
    private BlockService blockService;

    @Autowired
    private CertsRepository certRepo;

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private TxRepository txRepo;

    @Autowired
    private MINDEXRepository mRepo;

    @Autowired
    private BlockLoader defaultLoader;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    @GetMapping(value = "/all")
    public List<DBBlock> all() {

        LOG.info("Entering /blockchain/all");

        try (Stream<DBBlock> items = blockService.lastBlocks()) {
            return items.collect(toList());
        } catch (final Exception e) {
            LOG.error(e);
            return null;
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/block/{id}")
    public Block block(@PathVariable("id") Integer id) {

        LOG.info("Entering /blockchain/block/{number=" + id + "}");
        final var block = blockService
                .block(id)
                .orElseGet(() -> defaultLoader.fetchAndSaveBlock(id));

        return modelMapper.map(block, Block.class);
    }

    @CrossOrigin(origins = "*")
    @Transactional
    @GetMapping(value = "/blocks/{count}/{from}")
    public List<Block> blocks(@PathVariable("count") Integer count, @PathVariable("from") Integer from) {

        LOG.info("Entering /blockchain/blocks/{count=" + count + "}/{from=" + from + "}");

        final List<Integer> blocksToFind = IntStream.range(from, from + count).boxed().collect(toList());
        LOG.debug("---blocksToFind: " + blocksToFind);

        final List<DBBlock> knownBlocks = blockService.blocksIn(blocksToFind).collect(toList());
        LOG.debug("---known blocks: " + knownBlocks.stream().map(DBBlock::getNumber).collect(toList()));

        final List<DBBlock> blocksToSave = blocksToFind.stream()
                .filter(b -> knownBlocks.stream().noneMatch(kb -> kb.getNumber().equals(b)))
                .map(lg -> defaultLoader.fetchAndSaveBlock(lg)).collect(toList());

        LOG.debug("---fetchTrimmed blocks: " + Stream.concat(blocksToSave.stream(), knownBlocks.stream())
                .map(b -> b.getNumber().toString()).collect(joining(",")));

        blockService.saveAll(blocksToSave);

        return Stream.concat(blocksToSave.stream(), knownBlocks.stream()) //
                .map(b -> modelMapper.map(b, Block.class)) //
                .collect(toList());
    }


    @CrossOrigin(origins = "*")
    @Transactional
    @GetMapping(value = "/current")
    public Block current() {
        LOG.info("Entering /blockchain/current");
        final var b = blockService.current()
                .orElse(defaultLoader.fetchAndSaveBlock("current"));

        return modelMapper.map(b, Block.class);
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
    @GetMapping(value = "/with/{what}" ,name = "newcomers,certs,actives,revoked,leavers,excluded,ud,tx")
    @Transactional(readOnly = true)
    public WithDTO with(@PathVariable("what") String what) {

        LOG.info("Entering /blockchain/with/{newcomers,certs,actives,leavers,excluded,ud,tx}");
        Stream<DBBlock> st;
        switch (what) {
            case "newcomers":
                return new WithDTO(memberRepo.withJoiners());
            case "certs":
                return new WithDTO(certRepo.withCertifications());
            case "actives":
            case "renewed":
                return new WithDTO(memberRepo.withRenewed());
            case "revoked":
                return new WithDTO(memberRepo.withRevoked());
            case "leavers":
                return new WithDTO(memberRepo.withLeavers());
            case "excluded":
                return new WithDTO(memberRepo.withExcluded());
            case "ud":
                return new WithDTO(blockService.withUD());
            case "tx":
                return new WithDTO(txRepo.withTx());

            default:
                st = blockService.with(block -> !block.getTransactions().isEmpty());
        }

        try (Stream<Integer> items = st.map(DBBlock::getNumber)) {
            return new WithDTO(items.collect(toList()));
        } catch (final Exception e) {
            LOG.error(e);
            return null;
        }

    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/memberships/{search}")
    public Stream<MINDEX> memberships(@PathVariable("search") String search) {
        return mRepo.search(search);
    }


    @CrossOrigin(origins = "*")
    @GetMapping(value = "/parameters")
    public ChainParametersDTO parameters() {
        return modelMapper.map(new ChainParameters(), ChainParametersDTO.class);
    }

    @CrossOrigin(origins = "*")
    @Transactional(readOnly = true)
    @GetMapping(value = "/difficulties")
    public DifficultiesDTO difficulties() {
        return new DifficultiesDTO(blockService.currentBlockNumber(), List.of(new Difficulty("", 99)));
    }

    @CrossOrigin(origins = "*")
    @Transactional(readOnly = true)
    @GetMapping(value = "/hardship")
    public HardshipDTO hardship() {
        return new HardshipDTO(blockService.currentBlockNumber(), 99);
    }


    @GetMapping(value = "/branches")
    public List<Block> branches() {
        return List.of();
    }


    // ======= POST =======

    @PostMapping(value = "/membership")
    public ResponseEntity<MembershipDTO> membership(HttpServletRequest request, HttpServletResponse response) {

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


    @PostMapping(value = "/block")
    public ResponseEntity<DBBlock> block(HttpServletRequest request, HttpServletResponse response) {

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


}
