package juniter.service.core;


import com.google.common.collect.Lists;
import io.micrometer.core.annotation.Timed;
import juniter.core.crypto.Crypto;
import juniter.core.crypto.SecretBox;
import juniter.core.event.*;
import juniter.core.model.dbo.ChainParameters;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.index.BINDEX;
import juniter.core.model.dto.raw.WrapperBlock;
import juniter.core.model.wso.Wrapper;
import juniter.service.ws2p.WebSocketPool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


@Service
@Order(1000)
@ConditionalOnExpression("${juniter.useForkHead:false}")
public class ForkHead implements ApplicationListener<CoreEvent> {


    private static final Logger LOG = LogManager.getLogger(ForkHead.class);

    @Autowired
    private Index index;

    @Autowired
    private BlockService blockService;

    @Autowired
    private PeerService peerService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebSocketPool wsPool;

    @Value("${juniter.forkSize:100}")
    private Integer forkSize;

    private AtomicInteger reverted = new AtomicInteger(0);

    @Autowired
    private Sandboxes sandboxes;

    @Autowired
    private SecretBox serverSecret;

    private ExecutorService executor;

    private Future<DBBlock> prover;

    private List<Thread> threads = new CopyOnWriteArrayList<>();

    private ChainParameters conf = new ChainParameters();

    @PostConstruct
    public void init() {
        executor = new ThreadPoolExecutor(2, 4, 60,
                TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        LOG.info("Init ForkHead window: " + forkSize);

    }


    private DBBlock forge(BINDEX indexForForge) {
        var block = new DBBlock();
        block.setNumber(indexForForge.getNumber());
        block.setVersion(indexForForge.getVersion().shortValue());
        block.setMonetaryMass(indexForForge.getMass());
        block.setMembersCount(indexForForge.getMembersCount());
        block.setCurrency(indexForForge.getCurrency());
        block.setPreviousHash(indexForForge.getPreviousHash());
        block.setPreviousIssuer(indexForForge.getPreviousIssuer());
        block.setMedianTime(indexForForge.getMedianTime());

        block.setTime((long) (indexForForge.getMedianTime()+(conf.maxAcceleration()/2)));

        block.setDividend(null); // FIXME ?

        block.setIssuersFrame(indexForForge.getIssuersFrame());
        block.setIssuersCount(indexForForge.getIssuersCount());
        block.setIssuersFrameVar(indexForForge.getIssuersFrameVar());
        block.setPowMin(indexForForge.getPowMin());
        block.setUnitbase(indexForForge.getUnitBase());

        // FIXME complete
        block.setExcluded(new ArrayList<>());
        block.setJoiners(new ArrayList<>());
        block.setLeavers(new ArrayList<>());
        block.setRenewed(new ArrayList<>());
        block.setRevoked(new ArrayList<>());

        block.setCertifications(sandboxes.getPendingCertifications());
        block.setMembers(sandboxes.getPendingMemberships());
        block.setIdentities(sandboxes.getPendingIdentities());
        block.setTransactions(sandboxes.getPendingTransactions());
        block.setIssuer(serverSecret.getPublicKey());

        indexForForge.setSize(block.getSize());

        block.setInner_hash(Crypto.hash(block.toDUP(false, false)));

        return block;
    }

    private AtomicBoolean isForging = new AtomicBoolean(false);

    @Timed(longTask = true, histogram = true, value = "forkhead.parallelProver")
    private DBBlock parallelProver(String ccy) {
        LOG.info("entered parallelProver ");
        var found = new AtomicReference<DBBlock>();
        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            int finalI = i;
            threads.add(new Thread(() -> {
                String hash;
                var tryHead = index.prepareIndexForForge(serverSecret.getPublicKey());
                var newBlock = forge(tryHead);
                var nonce = Long.parseLong("100" + finalI + "0000000000");
                newBlock.setNonce(nonce);

                if(finalI==0){
                    LOG.info("Searching for powMin {}, powZeros {}, powRemainder {} on Forged block \n{}" ,
                            tryHead.getPowMin(), tryHead.getPowZeros(), tryHead.getPowRemainder(), newBlock.toDUP());
                }

                do {
                    nonce++;
                    newBlock.setNonce(nonce);
                    newBlock.setSignature(serverSecret.sign(newBlock.signedPart()));

                    hash = Crypto.hash(newBlock.signedPartSigned());
                    if (hash.startsWith("0000")) {
                        LOG.debug("nonce:" + nonce + ", hash:" + hash);
                    }
                    tryHead.setHash(hash);
                    newBlock.setHash(hash);

                } while (!index.isValid(tryHead, newBlock) && isForging.get());

                if (index.isValid(tryHead, newBlock)) found.set(newBlock);
                isForging.set(false);
                threads.forEach(Thread::interrupt);
            }));
        }


        try {
            isForging.set(true);
            threads.parallelStream().forEach(Thread::run);
            return found.get();
        } catch (Exception e) {
            LOG.error("Prover Exception", e);
        }
        return null;
    }

    private boolean postBlock(DBBlock block) {
        AtomicBoolean success = new AtomicBoolean(false);
        juniter.core.model.dto.raw.Wrapper reqBodyData = new WrapperBlock(block.toDUP(true, true) + "\n");


        Lists.newArrayList("https://duniter.moul.re", "https://g1.presles.fr:443", "https://g1.duniter.fr:443").forEach(host -> {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            String dest = "blockchain/block";


            //var reqURL = "https://g1.presles.fr"; // FIXME remove when fixed
            var reqURL = host + (host.endsWith("/") ? "" : "/") + dest;

            LOG.info("posting Forged Block to {}\n{}", reqURL, reqBodyData);

            var request = new HttpEntity<>(reqBodyData, headers);
            ResponseEntity response = null;

            try {

                response = restTemplate.postForEntity(reqURL, request, Object.class);

                if (response.getStatusCodeValue() != 200) {
                    throw new AssertionError("post Doc error, code {} " + response);
                } else {
                    LOG.info("successfully sent Doc, response : {}", response);
                    success.set(true);
                }

            } catch (HttpServerErrorException http) {
                LOG.warn("error sending Doc response {} " + response, http);

            } catch (ResourceAccessException ignored) {
                LOG.warn("ignored ResourceAccessException (handled as duniter ucode )", ignored);
            } catch (Exception | AssertionError e) {
                StringWriter sw = new StringWriter();
                LOG.error("ForkHead.sendBlock ", e);
            }

        });

        wsPool.getClients()
                .parallelStream()
                .forEach(wsClient -> wsClient.send(Wrapper.buildBlockDoc(block.toDUP())));

        return success.get();
    }


    @Override
    public void onApplicationEvent(CoreEvent event) {

        if (event instanceof NewBlock) {

            var newBlockEvent = (NewBlock) event;
            var bl = newBlockEvent.getWhat();
            LOG.info("newBlockEvent " + newBlockEvent);

            if (index.getBRepo().head().get().getNumber() < bl.getNumber()) {
                if (threads != null) {
                    LOG.info("Killing the current PoW execution");
                    isForging.set(false);
                    threads.forEach(Thread::interrupt);
                }
                index.indexUntil(bl.getNumber(), false, bl.getCurrency());
            }

        } else if (event instanceof NewBINDEX) {
            var what = (BINDEX) event.getWhat();

        } else if (event instanceof PossibleFork) {
            var forkEv = (PossibleFork) event;
            LOG.info("PossibleFork " + forkEv);
            var head = index.head().orElseThrow().getNumber();

            if (reverted.get() < forkSize) {
                blockService.listBlocksFromTo(head - reverted.get(), head).forEach(b -> blockService.delete(b));
                for (int i = 0; i < reverted.get(); i++)
                    index.revert1("g1");
                reverted.incrementAndGet();
            }
        } else if (event instanceof ServerLogin) {
            serverSecret = ((ServerLogin) event).getWhat();

        } else if (event instanceof Indexing) {
            if (!((Indexing) event).getWhat()) { // if we stopped indexing
                if (index.head_().getNumber().equals(blockService.currentBlockNumber())) { // and have no newer block
                    LOG.info("stopped indexing, no higher block => start proving next g1 block ");
                    DBBlock proved = parallelProver("g1");
                    if (proved != null && postBlock(proved)) {
                        LOG.info("Proved " + proved.getNumber() + " " + proved.getMedianTime() + " " + proved.getHash());
                    }else{
                        LOG.warn("Not proved or not post "+ proved);
                    }
                }
            }
        } else {
            LOG.debug("unssuported event " + event);
        }
    }
}