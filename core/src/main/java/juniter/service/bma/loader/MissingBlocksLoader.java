package juniter.service.bma.loader;

import juniter.core.model.dbo.BStamp;
import juniter.core.model.dbo.net.Peer;
import juniter.core.utils.TimeUtils;
import juniter.repository.jpa.net.PeersRepository;
import juniter.service.BlockService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * <pre>
 * </pre>
 *
 * @author ben
 */
@ConditionalOnExpression("${juniter.loader.useMissing:false}")
@Component
@Order(10)
public class MissingBlocksLoader {

    private static final Logger LOG = LogManager.getLogger(MissingBlocksLoader.class);


    @Autowired
    private BlockService blockService;

    @Autowired
    private BlockLoader defaultLoader;

    @Autowired
    private PeersRepository peerRepo;

    @Transactional(readOnly = true)
    private List<Integer> missingBlockNumbers() {

        final var currentNumber = peerRepo.findAll().stream()
                .map(Peer::getBlock)
                .mapToInt(BStamp::getNumber)
                .max()
                .orElse(blockService.currentBlockNumber());

        final var numbers = blockService.blockNumbers();

        if (currentNumber > blockService.currentBlockNumber()) {
            return IntStream
                    .range(0, currentNumber)
                    .boxed()
                    .filter(i -> !numbers.contains(i))
                    .collect(Collectors.toList());

        } else {
            return new ArrayList<>();
        }

    }


    @Async
    public void checkMissingBlocks() {

        if (defaultLoader.bulkLoadOn()) {
            return;
        }

        LOG.info("checkMissingBlocks ");
        final var start = System.nanoTime();


        final var missing = missingBlockNumbers();
        LOG.info("found MissingBlocks : " + missing.size() + " blocks  " + (missing.size() > 20 ? "" : missing));


        Map<Integer, Integer> map = new HashMap<>();
        int prev = -1;
        int bulkStart = -1;
        int cntI = 0;

        for (Integer miss : missing) {

            if (bulkStart == -1) {
                bulkStart = miss;
                cntI++;
            } else if (cntI >= 50 || missing.indexOf(miss) == missing.size() - 1) {
                map.put(bulkStart, cntI);
                bulkStart = miss;
                cntI = 0;
            } else if (miss != prev + 1) {
                map.put(prev, Math.max(cntI, 1));
                bulkStart = miss;
                cntI = 1;

            } else {
                cntI++;
            }
            prev = miss;

        }

        map.forEach((key, value) -> defaultLoader.put("blockchain/blocks/" + value + "/" + key));

//		missing.forEach(n -> {
//defaultLoader.fetchBlocks(entry.getValue(), entry.getKey())
//                            .forEach(b -> blockService//
//                                    .localSave(b) //
//                                    .ifPresent(bl -> LOG.debug("saved missing node " + bl))
//			if(!blackList.contains(n)){
//				LOG.info("  - doFetch for : " + n);
//				defaultLoader.fetchAndSaveBlock(n);
//			}
//
//		});

        var elapsed = Long.divideUnsigned(System.nanoTime() - start, 1000000);
        LOG.info("Elapsed time: " + TimeUtils.format(elapsed));
    }


}
