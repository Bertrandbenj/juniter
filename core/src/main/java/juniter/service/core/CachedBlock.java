package juniter.service.core;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import juniter.core.model.dbo.DBBlock;
import juniter.service.bma.loader.BlockLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class CachedBlock {

    @Autowired
    private BlockService blockService;

    @Autowired
    private BlockLoader blockLoader;

    private DBBlock current;


    private LoadingCache<Integer, DBBlock> cache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(new CacheLoader<>() {
                public DBBlock load(Integer key) {
                    return blockService.block(key).orElseGet(() -> blockLoader.fetchAndSaveBlock(key));
                }
            });


    private DBBlock cachedBlock(Integer bstamp) {

        try {
            return cache.get(bstamp);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    public DBBlock getCurrent() {
        if (current == null)
            current = blockService.current().orElse(blockLoader.fetchAndSaveBlock("/current"));
        return current;
    }

    Integer currentBlockNumber() {

        return blockService.currentBlockNumber();
    }


    public Optional<DBBlock> block(int i) {
        return Optional.of(cachedBlock(i));
    }
}
