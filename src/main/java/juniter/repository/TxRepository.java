package juniter.repository;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import juniter.model.base.PubKey;
import juniter.model.tx.Transaction;

public interface TxRepository extends JpaRepository<Transaction, Long> {

//	@Override
//	Optional<Transaction> findById(Long id);
	
	@Query("select t from Transaction t")
	Stream<Transaction> streamAll();

	default Stream<Transaction> streamTransactionSentBy(PubKey pubkey){
		return streamAll().filter(t -> t.txSentBy(pubkey));
	};
	

	default Stream<Transaction> streamTransactionSentBy(String pubkey){
		return streamTransactionSentBy(new PubKey(pubkey));
	};


	


}
