package juniter.repository.jpa.index;

import juniter.core.model.dbo.index.SINDEX;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Stream;

/**
 * Repository to manage {@link SINDEX} instances.
 */
@Repository
public interface SINDEXRepository extends JpaRepository<SINDEX, Long> {

    @Override
    void deleteAll(Iterable<? extends SINDEX> entities);

    @Override
    long count();

    @Override
    List<SINDEX> findAll();

    @Query("SELECT s FROM SINDEX s ")
    Page<SINDEX> findSome(Pageable pageable);

    @Query("SELECT sindex from SINDEX sindex WHERE consumed = false")
    Stream<SINDEX> sourceNotConsumed();

    @Query("SELECT sindex from SINDEX sindex WHERE identifier = ?1 AND  consumed = false")
    Stream<SINDEX> sourcesOfPubkey(String pubkey);

    @Query("SELECT sindex from SINDEX sindex WHERE  ( identifier = ?1 OR conditions = CONCAT('SIG(',?1,')')) AND  consumed = false")
    List<SINDEX> sourcesOfPubkeyL(String pubkey);

    @Query("SELECT sindex from SINDEX sindex WHERE conditions = ?1")
    List<SINDEX> sourcesByConditions(String condition);

    @Query("select t from SINDEX t where consumed = false AND conditions = ?1 ")
    Stream<SINDEX> transactionsOfReceiver(String pubkey);

    @Query("select t from SINDEX t where consumed = false AND identifier = ?1")
    Stream<SINDEX> transactionsOfIssuer(String pubkey);

    @Query("SELECT sindex from SINDEX sindex WHERE identifier = ?1 AND pos = ?2")
    Stream<SINDEX> sourcesByIdentifierAndPos(String identifier, Integer pos);

    @Query("SELECT sindex from SINDEX sindex WHERE  written.number = ?1 AND  written.hash = ?2 ")
    List<SINDEX> writtenOn(Integer writtenOn, String writtenHash);

    @Query("SELECT s from SINDEX s WHERE identifier LIKE CONCAT('%',?1,'%')")
    List<SINDEX> search(String search);


    @Query("SELECT sindex FROM SINDEX sindex WHERE consumed = true AND written.number < ?1")
    List<SINDEX> getForTrim(Integer trimBelow);

    @Transactional
    @Modifying
    @Query("DELETE FROM SINDEX sindex WHERE identifier = ?1 AND pos = ?2 AND amount = ?3 AND base = ?4")
    void trimCreate(String identifier, Integer pos, Integer amount, Integer base);

    @Transactional
    @Modifying
    default void trim(Integer trimBelow) {
        for (SINDEX s : getForTrim(trimBelow)) {
            trimCreate(s.getIdentifier(), s.getPos(), s.getAmount(), s.getBase());
        }

    }



}

	
