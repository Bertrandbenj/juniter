package juniter.service;

import juniter.core.utils.TimeUtils;
import juniter.repository.jpa.BlockRepository;
import juniter.repository.memory.Index;
import juniter.service.bma.loader.BlockLoader;
import juniter.service.front.AdminFX;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@ConditionalOnExpression("${juniter.indexer:false}")
@Service
public class Indexer {

    private Logger LOG = LogManager.getLogger();

    @Autowired
    private Index index;

    @Autowired
    BlockRepository blockRepo;

    @Autowired
    BlockLoader blockLoader;



    public void indexUntil(int syncUntil ) {
        LOG.info("Testing the local repository ");
        index.reset(true, true);
        //index.load();


        final long time = System.currentTimeMillis();
        long delta = System.currentTimeMillis() - time;
        final var current = blockRepo.currentBlockNumber();
        final DecimalFormat decimalFormat = new DecimalFormat("##.###%");


        for (int i = 0; i <= syncUntil; i++) {
            final int finali = i;
            final var block = blockRepo.block(i)
                    .orElseGet(()-> blockLoader.fetchAndSaveBlock(finali));

            try{
                if (index.validate(block)) {
                    LOG.debug("Validated " + block);
                }else{
                    LOG.warn("NOT Valid " + block);
                    break;
                }
            }catch(AssertionError e){
                LOG.warn("AssertionError " + block, e);

                break;
            }

            if (i > 0 && i % 100 == 0) {
                delta = System.currentTimeMillis() - time;

                final var perBlock = delta / i;
                final var estimate = current * perBlock;
                final String perc = decimalFormat.format(1.0 * i / current);
                AdminFX.indexUpdater.setValue(1.0 * i / current);
                LOG.info(perc + ", elapsed time " + TimeUtils.format(delta) + " which is " + perBlock
                        + " ms per block validated, estimating: " + TimeUtils.format(estimate) + " total");
            }
        }

        delta = System.currentTimeMillis() - time;
        LOG.info("Finished validation, took :  " + TimeUtils.format(delta));
        index.dump();

    }

}
