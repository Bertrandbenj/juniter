package juniter.repository.jpa.maybe;

import juniter.core.model.dbo.wot.Certification;
import juniter.core.model.dbo.wot.Excluded;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.stream.Stream;

public interface ExcludedRepository extends JpaRepository<Excluded, Long> {

//	@Query(value = "SELECT DISTINCT writtenon FROM  Excluded e ORDER BY writtenon ")
//	List<Integer> withExcluded();
}
