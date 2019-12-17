package juniter.service.bma.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import juniter.core.event.LogNetwork;
import juniter.core.event.MaxPeerBlock;
import juniter.core.model.dbo.BStamp;
import juniter.core.model.dbo.net.EndPointType;
import juniter.core.model.dbo.net.Peer;
import juniter.core.model.dto.net.PeerBMA;
import juniter.core.model.dto.net.PeersDTO;
import juniter.core.utils.TimeUtils;
import juniter.repository.jpa.net.EndPointsRepository;
import juniter.repository.jpa.net.PeersRepository;
import juniter.service.core.BlockService;
import juniter.service.bma.NetworkService;
import juniter.service.core.PeerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.Modifying;
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
    private ApplicationEventPublisher coreEventBus;
    public static final Logger LOG = LogManager.getLogger(PeerLoader.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PeersRepository peerRepo;

    @Autowired
    private PeerService peerService;

    @Autowired
    private BlockLoader blockLoader;

    @Autowired
    private BlockService blockService;

    @Autowired
    private EndPointsRepository endPointRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Value("${juniter.network.bulkSize:200}")
    private Integer bulkSize;

    @Async
    @Scheduled(fixedDelay =  60 * 1000, initialDelay = 60 * 1000)
    public void runPeerCheck() {

        LOG.info("@Scheduled runPeerCheck ");
        final var start = System.nanoTime();

         PeersDTO peersDTO = null;
        while (peersDTO == null || peersDTO.getPeers().isEmpty()) {

            final var host = peerService.nextHost(EndPointType.BMAS).orElseThrow().getHost();

            try {
                peersDTO = fetchPeers(host);

                var maxBlockPeer = peersDTO.getPeers().stream()
                        .map(pdto -> modelMapper.map(pdto, Peer.class))
                        .map(Peer::getBlock)
                        .mapToLong(BStamp::getNumber)
                        .max();

                var maxBlockDB = blockService.currentBlockNumber();

                coreEventBus.publishEvent(new LogNetwork(host));

                if (maxBlockPeer.isPresent()) {
                    coreEventBus.publishEvent(new MaxPeerBlock((int) maxBlockPeer.getAsLong()));

                    if (maxBlockDB < maxBlockPeer.getAsLong()) {
                        var batch = (int) Math.min(maxBlockPeer.getAsLong() - maxBlockDB, bulkSize);
                        blockLoader.fetchBlocks(batch, maxBlockDB)
                                .forEach(b -> blockService.safeSave(b));
                    }

                    final var elapsed = Long.divideUnsigned(System.nanoTime() - start, 1000000);
                    LOG.info("Max node found peers: " + maxBlockPeer +
                            "  db: " + maxBlockDB +
                            " Elapsed time: " + TimeUtils.format(elapsed));

                }  else {
                    LOG.info("Couldn't find maxPeerBlock peers: " + host + "while having received " + peersDTO);
                }

            } catch (Exception e) {
                LOG.warn("Retrying after failing on " + host + " ", e);
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

        LOG.info("=== Found max node " + max);

        var peer = peerService.endPointPeer(max.getNumber() - 1);
        var asBMA = new PeerBMA(peer.toDUP(true));

        peerRepo.peerWithBlock(max.toString())
                .flatMap(p -> p.endpoints().stream())
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

    private BStamp fetchPeeringNumber(String nodeURL) {

        var url = nodeURL + "network/peering";

        LOG.info("fetchPeering try on " + url);
        try {
            var peers = restTemplate.getForObject(url, Peer.class);

            var bstamp = peers.getBlock();

            blockLoader.fetchBlocks(100, bstamp.getNumber() - 100)
                    //.flatMap(Collection::stream) // blocks individually
                    .forEach(b -> blockService//
                            .safeSave(b) //
                            .ifPresent(bl -> LOG.debug(" saved node : " + bl)));

            return bstamp;

            //return peers;
        } catch (Exception e) {
            LOG.warn("error on " + nodeURL + "network/peering");
        }
        return null;
    }


    private PeersDTO fetchPeers(String nodeURL) {

        LOG.info("fetching peers using BMA at " + nodeURL + "network/peers");
        try {


            var responseEntity = restTemplate.exchange(nodeURL + "network/peers",
                    HttpMethod.GET, null, new ParameterizedTypeReference<PeersDTO>() {
                    });

            final var peers = responseEntity.getBody();
            final var contentType = responseEntity.getHeaders().getContentType();
            final var statusCode = responseEntity.getStatusCode();

            LOG.info("Found peers: " + peers.getPeers().size() + ", status : " + statusCode.getReasonPhrase()
                    + ", ContentType: " + contentType);

            save(peers);

            return peers;

        } catch (Exception e) {
            peerService.reportError(EndPointType.BMAS, nodeURL);
        }

        return new PeersDTO(new ArrayList<>());
        //return peers.getPeers().stream().map(b -> modelMapper.map(b, Peer.class)).collect(Collectors.toList());
    }

    private void save(PeersDTO peers) {
        peers.getPeers()
                .stream() // parsed peers
                .map(pdto -> modelMapper.map(pdto, Peer.class))
                .filter(p -> p.getBlock().getNumber() > blockService.currentBlockNumber() - 500)
                .filter(p -> "UP".equals(p.getStatus()))
//                .peek(c -> LOG.info("saving " + c))
                .forEach(p -> peerRepo.saveAndFlush(p));
    }


    @Transactional
    @Modifying
    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void cleanup() {
        peerRepo.cleanup(blockService.currentBlockNumber());
    }


}
