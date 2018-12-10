package juniter.repository.jpa;

import juniter.core.model.index.IINDEX;
import juniter.core.model.net.Peer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository to manage {@link Peer} instances.
 */
@Repository
public interface IINDEXRepository extends JpaRepository<IINDEX, Long> {
	
}

	
