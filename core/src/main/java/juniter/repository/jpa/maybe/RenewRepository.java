package juniter.repository.jpa.maybe;

import juniter.core.model.dbo.wot.Member;
import juniter.core.model.dbo.wot.Renew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RenewRepository extends JpaRepository<Renew, Long> {




}
