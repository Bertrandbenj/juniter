package juniter.repository.jpa.block;

import juniter.core.model.dbo.tx.Transaction;
import juniter.core.model.meta.SourceType;
import juniter.core.model.technical.Dividend;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.stream.Stream;

public interface TxRepository extends JpaRepository<Transaction, Long> {

    Logger LOG = LogManager.getLogger(TxRepository.class);

//	@Override
//	Optional<TransactionDTO> findById(Long id);

    @Query("SELECT t FROM Transaction t WHERE hash = ?1")
    List<Transaction> findByTHash(String hash);

    default Stream<Transaction> findTxOneAndSameIssuerAndDest() {
        return streamAll() //
                .filter(t -> t.getIssuers().size() == 1) //
                .filter(t -> t.getOutputs().size() == 1) //
                .filter(t -> {
                    final var iss = t.getIssuers().iterator().next();
                    final var dest = t.getOutputs().iterator().next();
                    LOG.info(iss + " " + dest.getConditionString());
                    return iss.equals(dest.getConditionString());
                });
    }

    @Query("SELECT t FROM Transaction t WHERE comment IS NOT NULL AND comment <> ''")
    Stream<Transaction> findTxsHavingComment();

    default Stream<Transaction> findTxsHavingTxInput() {
        return streamAll() //
                .filter(t -> t.getInputs().stream().anyMatch(tx -> tx.type().equals(SourceType.T)))//
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
                .filter(t -> t.getOutputs().stream().anyMatch(i -> !i.getConditionString().startsWith("SIG")));
    }

    @Query("SELECT t FROM Transaction t")
    Stream<Transaction> streamAll();


    @Query("SELECT t FROM Transaction t INNER JOIN t.issuers i WHERE i = ?1 AND t.written.medianTime >= ?2 AND t.written.medianTime <= ?3")
    List<Transaction> transactionsOfIssuerWindowedByTime(String pubkey, Long start, Long end);

    @Query("SELECT t FROM Transaction t INNER JOIN t.outputs o WHERE o.condition LIKE CONCAT('%',?1,'%') AND t.written.medianTime >= ?2 AND t.written.medianTime <= ?3")
    List<Transaction> transactionsOfReceiverWindowedByTime(String pubkey, Long start, Long end);


    @Query("SELECT t FROM Transaction t INNER JOIN t.issuers i WHERE i = ?1 ")
    List<Transaction> transactionsOfIssuerWindowedByBlock(String pubkey, Integer start, Integer end);

    @Query("SELECT t FROM Transaction t INNER JOIN t.outputs o WHERE o.condition LIKE CONCAT('%',?1,'%') AND t.blockstamp.number >= ?2 AND t.blockstamp.number <= ?3 ")
    List<Transaction> transactionsOfReceiverWindowedByBlock(String pubkey, Integer start, Integer end);


    @Query("SELECT t FROM Transaction t INNER JOIN t.outputs o WHERE o.condition LIKE CONCAT('%',:pubkey,'%')")
    List<Transaction> transactionsOfReceiver(@Param("pubkey") String pubkey);

    @Query("SELECT t FROM Transaction t INNER JOIN t.issuers i WHERE i = ?1 ")
    List<Transaction> transactionsOfIssuer(String pubkey);


    @Query(value = "SELECT DISTINCT blockstamp.number FROM  Transaction t ORDER BY blockstamp.number ")
    List<Integer> withTx();

    @Query("SELECT new juniter.core.model.technical.Dividend(number, medianTime, dividend) FROM DBBlock WHERE dividend IS NOT null AND number >= ?1")
    List<Dividend> dividendsFrom(Integer number);

    @Query("SELECT new juniter.core.model.technical.Dividend(number, medianTime, dividend) FROM DBBlock WHERE dividend IS NOT null AND number >= (SELECT MIN(written.number) FROM Member m WHERE pubkey = ?1)")
    List<Dividend> dividendsOf(String pubkey);


}
