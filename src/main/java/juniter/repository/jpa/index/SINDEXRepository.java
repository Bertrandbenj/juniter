package juniter.repository.jpa.index;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

/**
 * Repository to manage {@link SINDEX} instances.
 */
@Repository
public interface SINDEXRepository extends JpaRepository<SINDEX, Long> {
    @Override
    List<SINDEX> findAll();

    @Query(value = "SELECT sindex from SINDEX sindex WHERE identifier = ?1")
    Stream<SINDEX> sourcesOfPubkey(String pubkey);

    @Query(value = "SELECT sindex from SINDEX sindex WHERE conditions = ?1")
    Stream<SINDEX> sourcesByConditions(String condition);

    @Query("select t from SINDEX t where consumed = false AND identifier = ?1 ")
    Stream<SINDEX> transactionsOfReceiver(String pubkey);

    @Query("select t from SINDEX t where consumed = false AND identifier = ?1")
    Stream<SINDEX> transactionsOfIssuer(String pubkey);
}

	
