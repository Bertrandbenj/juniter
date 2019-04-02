package juniter.repository.jpa.maybe;

import juniter.core.model.dbo.wot.Renew;
import juniter.core.model.dbo.wot.Revoked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RevokedRepository extends JpaRepository<Revoked, Long> {

//	@Query(value = "SELECT DISTINCT writtenon FROM  Revoked r ORDER BY writtenon ")
//	List<Integer> withRevoked();



}
