package juniter.service.bma.loader;

import juniter.core.model.Block;
import juniter.core.utils.TimeUtils;
import juniter.core.validation.BlockLocalValid;
import juniter.repository.jpa.BlockRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
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
@ConditionalOnExpression("${juniter.useDefaultLoader:true}") // Must be up for dependencies
@Component
@Order(1)
public class BlockLoader implements CommandLineRunner, BlockLocalValid {

    private static final Logger LOG = LogManager.getLogger();

    @Value("#{'${juniter.network.trusted}'.split(',')}")
    private List<String> configuredNodes;

    @Value("${juniter.network.bulkSize:50}")
    private Integer bulkSize;

    private AtomicInteger rotator = new AtomicInteger();

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private BlockRepository blockRepo;

    public Optional<String> anyNotIn(final List<String> triedURL) {
        Collections.shuffle(configuredNodes);
        return configuredNodes.stream()
                .filter(node -> triedURL == null || !triedURL.contains(node))
                .findAny();
    }


    @Transactional
    private void bulkLoad() {

        final var start = System.nanoTime();
        final var currentNumber = fetchAndSaveBlock("current").getNumber();
        if (blockRepo.count() > currentNumber * 0.9) {
            LOG.warn(" = Ignore bulk loading " + blockRepo.count() + " blocks");
            return;
        }

        LOG.info(" ======== Start BulkLoading ======== " + blockRepo.count() + " blocks");

        final var nbPackage = Integer.divideUnsigned(currentNumber, bulkSize);
        final AtomicInteger ai = new AtomicInteger(0);

        IntStream.range(0, nbPackage)// get nbPackage Integers
                .map(nbb -> (nbb * bulkSize)) // with an offset of bulkSize
                .boxed() //
                .sorted() //
                .parallel() // parallel stream if needed
                .map(i -> fetchBlocks(bulkSize, i)) // remote list of blocks
                .flatMap(Collection::stream) // blocks individually
                .forEach(b -> blockRepo//
                        .localSave(b) //
                        .ifPresent(bl -> ai.incrementAndGet()));

        final var elapsed = Long.divideUnsigned(System.nanoTime() - start, 1000000);

        LOG.info("Bulkloaded " + ai.get() + " in " + TimeUtils.format(elapsed));
    }

    /**
     * Wrapper for fetchAndSaveBlock(String)
     *
     * @param number
     * @return
     */
    public Block fetchAndSaveBlock(Integer number) {
        return fetchAndSaveBlock("block/" + number);
    }

    /**
     * Fetch a block and save it synchronously
     *
     * @param id the block id
     */
    @Transactional
    public Block fetchAndSaveBlock(String id) {
        var block = fetchBlock(id);
        LOG.info("  Saving ... : " + block.getNumber());
        return blockRepo.localSave(block).orElse(block);
    }


    /**
     * Fetch a block and save it synchronously
     *
     * @param id the block id
     */
    @Transactional
    public Block fetchBlock(String id) {
        final var blacklistHosts = new ArrayList<String>();
        String url = null;
        Block block = null;
        final var attempts = 0;

        while (block == null) {


            final var host = anyNotIn(blacklistHosts).get();
            blacklistHosts.add(host);
            try {
                url = host + "blockchain/" + id;
                block = restTemplate.getForObject(url, Block.class);
                block = blockRepo.block(block.getNumber()).orElse(block);

                LOG.info("  Fetched ... : " + id);

            } catch (Exception e) {

                LOG.error("Retrying : Net error accessing node " + url + " " + e.getMessage());

            }

            assert blacklistHosts.size() <= configuredNodes.size(): "Please, connect to the internet and provide BMA configuredNodes ";
        }

        return block;
    }

    /**
     * uses /blockchain/blocks/[count]/[from]
     *
     * @param bulkSize
     * @param i
     * @return
     */
    @Transactional
    public List<Block> fetchBlocks(int bulkSize, int i) {
        List<Block> body = null;
        final var blacklistHosts = new ArrayList<String>();
        String url = null;
        final var attempts = 0;

        while (blacklistHosts.size() < configuredNodes.size() && body == null) {
            // Fetch & parse the blocks

            final var host = anyNotIn(blacklistHosts).get();
            blacklistHosts.add(host);
            try {
                url = host + "blockchain/blocks/" + bulkSize + "/" + i;
                final var responseEntity = restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<Block>>() {
                        });

                body = responseEntity.getBody();


                final var contentType = responseEntity.getHeaders().getContentType().toString();
                final var statusCode = responseEntity.getStatusCode().getReasonPhrase();


                body.removeIf(block -> !checkBlockisLocalValid(block));

                LOG.info(attempts + " " + body.size() + " Fetched: " + url + "... Status: " + statusCode
                        + " ContentType: " + contentType);

                return body;

            } catch (final RestClientException e) {
                LOG.error("fetchBlocks failed - RestClientException at " + url + " retrying .. ");
                continue;
            } catch (final Exception e) {
                LOG.error("fetchBlocks failed at " + url + " retrying .. ");
                continue;
            }


        }


        return null;
    }

    public Optional<Block> fetchOrRetry(String id) {
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

    @Override
    public void run(String... args) throws Exception {
        final var start = System.nanoTime();

        bulkLoad();

        final var elapsed = Long.divideUnsigned(System.nanoTime() - start, 1000000);
        LOG.info("Elapsed time: " + TimeUtils.format(elapsed));
    }


}
