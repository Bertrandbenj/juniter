package juniter.repository.jpa.index;

import juniter.core.model.dbo.index.SINDEX;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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

    @Query("SELECT sindex from SINDEX sindex WHERE consumed = false")
    Stream<SINDEX> sourceNotConsumed();

    @Query("SELECT sindex from SINDEX sindex WHERE identifier = ?1 AND  consumed = false")
    Stream<SINDEX> sourcesOfPubkey(String pubkey);

    @Query("SELECT sindex from SINDEX sindex WHERE identifier = ?1 AND  consumed = false")
    List<SINDEX> sourcesOfPubkeyL(String pubkey);

    @Query("SELECT sindex from SINDEX sindex WHERE conditions = ?1")
    List<SINDEX> sourcesByConditions(String condition);

    @Query("select t from SINDEX t where consumed = false AND conditions = ?1 ")
    Stream<SINDEX> transactionsOfReceiver(String pubkey);

    @Query("select t from SINDEX t where consumed = false AND identifier = ?1")
    Stream<SINDEX> transactionsOfIssuer(String pubkey);

    @Query("SELECT sindex from SINDEX sindex WHERE identifier = ?1 AND pos = ?2")
    Stream<SINDEX> sourcesByIdentifierAndPos(String identifier, Integer pos);

    @Query("SELECT sindex from SINDEX sindex WHERE written_on = ?1")
    List<SINDEX> writtenOn(String s);

    @Query("SELECT s from SINDEX s WHERE identifier LIKE CONCAT('%',?1,'%')")
    List<SINDEX> search(String search);



    @Query("SELECT sindex FROM SINDEX sindex WHERE consumed = true AND writtenOn < ?1")
    List<SINDEX>  getForTrim(Integer trimBelow);

    @Transactional
    @Modifying
    @Query("DELETE FROM SINDEX sindex WHERE identifier = ?1 AND pos = ?2 AND amount = ?3 AND base = ?4")
    void trimCreate(String identifier, Integer pos, Integer amount, Integer base);

    @Transactional
    @Modifying
    default void trim(Integer trimBelow){
        for (SINDEX s : getForTrim(trimBelow)) {
            trimCreate(s.getIdentifier(), s.getPos(), s.getAmount(), s.getBase());
        }

    }



//    @Query("SELECT conditions, SUM ( case WHEN consumed THEN ( 0 - s.amount ) ELSE s.amount end )   " +
//            "  FROM SINDEX s " +
//           // " -- where consumed = false  " +
//            " GROUP BY conditions " +
//            " HAVING  SUM ( case WHEN s.consumed THEN ( 0 - amount ) ELSE s.amount end ) < 100 " +
//            " ORDER BY conditions ")
//    Stream<Account> lowAccounts();


}

	
