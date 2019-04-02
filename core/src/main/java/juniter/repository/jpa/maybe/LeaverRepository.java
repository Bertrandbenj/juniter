package juniter.repository.jpa.maybe;

import juniter.core.model.dbo.wot.Excluded;
import juniter.core.model.dbo.wot.Leaver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LeaverRepository extends JpaRepository<Leaver, Long> {
//
//	@Query(value = "SELECT DISTINCT writtenon FROM  Leaver l ORDER BY writtenon ")
//	List<Integer> withLeavers();

}
