package juniter.repository.jpa.block;

import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.tx.Transaction;
import juniter.core.model.dbo.wot.Certification;
import juniter.core.model.dbo.wot.Identity;
import juniter.core.model.dbo.wot.Member;
import juniter.core.model.dto.node.IssuersFrameDTO;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Repository to manage {@link DBBlock} instances.
 */
@Repository
public interface BlockRepository extends JpaRepository<DBBlock, Long>, BlockLocalValid {
    Logger LOG = LogManager.getLogger(BlockRepository.class);

    @Override
    void delete(DBBlock entity);

    //@Cacheable(value = "blocks")//, unless="#getSize()<1")
    @Query("SELECT b from DBBlock b WHERE number = ?1")
    Optional<DBBlock> block(Integer number);


    @Query("SELECT b from DBBlock b WHERE number = ?1")
    List<DBBlock> block_(Integer number);


    @Override
    List<DBBlock> findAll();


    //@Cacheable(value = "blocks", key = "#number" )
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
        return current().map(DBBlock::getNumber).orElse(0);
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
    // Generic function to concatenate multiple lists in Java


    default Optional<DBBlock> localSave(DBBlock block) throws AssertionError {
        //LOG.error("localsavng  "+node.getNumber());
        block.getSize();

        // Denormalized Fields !
        for (Transaction tx : block.getTransactions()) {
            tx.setWritten(block.bStamp());
            tx.getHash();
        }

        for (Identity ident : block.getIdentities()) {
            ident.setWritten(block.bStamp());
        }

        for (Certification cert : block.getCertifications()) {
            cert.setWritten(block.bStamp());
        }

        for (Member m : block.getRenewed()) {
            m.setWritten(block.bStamp());
        }

        for (Member m : block.getRevoked()) {
            m.setWritten(block.bStamp());
        }

        for (Member m : block.getJoiners()) {
            m.setWritten(block.bStamp());
        }

        for (Member m : block.getExcluded()) {
            m.setWritten(block.bStamp());
        }

        for (Member m : block.getMembers()) {
            m.setWritten(block.bStamp());
        }

        for (Member m : block.getLeavers()) {
            m.setWritten(block.bStamp());
        }


        // Do the saving after some checks
        if (checkBlockIsLocalValid(block) && block(block.getNumber(),block.getHash()).isEmpty()) {

            try {
                return Optional.of(save(block));
            } catch (Exception e) {
                LOG.error("Error localSave block " + block.getNumber(), e);
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


    @Query("SELECT c FROM DBBlock c WHERE number >= ?1 AND number < ?2")
    List<DBBlock> blocksFromTo(Integer from, Integer to);

    @Query("SELECT  new juniter.core.model.dto.node.IssuersFrameDTO(number, issuersFrame, issuersFrameVar, powMin, medianTime) FROM DBBlock c WHERE number >= ?1 AND number < ?2")
    List<IssuersFrameDTO> issuersFrameFromTo(int from, int to);

}