package juniter.service;


import juniter.core.event.CoreEvent;
import juniter.core.event.NewBlock;
import juniter.core.event.PossibleFork;
import juniter.core.model.dbo.index.BINDEX;
import juniter.repository.jpa.index.BINDEXRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
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


    @PostConstruct
    public void init() {
        LOG.info("Init ForkHead window: " + forkSize);
//        websockets.reconnectWebSockets();

//         index.indexUntil(blockService.currentBlockNumber(), false);
    }


    private BINDEX current() {
         return bRepo.head().orElseThrow();
    }

    @Override
    public void onApplicationEvent(CoreEvent event) {

        if (event instanceof NewBlock) {

            var newBlockEvent = (NewBlock) event;
            LOG.info("newBlockEvent " + newBlockEvent);

            if (current().getNumber() < newBlockEvent.getWhat().getNumber())
                index.indexUntil(newBlockEvent.getWhat().getNumber(), false);

        } else if (event instanceof PossibleFork) {
            var forkEv = (PossibleFork) event;
            LOG.info("forkEv " + forkEv);
            var head = index.head().orElseThrow().getNumber();

            if (reverted.get() < forkSize) {
                blockService.listBlocksFromTo(head - reverted.get(), head).forEach(b -> blockService.delete(b));
                for (int i = 0; i < reverted.get(); i++)
                    index.revert1();
                reverted.incrementAndGet();
            }

        } else {
            LOG.debug("unssuported event " + event);

        }
    }
}