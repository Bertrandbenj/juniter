package juniter.service.bma.loader;

import javafx.application.Platform;
import juniter.core.model.DBBlock;
import juniter.core.utils.TimeUtils;
import juniter.core.validation.BlockLocalValid;
import juniter.repository.jpa.BlockRepository;
import juniter.service.adminfx.include.Bus;
import juniter.service.bma.PeerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;
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

    private static final Logger LOG = LogManager.getLogger();


    @Value("${juniter.network.bulkSize:50}")
    private Integer bulkSize;

    @Value("${juniter.reset:false}")
    private boolean reset;

    BlockingQueue<String> blockingQueue = new LinkedBlockingDeque<>(200);

    public BlockingQueue<String> getBlockingQueue() {
        return blockingQueue;
    }

    private AtomicInteger rotator = new AtomicInteger();

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BlockRepository blockRepo;

    @Value("#{'${juniter.network.trusted}'.split(',')}")
    private List<String> configuredNodes;

    @Autowired
    private PeerService peerService;

    @PostConstruct
    public void initConsumers() {

         Runnable cons = () -> {
            while (true) {

                getBlocks().forEach(b -> blockRepo.localSave(b));

            }
        };

        for (int i = 0; i < 5; i++) {
            new Thread(cons, "consumer" + i).start();
        }

    }

    @Async
    //@Transactional
    public void bulkLoad2() {
        resetBlockinDB();
        final var currentNumber = fetchAndSaveBlock("current").getNumber();

        Platform.runLater(() -> {
            Bus.currentDBBlock.setValue(blockRepo.count());
            Bus.maxDBBlock.setValue(currentNumber);
        });


        final var nbPackage = Integer.divideUnsigned(currentNumber, bulkSize);

        IntStream.range(0, nbPackage)// get nbPackage Integers
                .map(nbb -> (nbb * bulkSize)) // with an offset of bulkSize
                .boxed()
                .sorted()
                .map(i -> "blockchain/blocks/" + bulkSize + "/" + i)
                .forEach(url -> {
                    try {
                        blockingQueue.put(url);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

    }

    @Transactional
    private List<DBBlock> getBlocks() {
        List<DBBlock> body = new ArrayList<>();
        String url = null;

        try {
            String path = blockingQueue.take(); // blocking here
            var host = peerService.nextHost().get().getHost();
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


            LOG.info(" records" + body.size() + " from: " + url + "... Status: " + statusCode + " : " + contentType);
            peerService.reportSuccess(url);
            return body;

        } catch (final RestClientException e) {
            LOG.error("fetchBlocks failed - RestClientException at " + url + " retrying .. ");

        } catch (final Exception e) {
            LOG.error("fetchBlocks failed at " + url + " retrying .. ");

        }

        return body;
    }


    public Optional<String> anyNotIn(final List<String> triedURL) {
        Collections.shuffle(configuredNodes);
        return configuredNodes.stream()
                .filter(node -> triedURL == null || !triedURL.contains(node))
                .findAny();
    }

    private boolean bulkLoadOn = false;

    public boolean bulkLoadOn() {
        return bulkLoadOn;
    }

    @Async
    @Transactional
    public void bulkLoad() {
        bulkLoadOn = true;
        if (reset) resetBlockinDB();

        final var start = System.nanoTime();
        final var currentNumber = fetchAndSaveBlock("current").getNumber();
        if (blockRepo.count() > currentNumber * 0.9) {
            LOG.warn(" = Ignore bulk loading " + blockRepo.count() + " blocks");
            bulkLoadOn = false;
            return;
        }

        LOG.info(" ======== Start BulkLoading ======== " + blockRepo.count() + " blocks");

        final var nbPackage = Integer.divideUnsigned(currentNumber, bulkSize);
        final AtomicInteger ai = new AtomicInteger(0);

        IntStream.range(0, nbPackage)// get nbPackage Integers
                .map(nbb -> (nbb * bulkSize)) // with an offset of bulkSize
                .boxed()
                .sorted()
                // .parallel() // if needed
                .map(i -> fetchBlocks(bulkSize, i)) // remote list of blocks
                .flatMap(Collection::stream) // blocks individually
                .forEach(b -> blockRepo.localSave(b));

        final var elapsed = Long.divideUnsigned(System.nanoTime() - start, 1000000);

        LOG.info("Bulkloaded " + ai.get() + " in " + TimeUtils.format(elapsed));
    }

    /**
     * Wrapper for fetchAndSaveBlock(String)
     *
     * @param number
     * @return
     */
    public DBBlock fetchAndSaveBlock(Integer number) {
        return fetchAndSaveBlock("block/" + number);
    }

    /**
     * Fetch a block and save it synchronously
     *
     * @param id the block id
     */
    @Transactional
    public DBBlock fetchAndSaveBlock(String id) {
        var block = fetchBlock(id);
        LOG.info("  Saving ... : " + block.getNumber());
        return blockRepo.localSave(block).orElse(block);
    }

    private ArrayList<String> blacklistHosts = new ArrayList<>();

    /**
     * Fetch a block and save it synchronously
     *
     * @param id the block id
     */
    @Transactional
    public DBBlock fetchBlock(String id) {
        String url = null;
        DBBlock block = null;
        final var attempts = 0;

        while (block == null) {


            final var host = anyNotIn(blacklistHosts);
            if (host.isPresent()) {
                try {
                    url = host.get() + "blockchain/" + id;
                    block = restTemplate.getForObject(url, DBBlock.class);
                    block = blockRepo.block(block.getNumber()).orElse(block);

                    LOG.info("  Fetched ... : " + id);

                } catch (Exception e) {
                    blacklistHosts.add(host.get());

                    LOG.error("Retrying : Net error accessing node " + url + " " + e.getMessage());
                }
            }


            assert blacklistHosts.size() <= configuredNodes.size() : "Please, connect to the internet and provide BMA configuredNodes ";
        }

        return block;
    }

    /**
     * uses /blockchain/blocks/[count]/[from]
     *
     * @param bulkSize:
     * @param i;
     * @return .
     */
    @Transactional
    List<DBBlock> fetchBlocks(int bulkSize, int i) {
        List<DBBlock> body = null;
        final var blacklistHosts = new ArrayList<String>();
        String url = null;
        final var attempts = 0;
        Optional<PeerService.NetStats> host;
        peerService.load();
        while (body == null && (host = peerService.nextHost()).isPresent()) {

            try {
                //var host = peerService.nextHost().get();
                url = host.get().getHost() + "blockchain/blocks/" + bulkSize + "/" + i;
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


                LOG.info("attempts: " + attempts + " records" + body.size() + " from: " + url + "... Status: " + statusCode + " : " + contentType);
                peerService.reportSuccess(host.get().getHost());
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

    public Optional<DBBlock> fetchOrRetry(String id) {
        final List<String> triedNodes = new ArrayList<>();

        Optional<String> attempt;
        while (configuredNodes.size() > triedNodes.size()) {
            attempt = anyNotIn(triedNodes);
            if (attempt.isPresent())
                return Optional.ofNullable(fetchAndSaveBlock(id));
        }
        return Optional.empty();
    }

    public String rotating() {
        final var ai = rotator.incrementAndGet() % configuredNodes.size();

        return configuredNodes.get(ai);
    }


    @Transactional
    private void resetBlockinDB() {
        LOG.info(" === Reseting DB " + blockRepo.count());

        blockRepo.deleteAll();
        blockRepo.truncate();
        blockRepo.findAll().forEach(b -> {
            blockRepo.delete(b);
            //LOG.info("deleted " + b.getNumber());
            Platform.runLater(() -> Bus.currentDBBlock.subtract(1));
        });

        LOG.info(" === Reseting DB - DONE ");

    }

}
