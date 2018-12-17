package juniter.service;

import javafx.application.Platform;
import juniter.core.utils.MemoryUtils;
import juniter.core.utils.TimeUtils;
import juniter.repository.jpa.BlockRepository;
import juniter.repository.jpa.index.Index;
import juniter.service.adminfx.FrontPage;
import juniter.service.bma.loader.BlockLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@ConditionalOnExpression("${juniter.indexer:false}")
@Service
public class Indexer {

    private Logger LOG = LogManager.getLogger();

    @Autowired public Index index;

    @Autowired
    BlockRepository blockRepo;

    @Autowired
    BlockLoader blockLoader;




    //@Transactional
    @Async
    public void indexUntil(int syncUntil ) {

        LOG.info("Testing the local repository ");
        index.init(true);


        final long time = System.currentTimeMillis();
        long delta = System.currentTimeMillis() - time;
        final var current = blockRepo.currentBlockNumber();
        Platform.runLater(() -> FrontPage.maxDBBlock.setValue(current));



        final DecimalFormat decimalFormat = new DecimalFormat("##.###%");


        for (int i = 0; i <= syncUntil; i++) {
            final int finali = i;
            Platform.runLater(() -> FrontPage.currentBindex.setValue(finali));

            final var block = blockRepo.cachedBlock(i)
                    .orElseGet(()->blockLoader.fetchAndSaveBlock(finali));

            try{
                if (index.validate(block)) {
                    LOG.debug("Validated " + block);
                    //Platform.runLater(() -> FrontPage.indexLogMessage.setValue("Validated " + block));
                }else{
                    LOG.warn("ERROR Validating " + block);
                    Platform.runLater(() -> FrontPage.indexLogMessage.setValue("ERROR Validating " + block));

                    break;
                }
            }catch(AssertionError | Exception e){
                LOG.warn("error validating block " + block, e);
                Platform.runLater(() -> FrontPage.indexLogMessage.setValue("ERROR Validating " + block));
                break;
            }

            if (block.getDividend() != null) {
                delta = System.currentTimeMillis() - time;

                final var perBlock = delta / i;
                final var estimate = current * perBlock;
                final String perc = decimalFormat.format(1.0 * i / current);

                var log = perc + ", elapsed time " + TimeUtils.format(delta) + " which is " + perBlock
                        + " ms per block validated, estimating: " + TimeUtils.format(estimate) + " total";

                LOG.info(log);
                Platform.runLater(() -> FrontPage.indexLogMessage.setValue(log));


            }
        }

        delta = System.currentTimeMillis() - time;
        LOG.info("Finished validation, took :  " + TimeUtils.format(delta));


    }



    @Scheduled(fixedRate = 60*1000)
    public void checkMemory(){
        LOG.info(MemoryUtils.memInfo());
    }


}
