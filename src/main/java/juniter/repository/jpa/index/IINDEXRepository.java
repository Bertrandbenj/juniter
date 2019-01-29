package juniter.repository.jpa.index;

import juniter.core.model.index.IINDEX;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Repository to manage {@link IINDEX} instances.
 */
@Repository
public interface IINDEXRepository extends JpaRepository<IINDEX, Long> {
    @Override
    List<IINDEX> findAll();

    @Query(value = "SELECT iindex from IINDEX iindex WHERE hash = ?1 ")
    List<IINDEX> pendingIdentityByHash(String hash);

    @Query(value = "SELECT iindex from IINDEX iindex WHERE  uid = ?1 OR pub = ?2 ")
    List<IINDEX> byUidOrPubkey(String uid, String pubkey);

    @Query(value = "SELECT iindex from IINDEX iindex WHERE  uid LIKE CONCAT('%',?1,'%') OR pub LIKE CONCAT('%',?1,'%') ")
    List<IINDEX> search(String search);


    @Query(value = "SELECT iindex from IINDEX iindex WHERE  uid = ?1 ")
    List<IINDEX> byUid(String uid);

    @Query(value = "SELECT iindex from IINDEX iindex WHERE pub = ?1 ")
    List<IINDEX> idtyByPubkey(String pubkey);

    @Query(value = "SELECT iindex from IINDEX iindex WHERE pub = ?1 ")
    Optional<IINDEX> findFirstByPubLike(String pub);

    @Query(value = "SELECT iindex from IINDEX iindex WHERE written_on = ?1 ")
    List<IINDEX> writtenOn(String writtenOn);

    @Override
    <S extends IINDEX> S save(S entity);

    default Boolean idtyIsMember(String pubkey) {
        return findFirstByPubLike(pubkey).map(i -> i.member).orElse(false);
    }

    @Transactional
    @Modifying
    default void trimRecords(Integer mTime) {

        var below = duplicatesBelow(mTime);
        below.forEach(d -> {
            var del = fetchTrimmed(d);
            if (del.size() > 0) {

                System.out.println("MINDEX trimRecords " + del);

                deleteAll(del);
            }

        });
//        var below = duplicatesBelow(mTime);
//        if (below.size() > 0) {
//            System.out.println("IINDEX trimRecords " + below);
//            below.forEach(d -> {
//
//                var toDel = idtyByPubkey(d);
//                var merge = toDel.stream().reduce((i1, i2) -> {
//                    var tmp = i1;
//
//                    if (i2.getOp().equals("UPDATE")) {
//                        tmp = i2;
//                        tmp.setUid(i1.getUid());
//                        tmp.setWotbid(i1.getWotbid());
//                        tmp.setCreated_on(i1.getCreated_on());
//                        tmp.setMember(i1.getMember());
//                    } else {
//                        tmp.setUid(i2.getUid());
//                        tmp.setWotbid(i2.getWotbid());
//                        tmp.setCreated_on(i2.getCreated_on());
//                        tmp.setMember(i2.getMember());
//
//                    }
//
//
//                    return tmp;
//                });
//
//                if (merge.isPresent()) {
//                    System.out.println("triming Records " + toDel + " IINDEX, inserting  " + merge.get());
//
//                    var saved = save(merge.get());
//                    if (saved != null) {
//                        deleteAll(toDel);
//                        System.out.println("triming Records GREATSUCCESS " + saved);
//                    } else {
//                        System.out.println("triming Records EERRROR ");
//                    }
//                    //System.exit(1);
//
//
//                }
//
//            });
//        }

    }


    @Query("SELECT DISTINCT pub FROM IINDEX m WHERE writtenOn < ?1 GROUP BY pub HAVING count(*) > 1")
    List<String> duplicatesBelow(Integer blockNumber);

    @Query(value = "SELECT * FROM IINDEX WHERE pub = ?1 ORDER BY writtenOn DESC LIMIT 100 OFFSET 1", nativeQuery = true)
    List<IINDEX> fetchTrimmed(String pub);


}

	
