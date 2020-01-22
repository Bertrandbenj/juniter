package juniter.service.bma.loader;

import juniter.core.event.CoreEvent;
import juniter.core.event.CurrentBNUM;
import juniter.core.event.MaxPeerBlock;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.index.BINDEX;
import juniter.core.model.dbo.net.EndPointType;
import juniter.core.model.dbo.net.NetStats;
import juniter.core.service.BlockFetcher;
import juniter.core.utils.TimeUtils;
import juniter.core.validation.BlockLocalValid;
import juniter.service.jpa.Index;
import juniter.service.jpa.JPABlockService;
import juniter.service.jpa.PeerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
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
import javax.validation.constraints.Max;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
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
@ConditionalOnExpression("${juniter.useBMA:true}") // Must be up for dependencies
@Component
@Order(1)
public class BMABlockFetcher implements BlockLocalValid, BlockFetcher, ApplicationListener<CoreEvent> {

    private static final Logger LOG = LogManager.getLogger(BMABlockFetcher.class);


    @Value("${bulkload:false}")
    private Boolean bulkLoadAtStart;

    private AtomicBoolean bulkLoadOn = new AtomicBoolean(false);

    private AtomicBoolean loadingMissing = new AtomicBoolean(false);


    @Value("${juniter.network.bulkSize:200}")
    private Integer bulkSize;

    @Value("${juniter.reset:false}")
    private Boolean reset;

    private BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<>(1000);

    @Autowired
    private RestTemplate GET;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private JPABlockService blockService;

    @Autowired
    private Index index;

    @Autowired
    private PeerService peerService;

    public BMABlockFetcher() {
    }

    @PostConstruct
    public void initConsumers() {


        Runnable cons = () -> {
            while (true) {

                try {
                    getBlocks().forEach(b -> blockService.safeSave(b));
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    LOG.error("interrupted ", e);
                } catch (Exception e) {
                    LOG.error("Unknown exception ", e);
                }
            }
        };

        for (int i = 0; i < 10; i++) {
            new Thread(cons, "consumer" + i).start();
        }

        if (bulkLoadAtStart) {
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
                    final var responseEntity = GET.exchange(url, HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<DBBlock>>() {
                            });

                    body = responseEntity.getBody();

                    assert body != null;
                    if (body.size() != bulkSize) {
                        LOG.info("Couldnt parse it all " + url);
                    }

                    final var contentType = responseEntity.getHeaders().getContentType().toString();
                    final var statusCode = responseEntity.getStatusCode().getReasonPhrase();


                    body.removeIf(block -> !silentCheck(block));


                    LOG.info("getBlocks " + body.size() + " from: " + url + "... Status: " + statusCode + " : " + contentType);
                    peerService.reportSuccess(EndPointType.BMAS, host);

                    applicationEventPublisher.publishEvent(new CurrentBNUM(body.get(body.size() - 1).getNumber()));

                    return body;

                } catch (final RestClientException e) {
                    LOG.warn("fetchAndSaveBlocks failed - RestClientException at " + url + " retrying .. ");

                } catch (final Exception e) {
                    LOG.error("fetchAndSaveBlocks failed at " + url + " retrying because ", e.getMessage());
                }
            }
        } catch (final Exception e) {
            LOG.error("fetchAndSaveBlocks failed at " + url + " stopping .. ", e);

        }

        return body;
    }

    @Override
    public boolean isBulkLoading() {
        return bulkLoadOn.get() || ! blockingQueue.isEmpty() || loadingMissing.get();
    }

    @Async
    public void startBulkLoad() {

        if (bulkLoadOn.compareAndExchange(false, true)) {
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
        return blockService.safeSave(block).orElse(block);
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

                    block = GET.getForObject(url, DBBlock.class);

                    LOG.info("  Fetched ... : " + url + " => " + block.getHash());

                } catch (Exception e) {
                    LOG.warn("Exception accessing node " + url + " " + e.getMessage());
                    peerService.reportError(EndPointType.BMAS, host.get());
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
     * @param bulkSize :
     * @param from     block number to start from (included);
     * @return .
     */
    @Override
    @Transactional
    public List<DBBlock> fetchAndSaveBlocks(@Max(500) int bulkSize, int from) {
        List<DBBlock> body = null;
        final var blacklistHosts = new ArrayList<String>();
        String url = null;
        final var attempts = 0;
        Optional<NetStats> host;
        while (body == null && (host = peerService.nextHost(EndPointType.BMAS)).isPresent()) {

            try {
                //var host = peerService.nextHost().get();
                url = host.get().getHost() + "blockchain/blocks/" + bulkSize + "/" + from;
                final var responseEntity = GET.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<DBBlock>>() {
                        });

                body = responseEntity.getBody();

                assert body != null;
                if (body.size() != bulkSize) {
                    throw new Exception();
                }

                final var contentType = responseEntity.getHeaders().getContentType().toString();
                final var statusCode = responseEntity.getStatusCode().getReasonPhrase();


                body.removeIf(block -> !silentCheck(block));


                LOG.info("attempts: " + attempts + " to record " + body.size() + " from: " + url + "... Status: " + statusCode + " : " + contentType);
                peerService.reportSuccess(EndPointType.BMAS, host.get().getHost());
                return body;

            } catch (final RestClientException e) {
                LOG.error("fetchAndSaveBlocks failed - RestClientException at " + url + " retrying .. ");

            } catch (final Exception e) {
                LOG.error("fetchAndSaveBlocks failed at " + url + " retrying .. ");

            }
        }

        LOG.error("fetchAndSaveBlocks failed - Returning empty  ");

        return new ArrayList<>();
    }


    @Transactional
    @Modifying
    private void resetBlockinDB() {

        blockService.truncate();
    }

    public void queue(String s) {
        try {
            LOG.info("Queuing "+ s);
            blockingQueue.put(s);
        } catch (InterruptedException e) {
            LOG.error("Interrupted while queuing ", e);
        }
    }


    // ============== MISSING BLOCK LOADER ==============

    /**
     * @return the block between the current index and the highest block number ever heard of
     */
    private List<Integer> missingBlockNumbers() {

        final var topPeer = Math.max(peerService.topBlock(), blockService.currentBlockNumber());
        final var topIndex = index.head().map(BINDEX::getNumber).orElse(0);

        if (topPeer > topIndex) {
            final var knownBlockInRange = blockService.blockNumbers("g1", topIndex, topPeer);

            return IntStream
                    .range(topIndex, topPeer)
                    .boxed()
                    .filter(i -> !knownBlockInRange.contains(i))
                    .collect(Collectors.toList());

        } else {
            return new ArrayList<>();
        }

    }


    @Async
    public void checkMissingBlocksAsync() {

        if (isBulkLoading()) {
            return;
        }
        loadingMissing.set(true);

        LOG.info("checkMissingBlocksAsync ");
        final var start = System.nanoTime();


        var missing = missingBlockNumbers();
        LOG.info("found MissingBlocks : " + missing.size() + " blocks  " + (missing.size() > 20 ? "" : missing));
       // missing = Lists.newArrayList(0,1,2,3);

        Map<Integer, Integer> map = new HashMap<>();
        int prev = -1;
        int bulkStart = -1;
        int cntI = 0;

        for (Integer miss : missing) {

            if (bulkStart == -1) {
                bulkStart = miss;
                cntI++;
            } else if (cntI >= bulkSize || missing.indexOf(miss) == missing.size() - 1) {
                queue("blockchain/blocks/" + cntI + "/" + bulkStart);
                //map.put(bulkStart, cntI);
                bulkStart = miss;
                cntI = 0;
            } else if (miss != prev + 1) {
                queue("blockchain/blocks/" +  Math.max(cntI, 1) + "/" + prev);
                //map.put(prev, Math.max(cntI, 1));
                bulkStart = miss;
                cntI = 1;

            } else {
                cntI++;
            }
            prev = miss;

        }

        loadingMissing.set(false);

        var elapsed = Long.divideUnsigned(System.nanoTime() - start, 1000000);
        LOG.info("Elapsed time: " + TimeUtils.format(elapsed));
    }


    @Override
    public void onApplicationEvent(CoreEvent event) {
        if (event instanceof MaxPeerBlock) {

            checkMissingBlocksAsync();
//            var maxBlockDB = blockService.currentOrTop().getNumber();
//            var maxBlockPeer = ((MaxPeerBlock) event).getWhat();
//
//            if (maxBlockDB  < maxBlockPeer) {
//                var batch = (int) Math.min(maxBlockPeer - maxBlockDB, bulkSize);
//                var from = (maxBlockDB  + 1);
//                LOG.info("Found a peering card with higher block number ... queue fetching requests " + batch + " " + from);
//                queue("blockchain/blocks/" + batch + "/" + (maxBlockDB + 1));
//            }
        }
    }
}
