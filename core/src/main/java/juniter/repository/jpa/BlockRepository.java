package juniter.repository.jpa;

import juniter.core.model.DBBlock;
import juniter.core.validation.BlockLocalValid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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

    @Cacheable(value="blocks")//, unless="#size()<1")
    @Query("SELECT b from DBBlock b WHERE number = ?1 ")
    Optional<DBBlock> block(Integer number);


    @Query("SELECT b from DBBlock b WHERE number = ?1 ")
    List<DBBlock> block_(Integer number);


    @Override
    List<DBBlock> findAll();


    @Cacheable(value = "blocks", key = "#number")
    @Query("SELECT b FROM DBBlock b WHERE number = ?1 AND hash = ?2 ")
    Optional<DBBlock> block(Integer number, String hash);


    @Transactional
    @Modifying
    @Query("DELETE FROM DBBlock b ")
    void truncate();


    @Query("SELECT number FROM DBBlock")
    List<Integer> blockNumbers();


    @Query("SELECT number FROM DBBlock WHERE number NOT IN :notIn")
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
    long count();


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

    @Query("SELECT c FROM DBBlock c")
    Stream<DBBlock> streamAllBlocks();

    @Query("SELECT c FROM DBBlock c WHERE number >= ?1 AND number < ?2 ")
    Stream<DBBlock> streamBlocksFromTo(int from, int to);

    default Stream<DBBlock> with(Predicate<DBBlock> predicate) {
        return streamAllBlocks()
                .filter(predicate)
                .sorted();
    }


    @Query("SELECT number FROM DBBlock c WHERE dividend IS NOT NULL ORDER BY number")
    List<Integer> withUD();

    @Query(value = "SELECT DISTINCT writtenon FROM  wot_joiners  ORDER BY writtenon ", nativeQuery=true)
    List<Integer> withNewCommers();

    @Query(value = "SELECT DISTINCT writtenon FROM  wot_revoked  ORDER BY writtenon ", nativeQuery=true)
    List<Integer> withRevoked();

    @Query(value = "SELECT DISTINCT writtenon FROM  wot_member  ORDER BY writtenon ", nativeQuery=true)
    List<Integer> withMembers();

    @Query(value = "SELECT DISTINCT writtenon FROM  wot_renewed  ORDER BY writtenon ", nativeQuery=true)
    List<Integer> withRenewed();

    @Query(value = "SELECT DISTINCT writtenon FROM  wot_leavers  ORDER BY writtenon ", nativeQuery=true)
    List<Integer> withLeavers();

    @Query(value = "SELECT DISTINCT writtenon FROM  wot_certification  ORDER BY writtenon ", nativeQuery=true)
    List<Integer> withCertifications();

    @Query(value = "SELECT DISTINCT writtenon FROM  wot_excluded  ORDER BY writtenon ", nativeQuery=true)
    List<Integer> withExcluded();

    @Query(value = "SELECT DISTINCT writtenon FROM  transaction  ORDER BY writtenon ", nativeQuery=true)
    List<Integer> withTx();


    @Query("SELECT c FROM DBBlock c WHERE number >= ?1 AND number < ?2")
    List<DBBlock> blocksFromTo(int from, int to);

}