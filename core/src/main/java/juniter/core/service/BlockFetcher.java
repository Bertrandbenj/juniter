package juniter.core.service;

import juniter.core.model.dbo.DBBlock;

import java.util.List;

public interface BlockFetcher {

    void startBulkLoad();

    DBBlock fetchAndSaveBlock(Integer number);

    DBBlock fetchAndSaveBlock(String id);

    List<DBBlock> fetchAndSaveBlocks(int bulkSize, int from);

    void checkMissingBlocksAsync();

    /**
     * Queue a BMA request of the form blockchain/[query]
     * <p>
     * Async download of blocks
     *
     * @param query (blocks/100/1, block/2, currents)
     */
    void queue(String query);

    boolean bulkLoadOn();


}
