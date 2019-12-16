package juniter.repository.jpa.sandbox;

import juniter.core.model.dbo.sandbox.IdentitySandboxed;
import juniter.core.model.dbo.wot.Identity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdtySandboxRepository extends JpaRepository<IdentitySandboxed, Long> {



	
}
