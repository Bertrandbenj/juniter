package juniter.service.bma;

import juniter.core.crypto.SecretBox;
import juniter.core.model.DBBlock;
import juniter.core.model.business.net.EndPoint;
import juniter.core.model.business.net.Peer;
import juniter.core.model.dto.*;
import juniter.repository.jpa.BlockRepository;
import juniter.repository.jpa.EndPointsRepository;
import juniter.repository.jpa.PeersRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * BMA peering api
 *
 * @author ben
 */
@RestController
@ConditionalOnExpression("${juniter.useBMA:false}")
@RequestMapping("/network")
@Order(100)
public class NetworkService {

    @Value("${server.port:8443}")
    private Integer port;

    @Value("${server.name:localhost}")
    private String serverName;


    public static final Logger LOG = LogManager.getLogger();

    @Autowired
    private PeersRepository peerRepo;

    @Autowired
    private EndPointsRepository endPointRepo;

    @Autowired
    private BlockRepository blockRepo;


    @Autowired
    private ModelMapper modelMapper;


    private SecretBox secretBox = new SecretBox("salt", "password");


    @Transactional
    @GetMapping("/")
    public List<String> index() {
        LOG.info("Entering /network/ ... ");
        return peerRepo.streamAllPeers()
                .flatMap(p-> p.getUris().stream())
                .map(URI::toString)
                .collect(Collectors.toList());
    }

    @CrossOrigin(origins = "*")
    @Transactional(readOnly = true)
    @GetMapping(value = "/peers")
    public PeersDTO peers() {

        LOG.info("Entering /network/peers ...");

        try (var peers = peerRepo.streamAllPeers()) {
            final var peerL = peers.map(p-> modelMapper.map(p, PeerDTO.class)).collect(Collectors.toList());
            return new PeersDTO(peerL);
        } catch (final Exception e) {
            LOG.error("NetworkService.peers() peerRepo.streamAllPeers ->  ", e);
        }
        return new PeersDTO();
    }

    @Autowired
    private RestTemplate restTemplate;


    @CrossOrigin(origins = "*")
    @Transactional(readOnly = true)
    @GetMapping(value = "/ws2p/heads")
    public WS2PHeads wsHeads() {

        LOG.info("Entering /ws2p/heads ...");
       // peerRepo.streamAllPeers().flatMap(p-> p.endpoints().stream())
        var response = restTemplate.getForObject("https://g1.duniter.fr/network/ws2p/heads",WS2PHeads.class);


        //LOG.info("  ..." + response );
return response ;
//        var res =  WS2PHeads.builder()
//                .heads(List.of(HeadDTO.builder()
//                        .message("message")
//                        .sig("====")
//                        .messageV2("message")
//                        .sigV2("====")
//                        .step(1)
//                        .build())
//                ).build();
//
//        return res;
    }


    @Transactional(readOnly = true)
    @GetMapping(value = "/peering")
    public Peer peering(HttpServletRequest request, HttpServletResponse response) {
        String remote = request.getRemoteHost();

        LOG.info("Entering /network/peering ... " + remote);

        return endPointPeer(blockRepo.currentBlockNumber());

    }

    /**
     * Create the peer card of this node
     *
     * @param number the block number we declare the node
     * @return the Peer object
     */
    public Peer endPointPeer(Integer number) {
        LOG.info("endPointPeer " + number);

        DBBlock current = blockRepo.block(number).or (()->blockRepo.current()).orElseThrow();
        var peer = new Peer();
        peer.setVersion(10);
        peer.setBlock(current.bstamp());
        peer.setCurrency("g1");
        peer.setPubkey(secretBox.getPublicKey());
        peer.setStatus("UP");

        //peer.endpoints().add(new EndPoint("BMAS " + serverName + " " + port));
        //peer.endpoints().add(new EndPoint("WS2P " + serverName + " " + port));

        whatsMyIp().ifPresent(ip -> {
            peer.endpoints().add(new EndPoint("BMAS " + ip + " " + port));
            peer.endpoints().add(new EndPoint("BASIC_MERKLED_API " + ip + " " + port));
        });

        //peer.endpoints().add(new EndPoint("BASIC_MERKLED_API " + serverName + " " + " " + port));

        peer.setSignature(secretBox.sign(peer.toDUP(false)));

        return peer;
    }

    @PostMapping(value = "/peering/peers")
    @ResponseBody
    public ResponseEntity<Peer> peeringPeersPost(@RequestBody PeerBMA input) {

        LOG.info("POSTING /network/peering/peers ..." + input.getPeer());

        Peer peer = new Peer();
        final var headers = new HttpHeaders();

        return new ResponseEntity<>(peer, headers, HttpStatus.OK);
    }


    @Transactional(readOnly = true)
    @GetMapping(value = "/peering/peers")
    public @ResponseBody
    ResponseEntity<PeeringPeersDTO> peeringPeersGet(HttpServletRequest request, HttpServletResponse response) {

        LOG.info("Entering /network/peering/peers ...");
        final var headers = new HttpHeaders();


        var extraParams = request.getParameterMap();

        var peeringPeers = new PeeringPeersDTO();
        peeringPeers.setDepth(10);
        peeringPeers.setNodeCounts(652);
        peeringPeers.setLeavesCount(648);


        var leaves = extraParams.getOrDefault("leaves", new String[]{"false"})[0];

        if (Boolean.valueOf(leaves)) {
            peeringPeers.setLeaves(new ArrayList<>());
        }

        String leaf = extraParams.getOrDefault("leaf", new String[]{""})[0];
        if (leaf.length() > 0) {
            peeringPeers.setLeaf(new LeafDTO("hash", new Peer()));
        }

        return new ResponseEntity<>(peeringPeers, headers, HttpStatus.OK);
    }


    /**
     * Auto detect IP
     *
     * @return the IP address or null
     */
    @Bean
    private Optional<String> whatsMyIp() {

        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            return Optional.of(in.readLine());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
