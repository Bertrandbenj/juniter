package juniter.repository.jpa;

import juniter.core.model.tx.Transaction;
import juniter.core.model.tx.TxType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.stream.Stream;

public interface TxRepository extends JpaRepository<Transaction, Long> {

	Logger LOG = LogManager.getLogger();

//	@Override
//	Optional<TransactionDTO> findById(Long id);

	@Query("select t from Transaction t where thash = ?1")
	List<Transaction> findByTHash(String hash);

	default Stream<Transaction> findTxOneAndSameIssuerAndDest() {
		return streamAll() //
				.filter(t -> t.getIssuers().size() == 1) //
				.filter(t -> t.getOutputs().size() == 1) //
				.filter(t -> {
					final var iss = t.getIssuers().iterator().next();
					final var dest = t.getOutputs().iterator().next();
					LOG.info(iss + " " + dest.getOutputCondition());
					return iss.equals(dest.getOutputCondition());
				});
	}

	@Query("select t from Transaction t where comment IS NOT NULL AND comment <> ''")
	Stream<Transaction> findTxsHavingComment();

    default Stream<Transaction> findTxsHavingTxInput() {
		return streamAll() //
				.filter(t -> t.getInputs().stream().anyMatch(tx -> tx.getType().equals(TxType.T)))//
				.filter(t -> t.getInputs().size() < 30) //
				.limit(10);
	}

    default Stream<Transaction> findTxsWithMultipleOutputs() {
		return streamAll() //
				.filter(t -> t.getOutputs().size() > 2)//
				.limit(10);
	}

	default Stream<Transaction> findTxWithMultipleIssuers() {
		return streamAll().filter(t -> t.getIssuers().size() > 1);
	}

    default Stream<Transaction> findTxWithOtherThanSig() {
		return streamAll()
				.filter(t -> t.getOutputs().stream().anyMatch(i -> !i.getOutputCondition().startsWith("SIG")));
	}

    @Query("select t from Transaction t")
	Stream<Transaction> streamAll();

	@Query("select t from Transaction t where consumed = false ")
	default Stream<Transaction> transactionsOfReceiver(String pubkey) {
		return streamAll().filter(t -> t.txReceivedBy(pubkey));
	}

    /**
	 * Sent by transactions
	 *
	 * @param pubkey as String or PubKey
	 * @return
	 */
	default Stream<Transaction> transactionsOfIssuer(Object pubkey) {
		return streamAll().filter(t -> t.txSentBy(pubkey));
	}

}
