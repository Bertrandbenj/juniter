package juniter.service.bma;

import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import juniter.core.exception.DuniterException;
import juniter.core.exception.UCode;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.index.MINDEX;
import juniter.core.model.dto.ChainParametersDTO;
import juniter.core.model.dto.net.DifficultiesDTO;
import juniter.core.model.dto.net.Difficulty;
import juniter.core.model.dto.net.HardshipDTO;
import juniter.core.model.dto.Block;
import juniter.core.model.dto.node.WithDTO;
import juniter.core.model.dto.wot.MembershipDTO;
import juniter.repository.jpa.block.CertsRepository;
import juniter.repository.jpa.block.MemberRepository;
import juniter.repository.jpa.block.TxRepository;
import juniter.service.jpa.JPABlockService;
import juniter.service.jpa.Index;
import juniter.service.jpa.WebOfTrust;
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
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
 * |   `-- currentChained
 * </pre>
 *
 * @author ben
 */
@RestController
@ConditionalOnExpression("${juniter.useBMA:true}")
@RequestMapping("/blockchain")
public class BlockchainBMA {

    private static final Logger LOG = LogManager.getLogger(BlockchainBMA.class);

    @Autowired
    private JPABlockService blockService;

    @Autowired
    private CertsRepository certRepo;

    @Autowired
    private MemberRepository memberRepo;

    @Autowired
    private TxRepository txRepo;

    @Autowired
    private Index index;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private WebOfTrust wotService;

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/block/{id}", produces = "application/json")
    public Block block(@PathVariable("id") Integer id) {

        LOG.info("Entering /blockchain/block/{number=" + id + "}");
        final var block = blockService
                .blockOrFetch(id);

        return modelMapper.map(block, Block.class);
    }

    @CrossOrigin(origins = "*")
    @Transactional(readOnly = true)
    @GetMapping(value = "/blocks/{count}/{from}")
    public List<Block> blocks(@PathVariable("count") Integer count, @PathVariable("from") Integer from) {
        LOG.info("Entering /blockchain/blocks/{count=" + count + "}/{from=" + from + "}");
        final var blocksToFind = IntStream.range(from, from + count).boxed().collect(toList());
        return blockService.streamBlocksFromTo(from, from+count).map(b -> modelMapper.map(b, Block.class)).collect(toList());

    }


    @CrossOrigin(origins = "*")
    @Transactional
    @GetMapping(value = "/current")
    public Block current() {
        LOG.info("Entering /blockchain/current");
        return modelMapper.map(blockService.currentOrFetch(), Block.class);
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
     * @param what any of newcomers,certs,actives,revoked,leavers,excluded,ud,tx
     * @return A Wrapped List of Blocks
     */
    @CrossOrigin(origins = "*")
    @ApiResponse(code = 1000, message = "Api response")
    @ApiImplicitParam(example = "huhu,haha")
    @ApiParam(example = "newcomers,certs,actives,revoked,leavers,excluded,ud,tx")
    @GetMapping(value = "/with/{what}", name = "newcomers,certs,actives,revoked,leavers,excluded,ud,tx")
    @Transactional(readOnly = true)
    public WithDTO with(@PathVariable("what") String what) {
        LOG.info("Entering /blockchain/with/{newcomers,certs,actives,leavers,excluded,ud,tx}");
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
                throw new DuniterException(new UCode(5001,
                        "Parameter '" + what + "' unknown please pick one in : newcomers,certs,actives,revoked,leavers,excluded,ud,tx"
                ));
        }

    }

    @CrossOrigin(origins = "*")
    @Transactional(readOnly = true)
    @GetMapping(value = "/memberships/{search}")
    public Stream<MINDEX> memberships(@PathVariable("search") String search) {
        if (search.isBlank())
            throw new DuniterException(UCode.HTTP_PARAM_MEMBERSHIP_REQUIRED);

        return index.getMRepo().search(search);
    }


    @CrossOrigin(origins = "*")
    @GetMapping(value = "/parameters/{currency}")
    public ChainParametersDTO parameters(@PathVariable(name = "currency", required = false) Optional<String> ccy) {
        return ccy.or(() -> Optional.of("g1"))
                .map(c -> modelMapper.map(blockService.paramsByCCY(c), ChainParametersDTO.class))
                .orElseThrow(() -> new DuniterException(UCode.HTTP_PARAM_CCY_REQUIRED));
    }

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/parameters")
    @Timed(value = "blockchain_parameters")
    public ChainParametersDTO parameter() {
        return modelMapper.map(blockService.paramsByCCY("g1"), ChainParametersDTO.class);
    }


    @CrossOrigin(origins = "*")
    @Transactional(readOnly = true)
    @GetMapping(value = "/difficulties")
    public DifficultiesDTO difficulties() {
        try {
            return new DifficultiesDTO(blockService.currentBlockNumber(), List.of(new Difficulty("", 99)));
        } catch (Exception e) {
            throw new DuniterException(UCode.UNHANDLED);
        }
    }

    @CrossOrigin(origins = "*")
    @Transactional(readOnly = true)
    @GetMapping(value = "/hardship")
    public HardshipDTO hardship() {
        try {
            return new HardshipDTO(blockService.currentBlockNumber(), 99);
        } catch (Exception e) {
            throw new DuniterException(UCode.UNHANDLED);
        }
    }


    @GetMapping(value = "/branches")
    public List<Block> branches() throws DuniterException {
        throw new DuniterException(UCode.UNHANDLED);
    }


    // ======= POST =======
    @CrossOrigin(origins = "*")
    @PostMapping(value = "/membership")
    public ResponseEntity<MembershipDTO> membership(HttpServletRequest request, HttpServletResponse response) {

        LOG.info("POSTING /blockchain/membership ..." + request.getRemoteHost());

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            LOG.info(in.lines().collect(Collectors.joining("\n")));
        } catch (Exception e) {

            LOG.error("error reading blockchain/membership inputStream ", e);
            throw new DuniterException(UCode.HTTP_PARAM_MEMBERSHIP_REQUIRED);
        }

        MembershipDTO membership = new MembershipDTO();
        final var headers = new HttpHeaders();

        return new ResponseEntity<>(membership, headers, HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/block")
    public ResponseEntity<DBBlock> block(HttpServletRequest request, HttpServletResponse response) {

        LOG.info("POSTING /blockchain/block ..." + request.getRemoteHost());


        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            LOG.info(in.lines().collect(Collectors.joining("\n")));
        } catch (Exception e) {
            LOG.error("error reading blockchain/block inputStream ", e);
            throw new DuniterException(UCode.HTTP_PARAM_BLOCK_REQUIRED);

        }


        DBBlock block = new DBBlock();
        final var headers = new HttpHeaders();

        return new ResponseEntity<>(block, headers, HttpStatus.OK);
    }


}
