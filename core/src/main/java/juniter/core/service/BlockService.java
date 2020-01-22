package juniter.core.service;

import juniter.core.model.dbo.ChainParameters;
import juniter.core.validation.BlockLocalValid;

import java.util.List;

public interface BlockService extends BlockLocalValid {

    ChainParameters paramsByCCY(String ccy);

    Integer currentBlockNumber();

    void deleteAt(String ccy, Integer block);

    List<Integer> blockNumbers(String currency, int rangeStart, int rangeEnd);

    void truncate();

    default void checkMissingBlocksAsync() {
        getBlockFetchers().forEach(BlockFetcher::checkMissingBlocksAsync);
    }

    List<BlockFetcher> getBlockFetchers();

    default boolean isBulkLoading(){
        return  getBlockFetchers().stream().anyMatch(BlockFetcher::isBulkLoading);
    }
}
