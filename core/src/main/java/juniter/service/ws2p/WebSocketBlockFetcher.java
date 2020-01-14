package juniter.service.ws2p;

import juniter.core.model.dbo.DBBlock;
import juniter.core.service.BlockFetcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;

import java.util.List;

@ConditionalOnExpression("${juniter.useWS2P:true}") // Must be up for dependencies
@Service
public class WebSocketBlockFetcher implements BlockFetcher {
    private static final Logger LOG = LogManager.getLogger(WebSocketBlockFetcher.class);


    @Override
    public void startBulkLoad() {
        LOG.info("startBulkLoad");
    }

    @Override
    public DBBlock fetchAndSaveBlock(Integer number) {
        return null;
    }

    @Override
    public DBBlock fetchAndSaveBlock(String id) {
        return null;
    }

    @Override
    public List<DBBlock> fetchAndSaveBlocks(int bulkSize, int from) {
        return null;
    }

    @Override
    public void checkMissingBlocksAsync() {
        LOG.info("checkMissingBlocksAsync");
    }

    @Override
    public void queue(String query) {
        LOG.info("queue");
    }

    @Override
    public boolean bulkLoadOn() {
        return false;
    }
}
