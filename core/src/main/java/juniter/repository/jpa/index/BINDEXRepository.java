package juniter.repository.jpa.index;

import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.index.BINDEX;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Repository to manage {@link BINDEX} instances.
 */
@Repository
public interface BINDEXRepository extends JpaRepository<BINDEX, Long> {

    @Transactional(readOnly = true)
    @Query("FROM BINDEX")
    Stream<BINDEX> all();

    @Query("FROM BINDEX b ORDER BY number")
    List<BINDEX> allAsc();

    @Query("SELECT b from BINDEX b WHERE number = ?1 AND currency = ?2")
    Optional<BINDEX> byNum(Integer number, String ccy);

    Optional<BINDEX> findFirstByNumberIsNotNullOrderByNumberDesc();

    default Optional<BINDEX> head(){
        return findFirstByNumberIsNotNullOrderByNumberDesc();
    }


    @Transactional
    @Modifying
    default void trim(int bIndexSize){
        var top1 = head().map(BINDEX::getNumber).orElseThrow();

        findAll().stream()
                .filter(b-> b.getNumber() < top1-bIndexSize)
                .forEach(this::delete);
    }

}

	
