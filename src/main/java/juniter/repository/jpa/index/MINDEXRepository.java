package juniter.repository.jpa.index;

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

    @Query(value = "SELECT mi FROM MINDEX mi WHERE pub = ?1 ")
    Stream<MINDEX> member(String pubkey);

    @Query(value = "SELECT m from MINDEX m WHERE written_on = ?1")
    List<MINDEX> writtenOn(String s);

    @Query(value = "SELECT m from MINDEX m WHERE  pub LIKE CONCAT('%',?1,'%')")
    List<MINDEX> search(String search);


    @Query("SELECT m FROM MINDEX m WHERE expires_on <= ?1  OR revoked_on > ?1 ")
    List<MINDEX> getForTrim(Long mTime);

    @Transactional
    @Modifying
    @Query("DELETE FROM MINDEX m WHERE pub = ?1 AND written_on = ?2 ")
    void trimCreate(String pub, String created_on);

    @Transactional
    @Modifying
    default void trim(Long mTime) {

        var dups = mDuplicates();
        System.out.println("trim MINDEX " + mTime + " " + dups);

        dups.forEach(d -> {
            var del = fetch(d);

            System.out.println("trim MINDEX deleting " + del);
            //System.exit(1);
            deleteAll(del);
        });


    }


    @Query("SELECT pub FROM MINDEX m GROUP BY pub HAVING count(*) > 1")
    List<String> mDuplicates();

    @Query(value = "SELECT * FROM MINDEX WHERE pub = ?1 ORDER BY writtenOn DESC LIMIT 10 OFFSET 1", nativeQuery = true)
    List<MINDEX> fetch(String pub);
}

	
