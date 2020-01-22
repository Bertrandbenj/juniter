package juniter.repository.jpa.index;

import juniter.core.model.dbo.index.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository to manage {@link Accounts} instances.
 */
@Repository
//@NoRepositoryBean
public interface AccountsRepository extends JpaRepository<Accounts, Long> {


    @Query("SELECT a from Accounts a where bSum < 100 AND bSum > 0")
    List<Accounts> lowAccounts();

    @Query("SELECT a from Accounts a where conditions LIKE CONCAT('%',?1,'%')")
    Accounts accountOf(String pubkey);


}

	
