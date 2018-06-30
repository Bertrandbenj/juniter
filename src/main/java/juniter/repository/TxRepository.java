package juniter.repository;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import juniter.model.tx.Transaction;

public interface TxRepository extends JpaRepository<Transaction, Long> {

//	@Override
//	Optional<Transaction> findById(Long id);
	
	@Query("select t from Transaction t")
	Stream<Transaction> streamAll();

	
	/**
	 * Sent by transactions 
	 * @param pubkey as String or PubKey 
	 * @return
	 */
	default Stream<Transaction> streamTransactionSentBy(Object pubkey){
		return streamAll().filter(t -> t.txSentBy(pubkey));
	};
	
	/**
	 * Received by transactions
	 * @param pubkey as String or PubKey 
	 * @return
	 */
	default Stream<Transaction> streamTransactionReceivedBy(Object pubkey){
		return streamAll().filter(t -> t.txReceivedBy(pubkey));
	};
	



	


}
