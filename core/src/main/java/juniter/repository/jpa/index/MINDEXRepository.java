package juniter.repository.jpa.index;


import juniter.core.model.dbo.index.MINDEX;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

/**
 * Repository to manage {@link MINDEX} instances.
 */
@Repository
public interface MINDEXRepository extends JpaRepository<MINDEX, Long> {


    @Override
    long count();

    @Override
    List<MINDEX> findAll();

    @Override
    void deleteAll(Iterable<? extends MINDEX> entities);

    @Query(value = "SELECT mi FROM MINDEX mi WHERE pub = ?1 ORDER BY signed DESC")
    List<MINDEX> member(String pubkey);

    @Query(value = "SELECT m from MINDEX m WHERE  written.number = ?1 AND  written.hash = ?2 ")
    List<MINDEX> writtenOn(Integer writtenOn, String writtenHash);

    @Query(value = "SELECT m from MINDEX m WHERE  pub LIKE CONCAT('%',?1,'%')")
    Stream<MINDEX> search(String search);

    @Query("SELECT m FROM MINDEX m WHERE expires_on <= ?1  OR revoked.medianTime > ?1 ")
    List<MINDEX> getForTrim(Long mTime);

    @Transactional
    @Modifying
    @Query("DELETE FROM MINDEX m WHERE pub = ?1 AND written = ?2 ")
    void trimCreate(String pub, String created_on);


    @Transactional
    @Modifying
    @Query("SELECT m FROM MINDEX m WHERE written.number = ?1 ")
    List<MINDEX> bellowWrittenOn(Integer written_on);


    @Transactional
    @Modifying
    default void trimRecords(Integer mTime) {
        var below = duplicatesBelow(mTime);
        below.remove(0);
        below.forEach(d -> {
            var del = fetchTrimmed(d);
            if (del.size() > 0) {

                System.out.println("MINDEX trimRecords " + del);

                deleteAll(del);
            }

        });
    }

    @Query("SELECT DISTINCT i.uid FROM MINDEX m LEFT JOIN IINDEX i ON i.pub = m.pub WHERE m.expires_on > ?1 AND m.expires_on < ?2 ")
    List<String> expiresBetween(Long begin, Long end);


    @Query("SELECT DISTINCT pub FROM MINDEX m WHERE written.number < ?1 GROUP BY pub HAVING count(*) > 1")
    List<String> duplicatesBelow(Integer blockNumber);

    @Query(value = " FROM MINDEX WHERE pub = ?1 ORDER BY written.number ")
    List<MINDEX> fetchTrimmed(String pub);


//    @Query(value = "SELECT *, (SELECT m2.expires_on \n" +
//            "             FROM mindex m2 \n" +
//            "             WHERE m2.pub = m1.pub \n" +
//            "             AND m2.writtenOn = (\n" +
//            "               SELECT MAX(m4.writtenOn)\n" +
//            "               FROM mindex m4\n" +
//            "               WHERE pub = m2.pub\n" +
//            "             )) as renewal ,\n" +
//            "\n" +
//            "             ( SELECT m2.expired_on \n" +
//            "             FROM mindex m2 \n" +
//            "             WHERE m2.pub = m1.pub \n" +
//            "             AND m2.writtenOn = (\n" +
//            "               SELECT MAX(m4.writtenOn)\n" +
//            "               FROM mindex m4\n" +
//            "               WHERE pub = m2.pub\n" +
//            "             )\n" +
//            "           ) as expiry \n" +
//            "FROM mindex as m1\n" +
//            "WHERE m1.expires_on <= ?1\n" +
//            "  AND m1.revokes_on > ?1 ", nativeQuery = true, name = "is it slow ?")
//    List<MINDEX> findPubkeysThatShouldExpire(Long mtime);


    default List<MINDEX> findPubkeysThatShouldExpire2(Long mtime) {
        return List.of();
    }


    @Query(value = "SELECT m FROM MINDEX m WHERE m.revokes_on <= ?1 AND m.revoked IS NULL ")
    List<MINDEX> findRevokesOnLteAndRevokedOnIsNull(Long mTime);
}

	
