package juniter.service;


import juniter.core.crypto.Crypto;
import juniter.core.crypto.SecretBox;
import juniter.core.event.CoreEvent;
import juniter.core.event.NewBlock;
import juniter.core.event.PossibleFork;
import juniter.core.model.dbo.DBBlock;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
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

    @PostConstruct
    public void init() {
        LOG.info("Init ForkHead window: " + forkSize);


//        websockets.reconnectWebSockets();

//         index.indexUntil(blockService.currentBlockNumber(), false);
    }


    private DBBlock forge(DBBlock prev) {
        var block = new DBBlock();
        block.setNumber(prev.getNumber() + 1);
        block.setCurrency(prev.getCurrency());
        block.setPreviousHash(prev.getHash());
        block.setPreviousIssuer(prev.getPreviousIssuer());
        block.setTime(System.currentTimeMillis());
        block.setMedianTime(System.currentTimeMillis());
        block.setDividend(prev.getDividend());
        block.setCertifications(sandboxes.getPendingCertifications());
        block.setMembers(sandboxes.getPendingMemberships());
        block.setIdentities(sandboxes.getPendingIdentities());
        block.setTransactions(sandboxes.getPendingTransactions());
        block.setIssuer(sb.getPublicKey());

        // get Issuers Frame, frameVar and Count
        var issuers = index.getIssuers();
        var cnt = 0;
        var frameVar = 0;
        /*if (issuers.contains(settings.getNodeKey().getPublicKey())) {
            cnt = 1;
            frameVar++;
        }*/
        block.setIssuersFrame(index.bIndexSize());
        block.setIssuersCount(issuers.size() + cnt);
        block.setIssuersFrameVar(frameVar);


        // seal the block
        block.setInner_hash(Crypto.hash(block.toDUP(false, false)));
        //block.setSignature(settings.getNodeKey().sign(block.toDUP(false, true)));

        return block;
    }

    @Getter
    private AtomicBoolean forgeCurrent = new AtomicBoolean(true);

    private DBBlock proove(String ccy) {
        var prev = blockService.current(ccy);

        var newBlock = forge(prev);
        LOG.info("Forged " + newBlock.toDUP());
        var tryHead = index.prepareIndexForForge();
        String hash = "XXXXX";
        var nonce = 10099900011440L;
        while (forgeCurrent.get() && !index.isValid(tryHead, newBlock)) {
            newBlock.setNonce(nonce++);
            hash = Crypto.hash(newBlock.signedPartSigned());
            tryHead.setHash(hash);
        }
        newBlock.setHash(hash);

        LOG.info("Prooved "+ newBlock.toDUP());

        return newBlock;
}


    @Scheduled(fixedDelay = 30 * 1000, initialDelay = 60 * 1000)
    //@Counted(  absolute = true)
    public void tryToBuildABlockInFewMin() {

        //FIXME make it event cancelable

        blockService.existingCCYs().parallelStream().forEach(ccy -> {

            var lastBlockTime = blockService.current().orElseThrow().getMedianTime();
            var curTime = System.currentTimeMillis();
            if (lastBlockTime + 2 * 60 * 1000 > curTime) {
                proove(ccy);
            }
        });

    }

    @Override
    public void onApplicationEvent(CoreEvent event) {

        if (event instanceof NewBlock) {

            var newBlockEvent = (NewBlock) event;
            var bl = newBlockEvent.getWhat();
            LOG.info("newBlockEvent " + newBlockEvent);

            if (index.head_().getNumber() < bl.getNumber()) {
                index.indexUntil(bl.getNumber(), false, bl.getCurrency());
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