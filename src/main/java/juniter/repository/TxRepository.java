package juniter.repository;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import juniter.model.persistence.Hash;
import juniter.model.persistence.tx.Transaction;
import java.lang.String;
import java.util.List;

public interface TxRepository extends JpaRepository<Transaction, Long> {

//	@Override
//	Optional<Transaction> findById(Long id);
	
	@Query("select t from Transaction t")
	Stream<Transaction> streamAll();

	@Query("select t from Transaction t where tx_hash = ?1")
	List<Transaction> findByTHash(String hash);
	
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
	


	default Transaction findTransactionWithMultipleIssuers(){
		return streamAll() // 
				.filter(t -> t.issuers().size()>1) // 
				.findFirst().get();
	};
	
	default Transaction findTransactionOneAndSameIssuerAndDest(){
		return streamAll() // 
				.filter(t -> t.issuers().size()==1) // 
				.filter(t -> t.outputs().size()==1) // 
				.filter(t -> t.inputs().get(0).equals(t.outputs().get(0).functionReferenceValue()))
				.findFirst().get();
	};
	


}
