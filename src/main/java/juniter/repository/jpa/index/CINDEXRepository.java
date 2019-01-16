package juniter.repository.jpa.index;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Query(value = "SELECT c from CINDEX c WHERE written_on = ?1")
    List<CINDEX> writtenOn(String s);

    @Query(value = "SELECT c from CINDEX c WHERE receiver LIKE CONCAT('%',?1,'%') OR issuer LIKE CONCAT('%',?1,'%')")
    List<CINDEX> search(String search);

    @Transactional
    @Modifying
    @Query("DELETE from CINDEX c WHERE op = 'huhu'")
    void trim(int bIndexSize);
}

	
