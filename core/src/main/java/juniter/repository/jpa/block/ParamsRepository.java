package juniter.repository.jpa.block;

import juniter.core.model.dbo.ChainParameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Repository to manage {@link ChainParameters} instances.
 */
@Repository
public interface ParamsRepository extends JpaRepository<ChainParameters, Long> {
    Logger LOG = LogManager.getLogger(ParamsRepository.class);

    @Query("FROM ChainParameters p WHERE currency = ?1")
    ChainParameters paramsByCCY(String ccy);

    @Query("SELECT p.currency FROM ChainParameters p")
    List<String> existingCCY();


}