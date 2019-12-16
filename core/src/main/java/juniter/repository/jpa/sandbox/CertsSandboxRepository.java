package juniter.repository.jpa.sandbox;

import juniter.core.model.dbo.sandbox.CertificationSandboxed;
import juniter.core.model.dbo.wot.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

public interface CertsSandboxRepository extends JpaRepository<CertificationSandboxed, Long> {

	
}
