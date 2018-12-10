package juniter.repository.jpa;

import juniter.core.model.index.CINDEX;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository to manage {@link CINDEX} instances.
 */
@Repository
public interface CINDEXRepository extends JpaRepository<CINDEX, Long> {

    @Override
    <S extends CINDEX> List<S> saveAll(Iterable<S> entities);
}

	
