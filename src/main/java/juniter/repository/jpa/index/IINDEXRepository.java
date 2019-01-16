package juniter.repository.jpa.index;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Repository to manage {@link IINDEX} instances.
 */
@Repository
public interface IINDEXRepository extends JpaRepository<IINDEX, Long> {
    @Override
    List<IINDEX> findAll();

    @Query(value = "SELECT iindex from IINDEX iindex WHERE hash = ?1 ")
    Stream<IINDEX> pendingIdentityByHash(String hash);

    @Query(value = "SELECT iindex from IINDEX iindex WHERE  uid = ?1 OR pub = ?2 ")
    Stream<IINDEX> byUidOrPubkey(String uid, String pubkey);

    @Query(value = "SELECT iindex from IINDEX iindex WHERE  uid LIKE CONCAT('%',?1,'%') OR pub LIKE CONCAT('%',?1,'%') ")
    List<IINDEX> search(String search);


    @Query(value = "SELECT iindex from IINDEX iindex WHERE  uid = ?1 ")
    Stream<IINDEX> byUid(String uid);

    @Query(value = "SELECT iindex from IINDEX iindex WHERE pub = ?1 ")
    Stream<IINDEX> idtyByPubkey(String pubkey);

    @Query(value = "SELECT iindex from IINDEX iindex WHERE pub = ?1 ")
    Optional<IINDEX> findFirstByPubLike(String pub);

    @Query(value = "SELECT iindex from IINDEX iindex WHERE written_on = ?1 ")
    List<IINDEX> writtenOn(String writtenOn);



    default Boolean idtyIsMember(String pubkey) {
        return findFirstByPubLike(pubkey).map(i -> i.member).orElse(false);
    }

    @Transactional
    @Modifying
    @Query("DELETE from IINDEX i WHERE op = 'huhu'")
    void trim(int bIndexSize);
}

	
