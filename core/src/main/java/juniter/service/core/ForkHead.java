package juniter.service.core;


import com.google.common.collect.Lists;
import juniter.core.crypto.Crypto;
import juniter.core.crypto.SecretBox;
import juniter.core.event.*;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.index.BINDEX;
import juniter.core.model.dbo.net.EndPointType;
import juniter.core.model.dto.raw.Wrapper;
import juniter.core.model.dto.raw.WrapperBlock;
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
import java.io.PrintWriter;
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

    private SecretBox sb = new SecretBox("salt", "password");

    private ExecutorService executor;

    private Future<DBBlock> prover;

    private List<Thread> threads = new CopyOnWriteArrayList<>();


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
        block.setTime(indexForForge.getTime());
        block.setMedianTime(indexForForge.getMedianTime());

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
        block.setIssuer(sb.getPublicKey());

        indexForForge.setSize(block.getSize());

        block.setInner_hash(Crypto.hash(block.toDUP(false, false)));
        block.setSignature(sb.sign(block.toDUP(false, true)));

        return block;
    }

    private AtomicBoolean forge = new AtomicBoolean(false);

    private DBBlock parallelProver(String ccy) {
        AtomicReference<DBBlock> found = new AtomicReference<>();
        var tryHead = index.prepareIndexForForge(sb.getPublicKey());
        var newBlock = forge(tryHead);
        LOG.info("Forged : " + newBlock.toDUP());

        for (int i = 0; i < 4; i++) {
            int finalI = i;
            threads.add( new Thread(() -> {

                String hash;
                long nonce = Long.parseLong("100" + finalI + "0000000000");
                do {
                    nonce++;
                    newBlock.setNonce(nonce);
                    hash = Crypto.hash(newBlock.signedPartSigned());
                    if(hash.startsWith("0000")){
                        LOG.debug("nonce:" + nonce + ", hash:" + hash);
                    }
                    tryHead.setHash(hash);
                }while ( !index.isValid(tryHead, newBlock) && forge.get()  );
                newBlock.setNonce(nonce);
                newBlock.setHash(hash);

                if(index.isValid(tryHead, newBlock)) found.set(newBlock);
                forge.set(false);
                threads.forEach(Thread::interrupt);
            }));
        }


        try {
            forge.set(true);
            threads.parallelStream().forEach(Thread::run);
            return found.get();
        }  catch (Exception e) {
            LOG.error("Prover Exception", e);
        }
        return null;
    }

    private boolean postBlock(DBBlock block) {
        AtomicBoolean success = new AtomicBoolean(false);
        Wrapper reqBodyData = new WrapperBlock(block.toDUP(true,true) +"\n");


        Lists.newArrayList("https://duniter.moul.re","https://g1.presles.fr:443","https://g1.duniter.fr").forEach(host -> {
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
                    throw new AssertionError("post doc error, code {} " + response);
                } else {
                    LOG.info("successfully sent doc, response : {}", response);
                    success.set(true);
                }

            } catch (HttpServerErrorException http) {
                LOG.warn("error sending doc response {} " + response, http);

            } catch (ResourceAccessException ignored) {
                LOG.warn("ignored ResourceAccessException (handled as duniter ucode )", ignored);
            } catch (Exception | AssertionError e) {
                StringWriter sw = new StringWriter();
                LOG.error("ForkHead.sendBlock ", e);
            }

        });

        wsPool.getClients().forEach(wsClient -> {
            wsClient.send(reqBodyData.toString());
        });

        return success.get();
    }

    @Override
    public void onApplicationEvent(CoreEvent event) {

        if (event instanceof NewBlock) {

            var newBlockEvent = (NewBlock) event;
            var bl = newBlockEvent.getWhat();
            LOG.info("newBlockEvent " + newBlockEvent);

            if (index.head_().getNumber() < bl.getNumber()) {
                if (threads != null) {
                    LOG.info("Killing the current PoW execution");
                    forge.set(false);
                    threads.forEach(Thread::interrupt);
                }
                index.indexUntil(bl.getNumber(), false, bl.getCurrency());
            }

        } else if (event instanceof NewBINDEX) {
            var what = (BINDEX) event.getWhat();
            if (what.getNumber().equals(blockService.currentBlockNumber())) {
                LOG.info("new BINDEX, no higher block => starting proving next " + what.getCurrency() + " block ");
                DBBlock proved = parallelProver(what.getCurrency());
                if (proved != null && postBlock(proved)) {
                    LOG.info("Bitch please " + proved.getNumber() +" "+ proved.getMedianTime() + " "+ proved.getHash());
                    //System.exit(0);
                }
            }
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
        }else if(event instanceof ServerLogin){
            sb = ((ServerLogin) event).getWhat();
        } else {
            LOG.debug("unssuported event " + event);
        }
    }
}