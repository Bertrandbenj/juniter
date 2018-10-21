package juniter.repository.jpa;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import juniter.core.model.wot.Certification;

public interface CertsRepository extends JpaRepository<Certification, Long> {

//	@Override
//	Optional<Transaction> findById(Long id);
	
	@Query("select t from Certification t where certified.pubkey = ?1 ")
	Stream<Certification> streamCertifiersOf(String pubkeyOrUid);

	@Query("select t from Certification t where certifier.pubkey = ?1 ")
	Stream<Certification> streamCertifiedBy(String pubkeyOrUid);
	
}
