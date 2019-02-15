package juniter.service.gva.dev;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.annotations.GraphQLSubscription;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import juniter.core.model.business.tx.Transaction;
import juniter.repository.jpa.TxRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class FunBrokenService {

	private static final Logger LOG = LogManager.getLogger();

	@Autowired
	private TxRepository txRepository;

	@Transactional
	@GraphQLQuery(name = "findByCommentIsNotNull", description = "find a TransactionDTO having more than one issuer")
	public List<Transaction> findByCommentIsNotNull() {
		try (var txs = txRepository.findTxsHavingComment().limit(10)) {
			return txs.collect(Collectors.toList());
		} catch (final Exception e) {
			LOG.error("findByCommentIsNotNull " + e);
		}
		return null;
	}

	@Transactional
	@GraphQLQuery(name = "findTxsHavingTxInput", description = "find a TransactionDTO having more than one issuer")
	public List<Transaction> findTxsHavingTxInput() {
		try (var txs = txRepository.findTxsHavingTxInput().limit(10)) {
			return txs.collect(Collectors.toList());
		} catch (final Exception e) {
			LOG.error("findTxsHavingTxInput " + e);
		}
		return null;
	}

	@Transactional
	@GraphQLQuery(name = "findTxsWithMultipleOutputs", description = "find a TransactionDTO having more than one issuer")
	public List<Transaction> findTxsWithMultipleOutputs() {
		try (var txs = txRepository.findTxsWithMultipleOutputs().limit(10)) {
			return txs.collect(Collectors.toList());
		} catch (final Exception e) {
			LOG.error("findTxWithMultipleOutputs " + e);
		}
		return null;
	}

	@Transactional
	@GraphQLQuery(name = "findTxToOneself", description = "find a Self TransactionDTO having one input issuer equals to the output issuer ")
	public List<Transaction> findTxToOneself() {
		try (var txs = txRepository.findTxOneAndSameIssuerAndDest().limit(10)) {
			return txs.collect(Collectors.toList());
		} catch (final Exception e) {
			LOG.error("findTxToOneself " + e);
		}
		return null;
	}

	@Transactional
	@GraphQLQuery(name = "findTxWithMultipleIssuers", description = "find a TransactionDTO having more than one issuer")
	public List<Transaction> findTxWithMultipleIssuers() {
		try (var txs = txRepository.findTxWithMultipleIssuers().limit(10)) {
			return txs.collect(Collectors.toList());
		} catch (final Exception e) {
			LOG.error("findTxWithMultipleIssuers " + e);
		}
		return null;
	}

	@Transactional
	@GraphQLQuery(name = "findTxWithOtherThanSig", description = "find a TransactionDTO having more than one issuer")
	public List<Transaction> findTxWithOtherThanSig() {
		try (var txs = txRepository.findTxWithOtherThanSig().limit(10)) {
			return txs.collect(Collectors.toList());
		} catch (final Exception e) {
			LOG.error("findTxWithOtherThanSig " + e);
		}
		return null;
	}



	@Transactional
	@GraphQLSubscription(name = "sub", description = "subscribe test")
	public Publisher<Timer> sub(@GraphQLArgument(name = "raw") String raw) {
		LOG.info(" GVA - sub");
		Observable<Timer> observable = Observable
				.interval(1, TimeUnit.SECONDS)
				.flatMap(n -> Observable.create(observableEmitter -> {
					observableEmitter.onNext(new Timer(LocalDateTime.now()));
				}));
		return observable.toFlowable(BackpressureStrategy.LATEST);

	}

}