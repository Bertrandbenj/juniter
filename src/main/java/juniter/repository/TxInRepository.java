package juniter.repository;

import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import juniter.model.tx.TxInput;

public interface TxInRepository extends JpaRepository<TxInput, Long> {
	
	@Query("select t from TxInput t where type = 'T' and dsource = :#{#pubkey} ")
	Stream<TxInput> streamTxSentBy(@Param("pubkey") String pubkey);

	
}