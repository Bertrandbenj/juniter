package juniter.repository.jpa.block;

import juniter.core.model.dbo.ChainParameters;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dto.node.IssuersFrameDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.GenericJDBCException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Repository to manage {@link ChainParameters} instances.
 */
@Repository
public interface ParamsRepository extends JpaRepository<ChainParameters, Long> {
    Logger LOG = LogManager.getLogger(ParamsRepository.class);

    @Query("FROM ChainParameters p WHERE currency = ?1")
    ChainParameters findByCCY(String ccy) ;


}