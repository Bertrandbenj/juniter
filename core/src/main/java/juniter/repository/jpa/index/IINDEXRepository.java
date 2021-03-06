package juniter.repository.jpa.index;

import juniter.core.model.dbo.index.IINDEX;
import juniter.core.model.dto.wot.MemberDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Repository to manage {@link IINDEX} instances.
 */
@Repository
public interface IINDEXRepository extends JpaRepository<IINDEX, Long> {

    @Transactional(readOnly = true)
    @Query("FROM IINDEX")
    List<IINDEX> all();

    @Query(value = "SELECT new juniter.core.model.dto.wot.MemberDTO(pub, uid) FROM IINDEX i WHERE i.member IS NOT NULL AND i.member IS TRUE")
    List<MemberDTO> members();

    @Query("FROM IINDEX iindex WHERE hash = ?1 ")
    List<IINDEX> pendingIdentityByHash(String hash);

    @Query("FROM IINDEX iindex WHERE  uid = ?1 OR pub = ?2 ")
    List<IINDEX> byUidOrPubkey(String uid, String pub);

    @Query("FROM IINDEX iindex WHERE  uid = ?1 OR pub LIKE CONCAT('%',?1,'%') ")
    List<IINDEX> search(String search);

    @Query("FROM IINDEX iindex WHERE  uid = ?1 ")
    List<IINDEX> byUid(String uid);

    @Query("FROM IINDEX iindex WHERE pub = ?1 ")
    List<IINDEX> idtyByPubkey(String pubkey);

    @Query("FROM IINDEX iindex WHERE written.number = ?1 AND  written.hash = ?2 ")
    List<IINDEX> writtenOn(Integer writtenOn, String writtenHash);

    @Query("SELECT DISTINCT pub FROM IINDEX m WHERE written.number < ?1 GROUP BY pub HAVING count(*) > 1")
    List<String> duplicatesBelow(Integer blockNumber);

    @Query("FROM IINDEX WHERE pub = ?1 ORDER BY written.number")
    List<IINDEX> fetchTrimmed(String pub);

    @Transactional
    @Modifying
    default void trimRecords(Integer mTime) {

        var below = duplicatesBelow(mTime);
        below.remove(0);
        below.forEach(d -> {
            var del = fetchTrimmed(d);
            if (del.size() > 0) {

                deleteAll(del);
            }

        });
//        var below = duplicatesBelow(mTime);
//        if (below.getSize() > 0) {
//            System.out.println("IINDEX trimRecords " + below);
//            below.forEach(d -> {
//
//                var toDel = idtyByPubkey(d);
//                var merge = toDel.stream().reduce((i1, i2) -> {
//                    var tmp = i1;
//
//                    if (i2.getOp().equals("UPDATE")) {
//                        tmp = i2;
//                        tmp.setUserid(i1.getUserid());
//                        tmp.setWotbid(i1.getWotbid());
//                        tmp.setSigned(i1.getSigned());
//                        tmp.setMember(i1.getMember());
//                    } else {
//                        tmp.setUserid(i2.getUserid());
//                        tmp.setWotbid(i2.getWotbid());
//                        tmp.setSigned(i2.getSigned());
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


}

	
