package juniter.repository.jpa.maybe;

import juniter.core.model.dbo.wot.Joiner;
import juniter.core.model.dbo.wot.Leaver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface JoinerRepository extends JpaRepository<Joiner, Long> {
//
//	@Query(value = "SELECT DISTINCT writtenon FROM  Joiner j ORDER BY writtenon " )
//	List<Integer> withJoiners();


}
