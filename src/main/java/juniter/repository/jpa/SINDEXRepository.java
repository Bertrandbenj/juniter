package juniter.repository.jpa;

import juniter.core.model.index.SINDEX;
import juniter.core.model.net.Peer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to manage {@link Peer} instances.
 */
@Repository
public interface SINDEXRepository extends JpaRepository<SINDEX, Long> {
	
}

	
