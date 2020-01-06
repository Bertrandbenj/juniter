package juniter.repository.jpa.index;

import juniter.core.model.dbo.index.CINDEX;
import juniter.core.model.dbo.index.SINDEX;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Repository to manage {@link CINDEX} instances.
 */
@Repository
public interface CINDEXRepository extends JpaRepository<CINDEX, Long> {
    Logger LOG = LogManager.getLogger(CINDEXRepository.class);

    @Transactional(readOnly = true)
    @Query("FROM CINDEX")
    List<CINDEX> all();

    @Query("select DISTINCT c from CINDEX c where c.receiver IN (:pub)  ")
    List<String> receveiverDistinctIn(List<String> pub);

    default List<String> distance(String knownKey) {
        LOG.info("entering distance  " + knownKey);
        var knownPubkey = new ArrayList<String>();
        knownPubkey.add(knownKey);

        knownPubkey.addAll(receveiverDistinctIn(knownPubkey));
        LOG.info("Step 1  " + knownPubkey.size());

        knownPubkey.addAll(receveiverDistinctIn(knownPubkey));
        LOG.info("Step 2  " + knownPubkey.size());

        knownPubkey.addAll(receveiverDistinctIn(knownPubkey));
        LOG.info("Step 3  " + knownPubkey.size());

        knownPubkey.addAll(receveiverDistinctIn(knownPubkey));
        LOG.info("Step 4  " + knownPubkey.size());

        knownPubkey.addAll(receveiverDistinctIn(knownPubkey));
        LOG.info("Step 5  " + knownPubkey.size());

        //knownPubkey.addAll(receveiverDistinctIn(knownPubkey));
        LOG.info("Step  6 " + knownPubkey.size());
        return knownPubkey;
    }


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

	
