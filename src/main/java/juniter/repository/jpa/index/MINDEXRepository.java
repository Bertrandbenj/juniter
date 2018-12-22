package juniter.repository.jpa.index;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Stream;

/**
 * Repository to manage {@link MINDEX} instances.
 */
@Repository
public interface MINDEXRepository extends JpaRepository<MINDEX, Long> {

    @Query(value = "SELECT mi FROM MINDEX mi WHERE pub = ?1 ")
    Stream<MINDEX> member(String pubkey);

    @Query(value = "SELECT c from MINDEX c WHERE written_on = ?1")
    List<MINDEX> writtenOn(String s);
}

	
