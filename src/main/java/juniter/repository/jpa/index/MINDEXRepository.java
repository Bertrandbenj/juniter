package juniter.repository.jpa.index;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository to manage {@link MINDEX} instances.
 */
@Repository
public interface MINDEXRepository extends JpaRepository<MINDEX, Long> {
    @Override
    List<MINDEX> findAll();
}

	
