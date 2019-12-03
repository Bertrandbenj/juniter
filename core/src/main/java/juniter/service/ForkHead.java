package juniter.service;


import com.codahale.metrics.annotation.Counted;
import juniter.core.crypto.Crypto;
import juniter.core.event.CoreEvent;
import juniter.core.event.NewBlock;
import juniter.core.event.PossibleFork;
import juniter.core.model.DUPDocument;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.index.BINDEX;
import juniter.core.model.dbo.wot.Identity;
import juniter.repository.jpa.index.BINDEXRepository;
import juniter.user.UserSettings;
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
import java.util.concurrent.atomic.AtomicInteger;


@Service
@Order(1000)
@ConditionalOnExpression("${juniter.useForkHead:false}")
public class ForkHead implements ApplicationListener<CoreEvent> {


    private static final Logger LOG = LogManager.getLogger(ForkHead.class);

    @Autowired
    private Index index;

    @Autowired
    private BINDEXRepository bRepo;

    @Autowired
    private BlockService blockService;

    @Value("${juniter.forkSize:100}")
    private Integer forkSize;

    private AtomicInteger reverted = new AtomicInteger(0);

    //@Autowired
    //private UserSettings settings;


    @PostConstruct
    public void init() {
        LOG.info("Init ForkHead window: " + forkSize);


//        websockets.reconnectWebSockets();

//         index.indexUntil(blockService.currentBlockNumber(), false);
    }


    private BINDEX current() {
        return bRepo.head().orElseThrow();
    }


    private DBBlock forge(DBBlock prev, DUPDocument... docs) {
        var block = new DBBlock();
        block.setNumber(prev.getNumber() + 1);
        block.setCurrency(prev.getCurrency());
        block.setPreviousHash(prev.getHash());
        block.setPreviousIssuer(prev.getPreviousIssuer());
        block.setTime(System.currentTimeMillis());
        block.setMedianTime(System.currentTimeMillis());
        block.setDividend(prev.getDividend());
        //block.setIssuer(settings.getNodeKey().getPublicKey());

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

    private DBBlock proove(String ccy) {
        var prev = blockService.current();
        if (prev.isPresent()) {
            var newBlock = forge(prev.get(), new Identity());

            // Compute the hash
            boolean found = false;
            long nonce = 0L;
            while (!found) {
                newBlock.setNonce(nonce++);

                String hash = Crypto.hash(newBlock.signedPartSigned());
                if (hash.startsWith("0000")) {
                    LOG.info("has 4 zeros");
                    newBlock.setHash(hash);
                    found = true;
                }

            }
        }

        return null;
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

            if (current().getNumber() < bl.getNumber()) {
                index.indexUntil(bl.getNumber(), false, bl.getCurrency());
            }

        } else if (event instanceof PossibleFork) {
            var forkEv = (PossibleFork) event;
            LOG.info("forkEv " + forkEv);
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