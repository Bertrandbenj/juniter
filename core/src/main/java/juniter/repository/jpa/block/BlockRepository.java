package juniter.repository.jpa.block;

import juniter.core.model.technical.CcyStats;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dto.node.BlockNetworkMeta;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Repository to manage {@link DBBlock} instances.
 */
@Repository
public interface BlockRepository extends JpaRepository<DBBlock, Long> {
    Logger LOG = LogManager.getLogger(BlockRepository.class);


    //@Cacheable(value = "blocks")//, unless="#getSize()<1")
    @Query("SELECT b from DBBlock b WHERE number = ?1")
    Optional<DBBlock> block(Integer number);


    @Query("SELECT b from DBBlock b WHERE number = ?1")
    List<DBBlock> block_(Integer number);


    //@Cacheable(value = "blocks", key = "#number" )
    @Query("SELECT b FROM DBBlock b WHERE number = ?1 AND hash = ?2 ")
    DBBlock block(Integer number, String hash);


    @Transactional
    @Modifying
    @Query("DELETE FROM DBBlock b ")
    void truncate();


    @Query("SELECT number FROM DBBlock")
    List<Integer> blockNumbers();


    @Query("SELECT number FROM DBBlock WHERE number NOT IN :notIn")
    List<Integer> blockNumbers(List<Integer> notIn);



    @Override
    long count();


    Stream<DBBlock> findByNumberIn(List<Integer> number);


    Stream<DBBlock> findTop10ByOrderByNumberDesc();

    // TODO clean this up
    Optional<DBBlock> findTop1ByNumber(Integer number);

    @Query("FROM DBBlock b WHERE b.currency = :ccy AND b.number = :number")
    DBBlock current(String ccy, Integer number);

    /**
     * Alias for currentStrict()
     *
     * @return Optional<DBBlock>
     */
    Optional<DBBlock> findTop1ByOrderByNumberDesc();
    // Generic function to concatenate multiple lists in Java


    default <S extends DBBlock> DBBlock override(S block) {
        final var existingBlock = findTop1ByNumber(block.getNumber());
        final var bl = existingBlock.orElse(block);
        return save(bl);
    }

//    /**
//     * Saves the given {@link DBBlock}. unsafe
//     *
//     * @param block the block to save
//     * @return the block saved
//     */
//    @Override
//    <S extends DBBlock> S save(S block) throws GenericJDBCException;

    @Query("SELECT c FROM DBBlock c")
    Stream<DBBlock> streamAllBlocks();

    @Query("SELECT c FROM DBBlock c WHERE number >= ?1 AND number < ?2 ")
    Stream<DBBlock> streamBlocksFromTo(int from, int to);


    @Query("SELECT number FROM DBBlock c WHERE dividend IS NOT NULL ORDER BY number")
    List<Integer> withUD();

    @Query("SELECT new juniter.core.model.technical.CcyStats(number, medianTime, membersCount, monetaryMass)  FROM DBBlock c WHERE dividend IS NOT NULL ORDER BY number")
    List<CcyStats> statsWithUD();

    @Query("SELECT c FROM DBBlock c WHERE number >= ?1 AND number < ?2")
    List<DBBlock> blocksFromTo(Integer from, Integer to);

    @Query("SELECT  new juniter.core.model.dto.node.BlockNetworkMeta(number, issuersFrame, issuersFrameVar, powMin, medianTime) FROM DBBlock c WHERE number >= ?1 AND number < ?2")
    List<BlockNetworkMeta> issuersFrameFromTo(int from, int to);

}