package juniter.service.bma;

import juniter.core.crypto.SecretBox;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.net.EndPoint;
import juniter.core.model.dbo.net.EndPointType;
import juniter.core.model.dbo.net.Peer;
import juniter.core.model.dto.net.*;
import juniter.repository.jpa.net.PeersRepository;
import juniter.service.BlockService;
import org.apache.http.message.BasicNameValuePair;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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


    public static final Logger LOG = LogManager.getLogger(NetworkService.class);

    @Autowired
    private PeersRepository peerRepo;


    @Autowired
    private BlockService blockService;


    @Autowired
    private ModelMapper modelMapper;


    private SecretBox secretBox = new SecretBox("salt", "password");


    @Transactional
    @GetMapping("/")
    public List<String> index() {
        LOG.info("Entering /network/ ... ");
        return peerRepo.streamAllPeers()
                .flatMap(p -> getUris(p).stream())
                .map(URI::toString)
                .collect(Collectors.toList());
    }

    public List<URI> getUris(Peer peer) {


        var uriList = new ArrayList<URI>();
        if (!"UP".equals(peer.getStatus()))
            return uriList;

        var builder = new DefaultUriBuilderFactory().builder();


        Map<String, String> apis = peer.getEndpoints().stream()
                .map(ep -> {
                    var x = "1";
                    if (ep.getApi().equals(EndPointType.WS2P))
                        x = ep.getTree();
                    else if (ep.getOther() != null)
                        x = ep.getOther();

                    return new BasicNameValuePair(ep.getApi().toString(), x); // + "=" + x;
                })
                .distinct()
                .collect(Collectors.toMap(bnvp -> bnvp.getName(), o -> o.getValue()));


        MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
        apis.forEach((k, v) -> mvm.add(k, v));


        var domains = peer.getEndpoints().stream()
                .map(ep -> {
                    var dom = ep.getDomain() != null ? ep.getDomain()
                            : ep.getIp4() != null ? ep.getIp4()
                            : ep.getIp6() != null ? ep.getIp6()
                            : "";
                    return dom + (ep.getPort() != null ? ":" + ep.getPort() : "");
                })
                .distinct()
                .collect(Collectors.toList());


        var status = "&status=" + peer.getStatus();
        //var node = "&node=" + getBlock();
        var sign = peer.getSignature();
        var pub = peer.getPubkey();


        for (String domain : domains) {

            try {
                uriList.add(builder
                        .scheme("http" + (domain.endsWith("443") || apis.containsKey("BMAS") ? "s" : ""))
                        //.setUserInfo(pubkey)//,signature)
                        .host(domain)
                        .path((domain.endsWith("/") ? "" : "/") + peer.getStatus() + "/" + peer.getBlock().getNumber() + "/" + peer.getBlock().getHash())
                        .queryParams(mvm)
                        .build());

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        var cnt1 = uriList.stream().mapToInt(u -> u.toASCIIString().length()).sum();
        var cnt2 = peer.toDUP(true).length();
        // LOG.info(" getUris " + cnt1 + "  -  " + cnt2 + "\n" + uriList.stream().map(URI::toString).collect(Collectors.joining("\n")));


        return uriList;
    }


    @CrossOrigin(origins = "*")
    @Transactional(readOnly = true)
    @GetMapping(value = "/peers")
    public PeersDTO peers() {

        LOG.info("Entering /network/peers ...");

        try (var peers = peerRepo.streamAllPeers()) {
            final var peerL = peers.map(p -> modelMapper.map(p, PeerDTO.class)).collect(Collectors.toList());
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
        var response = restTemplate.getForObject("https://g1.duniter.fr/network/ws2p/heads", WS2PHeads.class);


        //LOG.info("  ..." + response );
        return response;
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

        return endPointPeer(blockService.currentBlockNumber());

    }

    /**
     * Create the peer card of this node
     *
     * @param number the node number we declare the node
     * @return the Peer object
     */
    public Peer endPointPeer(Integer number) {
        LOG.info("endPointPeer " + number);

        DBBlock current = blockService.block(number).or(() -> blockService.current()).orElseThrow();
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
