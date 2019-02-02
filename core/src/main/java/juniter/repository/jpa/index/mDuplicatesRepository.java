package juniter.repository.jpa.index;

import juniter.core.model.index.Duplicates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

/**
 * Repository to manage {@link Duplicates} instances.
 */
@Repository
public interface mDuplicatesRepository extends JpaRepository<Duplicates, Long> {


    @Query("SELECT d from Duplicates d ")
    Stream<Duplicates> mDuplicates();


}

	
