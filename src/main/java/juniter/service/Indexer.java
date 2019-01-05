package juniter.service;

import javafx.application.Platform;
import juniter.core.utils.TimeUtils;
import juniter.repository.jpa.BlockRepository;
import juniter.repository.jpa.index.Index;
import juniter.service.adminfx.include.Bus;
import juniter.service.bma.loader.BlockLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

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


    //@Transactional
    @Async
    public void indexUntil(int syncUntil) {

        LOG.info("Testing the local repository ");
        index.init(false);

        final long time = System.currentTimeMillis();
        long delta;

        final DecimalFormat decimalFormat = new DecimalFormat("##.###%");


        for (int i = index.head().map(h -> h.number + 1).orElse(0); i <= syncUntil; i++) {
            final int finali = i;
            Platform.runLater(() -> {
                Bus.currentBindex.setValue(finali);
                Bus.isIndexing.setValue(false);
            });

            final var block = blockRepo.cachedBlock(i)
                    .orElseGet(() -> blockLoader.fetchAndSaveBlock(finali));

            try {
                if (index.validate(block, false)) {
                    LOG.debug("Validated " + block);

                } else {
                    LOG.warn("ERROR Validating " + block);
                    Platform.runLater(() -> Bus.indexLogMessage.setValue("NOT Validated " + block));

                    break;
                }
            } catch (AssertionError | Exception e) {
                LOG.warn("error validating block " + block, e);
                Platform.runLater(() -> Bus.indexLogMessage.setValue("ERROR Validating " + block + " - " + e.getMessage()));
                break;
            }

            if (block.getDividend() != null) {
                delta = System.currentTimeMillis() - time;

                final var perBlock = delta / i;
                final var estimate = syncUntil * perBlock;
                final String perc = decimalFormat.format(1.0 * i / syncUntil);

                var log = perc + ", elapsed time " + TimeUtils.format(delta) + " which is " + perBlock
                        + " ms per block validated, estimating: " + TimeUtils.format(estimate) + " total";

                LOG.info(log);
                Platform.runLater(() -> Bus.indexLogMessage.setValue(log));


            }
        }

        Bus.isIndexing.setValue(false);
        delta = System.currentTimeMillis() - time;
        LOG.info("Finished validation, took :  " + TimeUtils.format(delta));


    }



    public void init() {
        index.init(true);
    }
}
