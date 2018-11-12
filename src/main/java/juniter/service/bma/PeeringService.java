package juniter.service.bma;

import juniter.core.crypto.SecretBox;
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

    public static final Logger LOG = LogManager.getLogger();

    @Autowired
    private PeersRepository peerRepo;

    @Autowired
    private EndPointsRepository endPointRepo;

    @Autowired
    private BlockRepository blockRepo;


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
    public Peer peering() {

        LOG.info("Entering /network/peering ...");
        var current = blockRepo.current().orElseThrow();

        var secretBox = new SecretBox("salt", "password");

        var peer = new Peer();

        peer.setBlock(current.getNumber() + "-" + current.getInner_hash());
        peer.setCurrency("g1");
        peer.setPubkey(secretBox.getPublicKey());


        return peer;
    }

    @RequestMapping(value = "/peering/peers", method = RequestMethod.POST)
    ResponseEntity<Peer> peeringPeersPost (HttpServletRequest request, HttpServletResponse response) {

        LOG.info("POSTING /network/peering/peers ...");
        String remote = request.getRemoteHost();
        Peer peer = new Peer();
        final var headers = new HttpHeaders();


        LOG.info("remote " + remote);

        return  new ResponseEntity<>(peer, headers, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    @RequestMapping(value = "/peering/peers", method = RequestMethod.GET)
    public @ResponseBody
    ResponseEntity<PeeringDTO> peeringPeers(HttpServletRequest request, HttpServletResponse response) {

        LOG.info("Entering /network/peering/peers ...");
        final var headers = new HttpHeaders();


        var  extraParams = request.getParameterMap();

        var peeringPeers = new PeeringDTO();
        peeringPeers.setDepth(10);
        peeringPeers.setNodeCounts(652);
        peeringPeers.setLeavesCount(648);


        var leaves = extraParams.getOrDefault("leaves", new String[] {"false"} )[0];

        if(Boolean.valueOf(leaves)){
            peeringPeers.setLeaves( new ArrayList<>());
        }

        var leaf = extraParams.get("leaf");
        if(leaf.length>0){
            peeringPeers.setLeaf( new LeafDTO("hash", new Peer() ) );
        }

        return new ResponseEntity<>(peeringPeers, headers, HttpStatus.OK);
    }


}
