package juniter.repository.jpa.sandbox;

import juniter.core.model.dbo.sandbox.MemberSandboxed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MembershipSandboxRepository extends JpaRepository<MemberSandboxed, Long> {

    @Query("DELETE FROM IdentitySandboxed i WHERE pubkey = ?1")
    void deleteByPubkey(String s);

}
