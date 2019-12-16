package juniter.service;


import juniter.core.crypto.Crypto;
import juniter.core.crypto.SecretBox;
import juniter.core.event.CoreEvent;
import juniter.core.event.NewBINDEX;
import juniter.core.event.NewBlock;
import juniter.core.event.PossibleFork;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.index.BINDEX;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


@Service
@Order(1000)
@ConditionalOnExpression("${juniter.useForkHead:false}")
public class ForkHead implements ApplicationListener<CoreEvent> {


    private static final Logger LOG = LogManager.getLogger(ForkHead.class);

    @Autowired
    private Index index;

    @Autowired
    private BlockService blockService;

    @Value("${juniter.forkSize:100}")
    private Integer forkSize;

    private AtomicInteger reverted = new AtomicInteger(0);

    @Autowired
    private Sandboxes sandboxes;

    private SecretBox sb = new SecretBox("salt", "password");

    private ExecutorService executor = Executors.newFixedThreadPool(4);

    private Future<DBBlock> future;


    @PostConstruct
    public void init() {
        LOG.info("Init ForkHead window: " + forkSize);


//        websockets.reconnectWebSockets();

//         index.indexUntil(blockService.currentBlockNumber(), false);
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
        block.setDividend(indexForForge.getDividend());
        block.setIssuersFrame(indexForForge.getIssuersFrame());
        block.setIssuersCount(indexForForge.getIssuersCount());
        block.setIssuersFrameVar(indexForForge.getIssuersFrameVar());

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

    @Getter
    private AtomicBoolean forgeCurrent = new AtomicBoolean(true);

    private DBBlock prove(String ccy) {
        var tryHead = index.prepareIndexForForge(sb.getPublicKey());

        var newBlock = forge(tryHead);
        LOG.info("Forged " + newBlock.toDUP());

        String hash = "XXXXX";
        var nonce = 10099900011440L;


        while (forgeCurrent.get() && !index.isValid(tryHead, newBlock)) {
            newBlock.setNonce(nonce++);

            hash = Crypto.hash(newBlock.signedPartSigned());
            tryHead.setHash(hash);
        }
        newBlock.setHash(hash);

        LOG.info("Prooved " + newBlock.toDUP());

        return newBlock;
    }

    private DBBlock parallelProver(String ccy) {

        future = executor.submit(() -> {
            var tryHead = index.prepareIndexForForge(sb.getPublicKey());
            var newBlock = forge(tryHead);
            String hash = "XXXXX";
            var nonce = Long.parseLong("100" + Thread.currentThread().getId() + "0000000000");
            while (!Thread.currentThread().isInterrupted() && !index.isValid(tryHead, newBlock)) {
                newBlock.setNonce(nonce++);
                hash = Crypto.hash(newBlock.signedPartSigned());
                tryHead.setHash(hash);
            }
            newBlock.setHash(hash);
            return newBlock;
        });

        try {
            return future.get();
        } catch (InterruptedException e) {
            LOG.error("Prover interrupted ", e);
        } catch (ExecutionException e) {
            LOG.error("Prover ExecutionException", e);
        } catch (Exception e) {
            LOG.error("Prover Exception", e);
        }
        return null;
    }

    @Override
    public void onApplicationEvent(CoreEvent event) {

        if (event instanceof NewBlock) {

            var newBlockEvent = (NewBlock) event;
            var bl = newBlockEvent.getWhat();
            LOG.info("newBlockEvent " + newBlockEvent);

            if (index.head_().getNumber() < bl.getNumber()) {
                future.cancel(true);
                index.indexUntil(bl.getNumber(), false, bl.getCurrency());
            }

        } else if (event instanceof NewBINDEX) {
            var what = (BINDEX) event.getWhat();
            if (what.getNumber().equals(blockService.currentBlockNumber())){
                LOG.info("new BINDEX, no higher block => starting proving next "+ what.getCurrency() + "block");
                parallelProver(what.getCurrency());
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

        } else {
            LOG.debug("unssuported event " + event);

        }
    }
}