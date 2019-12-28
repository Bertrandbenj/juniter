package juniter.repository.jpa.sandbox;

import juniter.core.model.dbo.sandbox.IdentitySandboxed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface IdtySandboxRepository extends JpaRepository<IdentitySandboxed, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM IdentitySandboxed i WHERE pubkey = ?1")
    void deleteByPubkey(String s);
}
