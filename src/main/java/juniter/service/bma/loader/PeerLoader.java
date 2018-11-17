package juniter.service.bma.loader;

import juniter.core.model.net.Peer;
import juniter.core.utils.TimeUtils;
import juniter.repository.jpa.EndPointsRepository;
import juniter.repository.jpa.PeersRepository;
import juniter.service.bma.NetworkService;
import juniter.service.bma.model.PeerBMA;
import juniter.service.bma.model.PeersDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ConditionalOnExpression("${juniter.useDefaultLoader:true}") // Must be up for dependencies
@Component
@Order(1)
public class PeerLoader  {

    public static final Logger LOG = LogManager.getLogger();

    private RestTemplate restTpl = new RestTemplate();

    @Value("#{'${juniter.network.trusted}'.split(',')}")
    private List<String> configuredNodes;

    @Autowired
    private PeersRepository peerRepo;


    @Autowired
    private NetworkService netService;

    @Autowired
    private EndPointsRepository endPointRepo;


    private Optional<String> anyNotIn(final List<String> triedURL) {
        Collections.shuffle(configuredNodes);
        return configuredNodes.stream()
                    .filter(node -> triedURL == null || !triedURL.contains(node))
                .findAny();
    }

    @Transactional
    @Scheduled(fixedRate = 10 * 60 * 1000 )
    public void runBMAnetworkPeeringCardsCheck() {
//        endPointRepo.endpointsBMA()
//                .map(bma -> fetchPeers(bma.url()) )
//                .flatMap(peerDoc -> peerDoc.getPeers().stream())
//                .flatMap(peer-> peer.endpoints().stream())
//                .distinct();

    }


    @Scheduled(fixedRate = 1 * 60 * 1000 )
    public void runPeerCheck() {
        final var start = System.nanoTime();

        final var blacklistHosts = new ArrayList<String>();
        PeersDTO peersDTO = null;

        while (peersDTO == null) {

            final var host = anyNotIn(blacklistHosts);

            if(host.isPresent()){

                blacklistHosts.add(host.get());
                try {
                    peersDTO = fetchPeers(host.get());
                    LOG.info("fetched " + peersDTO.getPeers().size() + " peers from " +host );

                } catch (Exception e) {
                    LOG.error("Retrying : Net error accessing node " + host + " " + e.getMessage());
                }

            }else{
                LOG.error( "Please, connect to the internet and provide BMA configuredNodes ");
            }

        }

        final var elapsed = Long.divideUnsigned(System.nanoTime() - start, 1000000);
        LOG.info("Elapsed time: " + TimeUtils.format(elapsed));

    }

    @Scheduled(fixedRate = 2 * 60 * 1000 )
    public void doPairing(){
        var peer = netService.endPointPeer();
        var asBMA = new PeerBMA(peer);
        var url = "https://g1.duniter.org/network/peering/peers";

        LOG.info("Sending to " + url + "\n" + asBMA.peer);

        restTpl.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTpl.getMessageConverters().add(new StringHttpMessageConverter());

        var responseEntity = restTpl.postForObject(url, asBMA, Peer.class);
        LOG.info(responseEntity) ;
        // restTemplate.put(,entity);

    }




    public PeersDTO fetchPeers(String nodeURL) throws Exception {


            var responseEntity = restTpl.exchange(nodeURL + "/network/peers",
                    HttpMethod.GET, null, new ParameterizedTypeReference<PeersDTO>() {
                    });

            final var peers = responseEntity.getBody();
            final var contentType = responseEntity.getHeaders().getContentType();
            final var statusCode = responseEntity.getStatusCode();
            save(peers);

            LOG.info("Found peers: " + peers.getPeers().size() + ", status : " + statusCode.getReasonPhrase()
                    + ", ContentType: " + contentType.toString());



        return peers;
    }

    private void save(PeersDTO peers) {
        peers.getPeers().stream() // parsed peers
                .forEach(p -> {
                    peerRepo.saveAndFlush(p); // save the peer object
                    p.endpoints().stream() // iterate endpoints
                            .map(ep -> endPointRepo.findByPeerAndEndpoint(p, ep.getEndpoint()).orElse(ep.linkPeer(p))) // fetch
                            // existing
                            .forEach(ep -> endPointRepo.saveAndFlush(ep)); // save individual endpoints
                });
    }



}
