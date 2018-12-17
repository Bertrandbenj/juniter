package juniter.repository.jpa.index;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

/**
 * Repository to manage {@link MINDEX} instances.
 */
@Repository
public interface MINDEXRepository extends JpaRepository<MINDEX, Long> {

    @Query(value = "SELECT mi FROM MINDEX mi WHERE pub = ?1 ")
    Stream<MINDEX> member(String pubkey);

}

	
