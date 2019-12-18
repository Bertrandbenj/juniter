package juniter.repository.jpa.index;

import juniter.core.model.dbo.index.CINDEX;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository to manage {@link CINDEX} instances.
 */
@Repository
public interface CINDEXRepository extends JpaRepository<CINDEX, Long> {

    @Override
    void deleteAll(Iterable<? extends CINDEX> entities);

    @Override
    long count();


    @Override
    List<CINDEX> findAll();

    @Query("select cert from CINDEX cert WHERE receiver = ?1")
    List<CINDEX> receivedBy(String pubkey);

    @Query("select cert from CINDEX cert WHERE issuer = ?1")
    List<CINDEX> issuedBy(String pubkey);

    @Query("select cert from CINDEX cert WHERE issuer = ?1 AND receiver = ?2")
    List<CINDEX> getCert(String issuer, String receiver);

    @Query("SELECT cert from CINDEX cert WHERE  written.number = ?1 AND  written.hash = ?2 ")
    List<CINDEX> writtenOn(Integer writtenOn, String writtenHash);

    @Query("SELECT cert from CINDEX cert WHERE receiver LIKE CONCAT('%',?1,'%') OR issuer LIKE CONCAT('%',?1,'%')")
    List<CINDEX> search(String search);

    @Query("SELECT COUNT(receiver) from CINDEX cert WHERE issuer = ?1 AND expired_on = 0 AND expires_on >= ?2 ")
    Integer certStock(String issuer, Long asOf);

    @Transactional
    @Modifying
    @Query("DELETE from CINDEX cert WHERE op = 'pings'")
    void trim(int bIndexSize);

    @Query("SELECT cert from CINDEX cert WHERE expires_on < ?1")
    List<CINDEX> findCertsThatShouldExpire(Long mTime);

}

	
