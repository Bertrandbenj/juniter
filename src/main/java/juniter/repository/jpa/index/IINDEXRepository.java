package juniter.repository.jpa.index;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository to manage {@link IINDEX} instances.
 */
@Repository
public interface IINDEXRepository extends JpaRepository<IINDEX, Long> {
    @Override
    List<IINDEX> findAll();

    @Query(value = "SELECT i from IINDEX i WHERE hash = ?1 ")
    IINDEX pendingIdentityByHash(String hash);

    @Query(value = "SELECT i from IINDEX i WHERE member = TRUE AND ( uid = ?1 OR pubkey = ?2 ) ")
    IINDEX memberByUidOrPubkey(String uid, String pubkey);
}

	
