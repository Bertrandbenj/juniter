package juniter.service.bma;

import juniter.core.crypto.SecretBox;
import juniter.core.model.net.EndPoint;
import juniter.core.model.net.Peer;
import juniter.repository.jpa.BlockRepository;
import juniter.repository.jpa.EndPointsRepository;
import juniter.repository.jpa.PeersRepository;
import juniter.service.bma.model.LeafDTO;
import juniter.service.bma.model.PeeringDTO;
import juniter.service.bma.model.PeersDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * BMA peering api
 *
 * @author ben
 */
@RestController
@ConditionalOnExpression("${juniter.bma.enabled:false}")
@RequestMapping("/network")
public class PeeringService {

    @Value("${server.port:8443}")
    Integer port;

    public static final Logger LOG = LogManager.getLogger();

    @Autowired
    private PeersRepository peerRepo;

    @Autowired
    private EndPointsRepository endPointRepo;

    @Autowired
    private BlockRepository blockRepo;

    private String foundIP;



    @Transactional
    @RequestMapping("/")
    public List<String> index() {
        LOG.info("Entering /network/ ... ");
        return endPointRepo.enpointsURL();
    }

    @Transactional(readOnly = true)
    @RequestMapping(value = "/peers", method = RequestMethod.GET)
    public PeersDTO peers() {

        LOG.info("Entering /network/peers ...");

        try (var peerss = peerRepo.streamAllPeers()) {
            final var peerL = peerss.collect(Collectors.toList());
            return new PeersDTO(peerL);
        } catch (final Exception e) {
            LOG.error("PeeringService.peers() peerRepo.streamAllPeers ->  ", e);
        }
        return null;
    }


    @Transactional(readOnly = true)
    @RequestMapping(value = "/peering", method = RequestMethod.GET)
    public Peer peering(HttpServletRequest request, HttpServletResponse response) {
        String remote = request.getRemoteHost();

        LOG.info("Entering /network/peering ... " + remote);
        var current = blockRepo.current().orElseThrow();

        var secretBox = new SecretBox("salt", "password");

        var peer = new Peer();
        peer.setVersion(10);
        peer.setBlock(current.getNumber() + "-" + current.getInner_hash());
        peer.setCurrency("g1");
        peer.setPubkey(secretBox.getPublicKey());
        peer.setStatus("UP");
        peer.endpoints().add(new EndPoint("BMAS " + whatsMyIp() + " " + port));
        peer.endpoints().add(new EndPoint("BASIC_MERKLED_API " + whatsMyIp() + " " + port));

        peer.setSignature(secretBox.sign(peer.toDUP(false)));

        return peer;
    }

    @RequestMapping(value = "/peering/peers", method = RequestMethod.POST)
    ResponseEntity<Peer> peeringPeersPost(HttpServletRequest request, HttpServletResponse response) {

        LOG.info("POSTING /network/peering/peers ...");

        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            LOG.info(in.lines().collect(Collectors.joining("\n")));
        }catch (Exception e ){
            LOG.error("error reading network/peering/peers inputStream ", e);
        }

        Peer peer = new Peer();
        final var headers = new HttpHeaders();


        return new ResponseEntity<>(peer, headers, HttpStatus.OK);
    }



    @Transactional(readOnly = true)
    @RequestMapping(value = "/peering/peers", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<PeeringDTO> peeringPeersGet(HttpServletRequest request, HttpServletResponse response) {

        LOG.info("Entering /network/peering/peers ...");
        final var headers = new HttpHeaders();


        var extraParams = request.getParameterMap();

        var peeringPeers = new PeeringDTO();
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


    private String whatsMyIp() {

        if(foundIP != null)
            return foundIP;

        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));

            foundIP = in.readLine(); //you get the IP as a String
            System.out.println(foundIP);
            return foundIP;
        } catch (Exception e) {
            return null;
        }
    }

}
