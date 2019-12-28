package juniter.repository.jpa.sandbox;

import juniter.core.model.dbo.sandbox.MemberSandboxed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MembershipSandboxRepository extends JpaRepository<MemberSandboxed, Long> {

    @Transactional
    @Modifying
    @Query("DELETE FROM MemberSandboxed i WHERE pubkey = ?1")
    void deleteByPubkey(String s);

    @Transactional
    @Modifying
    @Query("DELETE FROM MemberSandboxed m WHERE m.pubkey IN (:pubs) ")
    void deleteByPubkeys(List<String> pubs);
}
