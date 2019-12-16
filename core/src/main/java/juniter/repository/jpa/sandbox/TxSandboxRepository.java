package juniter.repository.jpa.sandbox;

import juniter.core.model.dbo.sandbox.TransactionSandboxed;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TxSandboxRepository extends JpaRepository<TransactionSandboxed, Long> {


}
