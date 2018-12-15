package juniter.service;

import juniter.core.utils.TimeUtils;
import juniter.repository.jpa.BlockRepository;
import juniter.repository.jpa.index.Index;
import juniter.service.bma.loader.BlockLoader;
import juniter.service.adminfx.FrontPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.text.NumberFormat;

@ConditionalOnExpression("${juniter.indexer:false}")
@Service
public class Indexer {

    private Logger LOG = LogManager.getLogger();

    @Autowired
    public Index index;

    @Autowired
    BlockRepository blockRepo;

    @Autowired
    BlockLoader blockLoader;




    @Transactional
    @Async
    public void indexUntil(int syncUntil ) {

        LOG.info("Testing the local repository ");
        index.init(true);


        final long time = System.currentTimeMillis();
        long delta = System.currentTimeMillis() - time;
        final var current = blockRepo.currentBlockNumber();
        final DecimalFormat decimalFormat = new DecimalFormat("##.###%");


        for (int i = 0; i <= syncUntil; i++) {
            final int finali = i;
            final var block = blockRepo.cachedBlock(i)
                    .orElseGet(()->blockLoader.fetchAndSaveBlock(finali));

            try{
                if (index.validate(block)) {
                    LOG.debug("Validated " + block);
                }else{
                    LOG.warn("NOT Valid " + block);
                    break;
                }
            }catch(AssertionError | Exception e){
                LOG.warn("error validating block " + block, e);

                break;
            }

            if (block.getDividend() != null) {
                delta = System.currentTimeMillis() - time;

                final var perBlock = delta / i;
                final var estimate = current * perBlock;
                final String perc = decimalFormat.format(1.0 * i / current);
                FrontPage.indexUpdater.setValue(1.0 * i / current);
                LOG.info(perc + ", elapsed time " + TimeUtils.format(delta) + " which is " + perBlock
                        + " ms per block validated, estimating: " + TimeUtils.format(estimate) + " total");


                LOG.info("Memory usage" + memInfo());
            }
        }

        delta = System.currentTimeMillis() - time;
        LOG.info("Finished validation, took :  " + TimeUtils.format(delta));


    }

    public String memInfo() {
        NumberFormat format = NumberFormat.getInstance();
        StringBuilder sb = new StringBuilder();
        long maxMemory = Runtime.getRuntime().maxMemory();
        long allocatedMemory = Runtime.getRuntime().totalMemory();
        long freeMemory = Runtime.getRuntime().freeMemory();
        sb.append("Free memory: ");
        sb.append(format.format(freeMemory / 1024));
        sb.append("\n");
        sb.append("Allocated memory: ");
        sb.append(format.format(allocatedMemory / 1024));
        sb.append("\n");
        sb.append("Max memory: ");
        sb.append(format.format(maxMemory / 1024));
        sb.append("\n");
        sb.append("Total free memory: ");
        sb.append(format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024));
        sb.append("\n");
        return sb.toString();

    }

}
