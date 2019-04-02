package juniter.service.bma.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import juniter.core.event.CoreEventBus;
import juniter.core.model.dbo.BStamp;
import juniter.core.model.dbo.net.EndPointType;
import juniter.core.model.dbo.net.Peer;
import juniter.core.model.dto.PeerBMA;
import juniter.core.model.dto.PeersDTO;
import juniter.core.utils.TimeUtils;
import juniter.repository.jpa.block.BlockRepository;
import juniter.repository.jpa.net.EndPointsRepository;
import juniter.repository.jpa.net.PeersRepository;
import juniter.service.bma.NetworkService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@ConditionalOnExpression("${juniter.loader.useDefault:true}") // Must be up for dependencies
@Component
@Order(10)
public class PeerLoader {

    @Autowired
    private CoreEventBus coreEventBus;
    public static final Logger LOG = LogManager.getLogger();

    @Autowired
    private RestTemplate restTpl;

    @Value("#{'${juniter.network.trusted}'.split(',')}")
    private List<String> configuredNodes;

    @Autowired
    private PeersRepository peerRepo;

    @Autowired
    private BlockLoader blockLoader;


    @Autowired
    private BlockRepository blockRepo;

    @Autowired
    private NetworkService netService;

    @Autowired
    private EndPointsRepository endPointRepo;


    @Autowired
    private ModelMapper modelMapper;
    @Value("${juniter.network.bulkSize:200}")
    private Integer bulkSize;


    private Optional<String> anyNotIn(final List<String> triedURL) {
        Collections.shuffle(configuredNodes);
        return configuredNodes.stream()
                .filter(node -> triedURL == null || !triedURL.contains(node))
                .findAny();
    }

    @Transactional
    //@Scheduled(initialDelay = 11 * 60 * 1000)
    public void runBMAnetworkPeeringCardsCheck() {
//        endPointRepo.endpointsBMAS()
//                .map(bma -> fetchPeers(bma.url()) )
//                .flatMap(peerDoc -> peerDoc.getPeers().stream())
//                .flatMap(peer-> peer.endpoints().stream())
//                .distinct();

    }


    @Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 10 * 60 * 1000)
    public void runPeerCheck() {

        LOG.info("@Scheduled runPeerCheck ");
        final var start = System.nanoTime();

        final var blacklistHosts = new ArrayList<String>();
        PeersDTO peersDTO = null;
        while (peersDTO == null) {

            final var host = anyNotIn(blacklistHosts);

            if (host.isPresent()) {

                blacklistHosts.add(host.get());
                try {
                    peersDTO = fetchPeers(host.get());
                    LOG.info("fetched " + peersDTO.getPeers().size() + " peers from " + host);

                    var maxBlockPeer = peersDTO.getPeers().stream()
                            .map(pdto -> modelMapper.map(pdto, Peer.class))
                            .map(Peer::getBlock)
                            .map(b -> b.split("-")[0])
                            .mapToLong(Long::parseLong)
                            .max();

                    var maxBlockDB = blockRepo.currentBlockNumber();

                    coreEventBus.sendEventSetMaxDBBlock(maxBlockDB);

                    coreEventBus.sendEventPeerLogMessage(host.orElse(""));


                    if (maxBlockPeer.isPresent()) {
                        coreEventBus.sendEventSetMaxPeerBlock(maxBlockPeer.getAsLong());

                        if (maxBlockDB < maxBlockPeer.getAsLong()) {
                            blockLoader.fetchBlocks(bulkSize, (int) (maxBlockPeer.getAsLong() - bulkSize))
                                    .forEach(b -> blockRepo.save(b));
                        }
                    }


                    final var elapsed = Long.divideUnsigned(System.nanoTime() - start, 1000000);
                    LOG.info("Max block found peers:" + maxBlockPeer +
                            "  db: " + maxBlockDB +
                            " Elapsed time: " + TimeUtils.format(elapsed));

                    return;

                } catch (Exception e) {
                    LOG.error("Retrying : Net error accessing node " + host + " " + e.getMessage());
                }

            } else {
                LOG.error("Please, connect to the internet and provide BMA configuredNodes ");
                System.exit(1);

            }

        }


    }

    @Transactional
    @Async
    public void doPairing() {

        LOG.info("Entering doPairing " + endPointRepo.endpointsBMAS().count());

        var max = endPointRepo.endpointsBMAS()
                .parallel()
                .map(bma -> fetchPeeringNumber(bma.url()))
                .filter(Objects::nonNull)
                .findAny()
                //.so+rted().max(Comparator.naturalOrder())
                .orElseThrow();

        LOG.info("=== Found max block " + max);

        var peer = netService.endPointPeer(max.getNumber() - 1);
        var asBMA = new PeerBMA(peer.toDUP(true));

        peerRepo.peerWithBlock(max.toString())
                .flatMap(b -> b.endpoints().stream())
                .filter(Objects::nonNull)
                .forEach(ep -> {
                    if (ep.api() == EndPointType.BMAS) {
                        var url = ep.url() + "network/peering/peers";
                        var res = technicalPost(url, asBMA);

                        LOG.info("doPairing got response : " + res);

                        if (res != null) {
                            LOG.info(res.toDUP(true));
                        }

                    }

                });

        LOG.info("Finished doPairing ");

    }

    private Peer technicalPost(String url, PeerBMA peerBMA) {

        try {
            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("Content-Type", "application/json");
            headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            LOG.info("technicalPost : " + peerBMA.getPeer());


            HttpEntity<PeerBMA> request = new HttpEntity<>(peerBMA, headers);
            var responseObject = restTemplate.postForObject(url, request, Peer.class);


            return responseObject;

        } catch (HttpClientErrorException e) {
            LOG.warn("HttpClientErrorException :  " + e.getRawStatusCode() + " " + e.getResponseBodyAsString());
            ObjectMapper mapper = new ObjectMapper();
            //ErrorHolder eh = mapper.readValue(e.getResponseBodyAsString(), ErrorHolder.class);
            //LOG.error("error:  " + eh.getErrorMessage());
        } catch (HttpServerErrorException e) {

            var status = e.getRawStatusCode();

            LOG.error("HttpServerErrorException " + status +
                    " : posting to " + url +
                    "\n" + e.getResponseBodyAsString());
        } catch (ResourceAccessException e) {
            LOG.error("retrying : error accessing " + url);
        } catch (Exception e) {
            LOG.error("error:  ", e);
        }
        return null;
    }

    public BStamp fetchPeeringNumber(String nodeURL) {

        var url = nodeURL + "network/peering";

        LOG.info("fetchPeering try on " + url);
        try {
            var peers = restTpl.getForObject(url, Peer.class);

            var bstamp = new BStamp(peers.getBlock());

            blockLoader.fetchBlocks(100, bstamp.getNumber() - 100)
                    //.flatMap(Collection::stream) // blocks individually
                    .forEach(b -> blockRepo//
                            .localSave(b) //
                            .ifPresent(bl -> LOG.debug(" saved block : " + bl)));

            return bstamp;

            //return peers;
        } catch (Exception e) {
            LOG.warn("error on " + nodeURL + "network/peering");
        }
        return null;
    }


    public PeersDTO fetchPeers(String nodeURL) {


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
                .map(pdto -> modelMapper.map(pdto, Peer.class))
                .forEach(p -> {
                    peerRepo.saveAndFlush(p); // save the peer object
                    p.endpoints().stream() // iterate endpoints
                            .map(ep -> endPointRepo.findByPeerAndEndpoint(p, ep.getEndpoint()).orElse(ep.linkPeer(p))) // fetchTrimmed
                            // existing
                            .forEach(ep -> endPointRepo.saveAndFlush(ep)); // save individual endpoints
                });
    }


}
