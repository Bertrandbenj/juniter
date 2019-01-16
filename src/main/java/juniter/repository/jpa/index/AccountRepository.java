package juniter.repository.jpa.index;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

/**
 * Repository to manage {@link Account} instances.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {


    @Query("SELECT a from Account a where bSum < 100 AND bSum > 0")
    Stream<Account> lowAccounts();


}

	
