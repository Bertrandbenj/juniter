package juniter.repository.jpa.sandbox;

import juniter.core.model.dbo.sandbox.MemberSandboxed;
import juniter.core.model.dbo.wot.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MembershipSandboxRepository  extends JpaRepository<MemberSandboxed, Long> {



	
}
