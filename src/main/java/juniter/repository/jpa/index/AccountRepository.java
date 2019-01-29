package juniter.repository.jpa.index;

import juniter.core.model.index.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository to manage {@link Account} instances.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {


    @Query("SELECT a from Account a where bSum < 100 AND bSum > 0")
    List<Account> lowAccounts();


}

	
