package juniter.repository;

import java.util.List;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import juniter.model.persistence.tx.Transaction;
import juniter.model.persistence.tx.TxType;

public interface TxRepository extends JpaRepository<Transaction, Long> {

	Logger logger = LogManager.getLogger();

//	@Override
//	Optional<Transaction> findById(Long id);

	@Query("select t from Transaction t where thash = ?1")
	List<Transaction> findByTHash(String hash);

	default Stream<Transaction> findTxOneAndSameIssuerAndDest() {
		return streamAll() //
				.filter(t -> t.getIssuers().size() == 1) //
				.filter(t -> t.getOutputs().size() == 1) //
				.filter(t -> {
					final var iss = t.getIssuers().get(0);
					final var dest = t.getOutputs().get(0);
					logger.info(iss.getPubkey() + " " + dest.getOutputCondition());
					return iss.getPubkey().equals(dest.getFctParam());
				});
	}

	@Query("select t from Transaction t where comment IS NOT NULL AND comment <> ''")
	Stream<Transaction> findTxsHavingComment();;

	default Stream<Transaction> findTxsHavingTxInput() {
		return streamAll() //
				.filter(t -> t.getInputs().stream().anyMatch(tx -> tx.getType().equals(TxType.T)))//
				.filter(t -> t.getInputs().size() < 30) //
				.limit(100);
	};

	default Stream<Transaction> findTxsWithMultipleOutputs() {
		return streamAll() //
				.filter(t -> t.getOutputs().size() > 2)//
				.limit(100);
	}

	default Stream<Transaction> findTxWithMultipleIssuers() {
		return streamAll().filter(t -> t.getIssuers().size() > 1);
	};

	default Stream<Transaction> findTxWithOtherThanSig() {
		return streamAll()
				.filter(t -> t.getOutputs().stream().anyMatch(i -> !i.getOutputCondition().startsWith("SIG")));
	};

	@Query("select t from Transaction t")
	Stream<Transaction> streamAll();

	/**
	 * Received by transactions
	 *
	 * @param pubkey as String or PubKey
	 * @return
	 */
	default Stream<Transaction> streamTransactionReceivedBy(Object pubkey) {
		return streamAll().filter(t -> t.txReceivedBy(pubkey));
	};

	/**
	 * Sent by transactions
	 *
	 * @param pubkey as String or PubKey
	 * @return
	 */
	default Stream<Transaction> streamTransactionSentBy(Object pubkey) {
		return streamAll().filter(t -> t.txSentBy(pubkey));
	};

}
