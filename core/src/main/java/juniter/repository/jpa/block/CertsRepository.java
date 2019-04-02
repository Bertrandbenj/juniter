package juniter.repository.jpa.block;

import juniter.core.model.dbo.wot.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.stream.Stream;

public interface CertsRepository extends JpaRepository<Certification, Long> {


	@Query("select t from Certification t where certified = ?1 ")
	Stream<Certification> streamCertifiersOf(String pubkeyOrUid);

	@Query("select t from Certification t where certifier = ?1 ")
	Stream<Certification> streamCertifiedBy(String pubkeyOrUid);


	@Query(value = "SELECT DISTINCT blockNumber FROM  Certification c ORDER BY blockNumber ")
	List<Integer> withCertifications();
	
}
