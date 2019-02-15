package juniter.service.bma.loader;

import juniter.core.model.business.net.Peer;
import juniter.core.utils.TimeUtils;
import juniter.repository.jpa.BlockRepository;
import juniter.repository.jpa.PeersRepository;
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

    private static final Logger LOG = LogManager.getLogger();


    @Autowired
    BlockRepository blockRepo;

    @Autowired
    BlockLoader defaultLoader;

    @Autowired
    private PeersRepository peerRepo;

    @Transactional(readOnly = true)
    private List<Integer> missingBlockNumbers() {

        final var currentNumber = peerRepo.findAll().stream()
                .map(Peer::getBlock)
                .mapToInt(b -> Integer.parseInt(b.split("-")[0]))
                .max()
                .orElse(blockRepo.currentBlockNumber());

        final var numbers = blockRepo.blockNumbers();

        if (currentNumber > blockRepo.currentBlockNumber()) {
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

        LOG.info("@Scheduled checkMissingBlocks ");
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
            } else if (cntI >= 50 || missing.indexOf(miss) == missing.size()-1) {
                map.put(bulkStart, cntI);
                bulkStart = miss;
                cntI = 0;
            } else if (miss != prev + 1) {
                map.put(prev, cntI);
                bulkStart = miss;
                cntI = 1;

            }else{
                cntI++;
            }
            prev = miss;

        }

        map.entrySet().forEach(entry ->
                defaultLoader.put("blockchain/blocks/"+entry.getValue()+"/"+entry.getKey()));

//		missing.forEach(n -> {
//defaultLoader.fetchBlocks(entry.getValue(), entry.getKey())
//                            .forEach(b -> blockRepo//
//                                    .localSave(b) //
//                                    .ifPresent(bl -> LOG.debug("saved missing block " + bl))
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
