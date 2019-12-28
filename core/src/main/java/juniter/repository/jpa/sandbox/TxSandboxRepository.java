package juniter.repository.jpa.sandbox;

import juniter.core.model.dbo.sandbox.TransactionSandboxed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface TxSandboxRepository extends JpaRepository<TransactionSandboxed, Long> {


    @Transactional
    @Modifying
    @Query("DELETE FROM TransactionSandboxed t ")
    void deleteByHashs(List<String> hashs);
}
