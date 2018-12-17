package juniter.repository.jpa.index;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

/**
 * Repository to manage {@link CINDEX} instances.
 */
@Repository
public interface CINDEXRepository extends JpaRepository<CINDEX, Long> {

    @Query("select t from CINDEX t WHERE receiver = ?1")
    Stream<CINDEX> receivedBy(String pubkey);

    @Query("select t from CINDEX t WHERE issuer = ?1")
    Stream<CINDEX> issuedBy(String pubkey);
}

	
