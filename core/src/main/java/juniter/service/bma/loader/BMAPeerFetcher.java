package juniter.service.bma.loader;

import com.fasterxml.jackson.databind.ObjectMapper;
import juniter.core.event.LogNetwork;
import juniter.core.event.MaxPeerBlock;
import juniter.core.model.dbo.BStamp;
import juniter.core.model.dbo.net.EndPointType;
import juniter.core.model.dbo.net.Peer;
import juniter.core.model.dto.net.PeerBMA;
import juniter.core.model.dto.net.PeersDTO;
import juniter.repository.jpa.net.EndPointsRepository;
import juniter.repository.jpa.net.PeersRepository;
import juniter.service.jpa.JPABlockService;
import juniter.service.jpa.PeerService;
import juniter.service.rdf.Helpers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

@ConditionalOnExpression("${juniter.useBMA:true}") // Must be up for dependencies
@Component
@Order(10)
public class BMAPeerFetcher {

    @Autowired
    private ApplicationEventPublisher coreEventBus;
    public static final Logger LOG = LogManager.getLogger(BMAPeerFetcher.class);

    @Autowired
    private RestTemplate GET;

    @Autowired
    private PeersRepository peerRepo;

    @Autowired
    private PeerService peerService;

    @Autowired
    private BMABlockFetcher blockLoader;

    @Autowired
    private JPABlockService blockService;

    @Autowired
    private EndPointsRepository endPointRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Async
    @Scheduled(fixedDelay = 2 * 60 * 1000, initialDelay = 30 * 1000)
    public void runPeerCheck() {

        LOG.info("@Scheduled runPeerCheck ");
        final var start = System.nanoTime();

        AtomicBoolean found = new AtomicBoolean(false);
        while (!found.get()) {

            final var host = peerService.nextHost(EndPointType.BMAS).orElseThrow().getHost();
            coreEventBus.publishEvent(new LogNetwork(host));

            try {
                fetchPeers(host)
                        .map(Peer::getBlock)
                        .mapToInt(BStamp::getNumber)
                        .max()
                        .ifPresent(maxBlockPeer -> {
                                    coreEventBus.publishEvent(new MaxPeerBlock(maxBlockPeer));
                                    found.set(true);
                                }
                        );


            } catch (Exception e) {
                LOG.warn("Retrying after failing on " + host + " ", e);
            }

        }

        LOG.info("@Scheduled ranPeerCheck " + Helpers.delta(System.nanoTime() - start));

    }


    @Transactional
    @Async
    public void doPairing() {

        var eps = endPointRepo.get(EndPointType.BMAS, EndPointType.BASIC_MERKLED_API);

        LOG.info("Entering doPairing " + eps.size());

        var max = eps.parallelStream()
                .map(bma -> fetchPeeringNumber(bma.url()))
                .filter(Objects::nonNull)
                .findAny()
                //.so+rted().max(Comparator.naturalOrder())
                .orElseThrow();

        LOG.info("=== Found max node " + max);

        var peer = peerService.endPointPeer(max.getNumber() - 1);
        var asBMA = new PeerBMA(peer.toDUPdoc(true));

        peerRepo.peerWithBlock(max.toString())
                .flatMap(p -> p.endpoints().stream())
                .filter(Objects::nonNull)
                .forEach(ep -> {
                    if (ep.api() == EndPointType.BMAS) {
                        var url = ep.url() + "network/peering/peers";
                        var res = technicalPost(url, asBMA);

                        LOG.info("doPairing " + url + " : " + res);

                        if (res != null) {
                            LOG.info(res.toDUPdoc(true));
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
            var peers = GET.getForObject(url, Peer.class);

            var bstamp = peers.getBlock();

            blockLoader.fetchAndSaveBlocks(100, bstamp.getNumber() - 100)
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


    private Stream<Peer> fetchPeers(String nodeURL) {

        LOG.info("fetching peers using BMA at " + nodeURL + "network/peers");
        try {


            var responseEntity = GET.exchange(nodeURL + "network/peers",
                    HttpMethod.GET, null, new ParameterizedTypeReference<PeersDTO>() {
                    });

            final var peers = responseEntity.getBody();
            final var contentType = responseEntity.getHeaders().getContentType();
            final var statusCode = responseEntity.getStatusCode();

            LOG.info("Found peers: " + peers.getPeers().size() + ", status : " + statusCode.getReasonPhrase()
                    + ", ContentType: " + contentType);


            return save(peers);

        } catch (Exception e) {
            peerService.reportError(EndPointType.BMAS, nodeURL);
        }

        return Stream.of();
        //return peers.getPeers().stream().map(b -> modelMapper.map(b, Peer.class)).collect(Collectors.toList());
    }

    private Stream<Peer> save(PeersDTO peers) {
        return peers.getPeers()
                .stream() // parsed peers
                .map(pdto -> {
                    var res = modelMapper.map(pdto, Peer.class);
                    res.setBlock(new BStamp(pdto.getBlock()));
                    return res;
                })
                //.filter(p -> p.getBlock().getNumber() > blockService.currentBlockNumber() - 500)
                .filter(p -> "UP".equals(p.getStatus()))
                //.peek(c -> LOG.info("saving " + c))
                .map(p -> {
                    try {
                        return peerRepo.saveAndFlush(p);
                    } catch (Exception e) {
                        LOG.warn("error saving peer "+ e.getMessage());
                        return null;
                    }
                });
    }


    @Transactional
    @Modifying
    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void cleanup() {
        peerRepo.cleanup(blockService.currentBlockNumber());
    }


}
