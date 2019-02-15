package juniter.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import juniter.core.model.DBBlock;
import juniter.repository.jpa.BlockRepository;
import juniter.service.bma.loader.BlockLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Service
public class CachedBlock {

    @Autowired
    private BlockRepository blockRepo;

    @Autowired
    private BlockLoader blockLoader;

    private DBBlock current;


    private LoadingCache<Integer, DBBlock> cache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(new CacheLoader<>() {
                public DBBlock load(Integer key) {
                    return blockRepo.block(key).orElseGet(()->blockLoader.fetchAndSaveBlock(key));
                }
            });




    DBBlock cachedBlock(Integer bstamp) {

        try {
            return cache.get(bstamp);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    public DBBlock getCurrent() {
        if (current == null)
            current = blockRepo.current().orElse(  blockLoader.fetchAndSaveBlock("/current"));
        return current;
    }

    Integer currentBlockNumber() {

        return blockRepo.currentBlockNumber();
    }


    public Optional<DBBlock> block(int i) {
        return Optional.of(cachedBlock(i));
    }
}
