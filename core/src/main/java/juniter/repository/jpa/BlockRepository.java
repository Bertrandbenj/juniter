package juniter.repository.jpa;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import juniter.core.model.BStamp;
import juniter.core.model.DBBlock;
import juniter.core.validation.BlockLocalValid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * Repository to manage {@link DBBlock} instances.
 */
@Repository
public interface BlockRepository extends JpaRepository<DBBlock, Long>, BlockLocalValid {
    Logger LOG = LogManager.getLogger();

    @Override
    void delete(DBBlock entity);

    default Optional<DBBlock> block(Integer number) {
        return findTop1ByNumber(number);
    }

    @Override
    List<DBBlock> findAll();

    @Query("select b from DBBlock b where number = ?1 AND hash = ?2 ")
    Optional<DBBlock> block(Integer number, String hash);


    LoadingCache<BStamp, DBBlock> cache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<>() {
                        @Autowired
                        BlockRepository blRepo;

                        public DBBlock load(BStamp key) {
                            return blRepo.block(key.getNumber(), key.getHash()).get();
                        }
                    });

    default DBBlock cachedBlock(BStamp bstamp) {

        try {
            return cache.get(bstamp, () -> block(bstamp.getNumber(), bstamp.getHash()).orElseThrow());
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional
    @Modifying
    @Query("DELETE FROM DBBlock b ")
    void truncate();

    @Query("select number from DBBlock")
    List<Integer> blockNumbers();

    @Query("select number from DBBlock WHERE number NOT IN :notIn")
    List<Integer> blockNumbers(List<Integer> notIn);

    /**
     * highest block by block number in base
     *
     * @return Optional<DBBlock>
     */
    default Optional<DBBlock> current() {
        return findTop1ByOrderByNumberDesc();
    }

    default Integer currentBlockNumber() {
        return current().map(DBBlock::getNumber).orElse(-1);
    }


    @Override
    public long count();

    Stream<DBBlock> findByNumberIn(List<Integer> number);

    Stream<DBBlock> findTop10ByOrderByNumberDesc();

    // TODO clean this up
    Optional<DBBlock> findTop1ByNumber(Integer number);

    /**
     * Alias for current()
     *
     * @return Optional<DBBlock>
     */
    Optional<DBBlock> findTop1ByOrderByNumberDesc();

    default Optional<DBBlock> localSave(DBBlock block) throws AssertionError {
        //LOG.error("localsavng  "+block.getNumber());

        if (checkBlockIsLocalValid(block)) {

            try {
                var dbblock = Optional.of(save(block));
                return dbblock;
            } catch (Exception e) {
                LOG.error("Error localSave block " + block.getNumber());
                return Optional.empty();
            }
        } else {
            LOG.error("Error localSave block " + block.getNumber());

        }


        return Optional.empty();
    }


    default <S extends DBBlock> DBBlock override(S block) {
        final var existingBlock = findTop1ByNumber(block.getNumber());
        final var bl = existingBlock.orElse(block);
        return save(bl);
    }

    /**
     * Saves the given {@link DBBlock}. unsafe
     *
     * @param block the block to save
     * @return the block saved
     */
    @Override
    <S extends DBBlock> S save(S block) throws GenericJDBCException;

    @Query("select c from DBBlock c")
    Stream<DBBlock> streamAllBlocks();

    @Query("select c from DBBlock c where number >= ?1 AND number < ?2 ")
    Stream<DBBlock> streamBlocksFromTo(int from, int to);

    default Stream<DBBlock> with(Predicate<DBBlock> predicate) {
        return streamAllBlocks()
                .filter(predicate)
                .sorted();
    }

    @Query("select c from DBBlock c where number >= ?1 AND number < ?2")
    List<DBBlock> blocksFromTo(int from, int to);

}