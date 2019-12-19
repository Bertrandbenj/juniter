package juniter.repository.jpa.sandbox;

import juniter.core.model.dbo.sandbox.TransactionSandboxed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface TxSandboxRepository extends JpaRepository<TransactionSandboxed, Long> {

    @Query("DELETE FROM TransactionSandboxed t WHERE hash = ?1")
    void deleteByHash(String s);
}
