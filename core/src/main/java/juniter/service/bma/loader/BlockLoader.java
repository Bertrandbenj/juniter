package juniter.service.bma.loader;

import juniter.core.event.CurrentBNUM;
import juniter.core.event.DecrementCurrent;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.net.NetStats;
import juniter.core.model.dbo.net.EndPointType;
import juniter.core.validation.BlockLocalValid;
import juniter.service.BlockService;
import juniter.service.bma.PeerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

/**
 * Controller connecting to the comma separated properties
 *
 * <pre>
 * juniter.network.trusted=node1,node2,node3
 * </pre>
 *
 * @author ben
 */
@ConditionalOnExpression("${juniter.loader.useDefault:true}") // Must be up for dependencies
@Component
@Order(1)
public class BlockLoader implements BlockLocalValid {

    private static final Logger LOG = LogManager.getLogger(BlockLoader.class);


    @Value("${bulkload:false}")
    private Boolean bulkLoadAtStart;

    private AtomicBoolean bulkLoadOn = new AtomicBoolean(false);

    @Value("${juniter.network.bulkSize:200}")
    private Integer bulkSize;

    @Value("${juniter.reset:false}")
    private Boolean reset;

    private BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<>(200);


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private BlockService blockService;

    @Autowired
    private PeerService peerService;

    public BlockLoader() {
    }

    @PostConstruct
    public void initConsumers() {


        Runnable cons = () -> {
            while (true) {

                try {
                    getBlocks().forEach(b -> blockService.localSave(b));
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    LOG.error("interrupted ", e);
                }catch (Exception e) {
                    LOG.error("Unknown exception ", e);
                }
            }
        };

        for (int i = 0; i < 10; i++) {
            new Thread(cons, "consumer" + i).start();
        }

        if(bulkLoadAtStart){
            startBulkLoad();
        }

    }

    private void queueBulkQueries() {

        final var currentNumber = fetchAndSaveBlock("current").getNumber();

        applicationEventPublisher.publishEvent(new CurrentBNUM((int) blockService.count()));

        final var nbPackage = Integer.divideUnsigned(currentNumber, bulkSize);

        IntStream.range(0, nbPackage)// get nbPackage Integers
                .map(nbb -> (nbb * bulkSize)) // with an offset of bulkSize
                .boxed()
                .sorted()
                .map(i -> "blockchain/blocks/" + bulkSize + "/" + i)
                .forEach(this::queue);

    }

    @Transactional
    private List<DBBlock> getBlocks() {

        if (bulkLoadOn.get() && blockingQueue.isEmpty()) {
            LOG.info("finished bulkload");
            bulkLoadOn.set(false);
        }

        List<DBBlock> body = null;
        String url = null;
        try {
            String path = blockingQueue.take(); // blocking here
            while (body == null || body.size() == 0) {
                try {

                    var host = peerService.nextHost(EndPointType.BMAS).get().getHost();
                    url = host + path;
                    final var responseEntity = restTemplate.exchange(url, HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<DBBlock>>() {
                            });

                    body = responseEntity.getBody();

                    assert body != null;
                    if (body.size() != bulkSize) {
                        LOG.info("Couldnt parse it all " + url);
                    }

                    final var contentType = responseEntity.getHeaders().getContentType().toString();
                    final var statusCode = responseEntity.getStatusCode().getReasonPhrase();


                    body.removeIf(block -> !checkBlockIsLocalValid(block));


                    LOG.info("getBlocks " + body.size() + " from: " + url + "... Status: " + statusCode + " : " + contentType);
                    peerService.reportSuccess(EndPointType.BMAS, host);

                    applicationEventPublisher.publishEvent(new CurrentBNUM(body.get(body.size() - 1).getNumber()));

                    return body;

                } catch (final RestClientException e) {
                    LOG.warn("fetchBlocks failed - RestClientException at " + url + " retrying .. ");

                }
            }
        } catch (final Exception e) {
            LOG.error("fetchBlocks failed at " + url + " stopping .. ");

        }

        return body;
    }


    public boolean bulkLoadOn() {
        return bulkLoadOn.get();
    }

    @Async
    public void startBulkLoad() {

        if(bulkLoadOn.compareAndExchange(false,true)){
            resetBlockinDB();
            queueBulkQueries();
        }
    }


    /**
     * Wrapper for fetchAndSaveBlock(String)
     *
     * @param number: block number
     * @return the block or null
     */
    public DBBlock fetchAndSaveBlock(Integer number) {
        return fetchAndSaveBlock("block/" + number);
    }

    /**
     * Fetch a node and save it synchronously
     *
     * @param id the node id
     */
    @Transactional
    public DBBlock fetchAndSaveBlock(String id) {
        var block = fetchBlock(id);
        LOG.info("  Saving ... : " + block.getNumber());
        return blockService.localSave(block).orElse(block);
    }


    /**
     * Fetch a node and save it synchronously
     *
     * @param id the node id
     */
    @Transactional
    private DBBlock fetchBlock(String id) {
        String url = null;
        DBBlock block = null;
        final var attempts = 0;

        while (block == null) {

            final var host = peerService.nextHost(EndPointType.BMAS).map(NetStats::getHost);
            if (host.isPresent()) {
                try {
                    url = host.get() + "blockchain/" + id;

                    block = restTemplate.getForObject(url, DBBlock.class);

                    LOG.info("  Fetched ... : " + url + " => " + block.getHash());

                } catch (Exception e) {
                    LOG.warn("Exception accessing node " + url + " " + e.getMessage());
                }
            } else {
                LOG.error("Please, connect to the internet and provide BMA configuredNodes ");
            }
        }

        return block;
    }

    /**
     * uses /blockchain/blocks/[count]/[from]
     *
     * @param bulkSize:
     * @param from;
     * @return .
     */
    @Transactional
    List<DBBlock> fetchBlocks(int bulkSize, int from) {
        List<DBBlock> body = null;
        final var blacklistHosts = new ArrayList<String>();
        String url = null;
        final var attempts = 0;
        Optional<NetStats> host;
        while (body == null && (host = peerService.nextHost(EndPointType.BMAS)).isPresent()) {

            try {
                //var host = peerService.nextHost().get();
                url = host.get().getHost() + "blockchain/blocks/" + bulkSize + "/" + from;
                final var responseEntity = restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<DBBlock>>() {
                        });

                body = responseEntity.getBody();

                assert body != null;
                if (body.size() != bulkSize) {
                    throw new Exception();
                }

                final var contentType = responseEntity.getHeaders().getContentType().toString();
                final var statusCode = responseEntity.getStatusCode().getReasonPhrase();


                body.removeIf(block -> !checkBlockIsLocalValid(block));


                LOG.info("attempts: " + attempts + " to record " + body.size() + " from: " + url + "... Status: " + statusCode + " : " + contentType);
                peerService.reportSuccess(EndPointType.BMAS, host.get().getHost());
                return body;

            } catch (final RestClientException e) {
                LOG.error("fetchBlocks failed - RestClientException at " + url + " retrying .. ");

            } catch (final Exception e) {
                LOG.error("fetchBlocks failed at " + url + " retrying .. ");

            }
        }

        LOG.error("fetchBlocks failed - Returning empty  ");

        return new ArrayList<>();
    }


    @Transactional
    @Modifying
    private void resetBlockinDB() {
        LOG.info(" === Reseting DB " + blockService.count());

        blockService.deleteAll();
        blockService.truncate();
        blockService.findAll().forEach(b -> {
            blockService.delete(b);
            //LOG.info("deleted " + b.getNumber());
            applicationEventPublisher.publishEvent(new DecrementCurrent());
        });

        LOG.info(" === Reseting DB - DONE ");

    }

    public void queue(String s) {
        try {
            blockingQueue.put(s);
        } catch (InterruptedException e) {
            LOG.error("Interrupted while queuing ",e);
        }
    }
}
