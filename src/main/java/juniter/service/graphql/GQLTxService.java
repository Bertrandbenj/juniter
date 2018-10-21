package juniter.service.graphql;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.leangen.graphql.annotations.GraphQLQuery;
import juniter.core.model.tx.Transaction;
import juniter.repository.jpa.TxRepository;

@Service
public class GQLTxService {

	private static final Logger logger = LogManager.getLogger();

	@Autowired
	private TxRepository txRepository;

	@Transactional
	@GraphQLQuery(name = "findByCommentIsNotNull", description = "find a Transaction having more than one issuer")
	public List<Transaction> findByCommentIsNotNull() {
		try (var txs = txRepository.findTxsHavingComment()) {
			return txs.collect(Collectors.toList());
		} catch (final Exception e) {
			logger.error("findByCommentIsNotNull " + e);
		}
		return null;
	}

	@Transactional
	@GraphQLQuery(name = "findTxsHavingTxInput", description = "find a Transaction having more than one issuer")
	public List<Transaction> findTxsHavingTxInput() {
		try (var txs = txRepository.findTxsHavingTxInput()) {
			return txs.collect(Collectors.toList());
		} catch (final Exception e) {
			logger.error("findTxsHavingTxInput " + e);
		}
		return null;
	}

	@Transactional
	@GraphQLQuery(name = "findTxsWithMultipleOutputs", description = "find a Transaction having more than one issuer")
	public List<Transaction> findTxsWithMultipleOutputs() {
		try (var txs = txRepository.findTxsWithMultipleOutputs()) {
			return txs.collect(Collectors.toList());
		} catch (final Exception e) {
			logger.error("findTxWithMultipleOutputs " + e);
		}
		return null;
	}

	@Transactional
	@GraphQLQuery(name = "findTxToOneself", description = "find a Self Transaction having one input issuer equals to the output issuer ")
	public List<Transaction> findTxToOneself() {
		try (var txs = txRepository.findTxOneAndSameIssuerAndDest()) {
			return txs.collect(Collectors.toList());
		} catch (final Exception e) {
			logger.error("findTxToOneself " + e);
		}
		return null;
	}

	@Transactional
	@GraphQLQuery(name = "findTxWithMultipleIssuers", description = "find a Transaction having more than one issuer")
	public List<Transaction> findTxWithMultipleIssuers() {
		try (var txs = txRepository.findTxWithMultipleIssuers()) {
			return txs.collect(Collectors.toList());
		} catch (final Exception e) {
			logger.error("findTxWithMultipleIssuers " + e);
		}
		return null;
	}

	@Transactional
	@GraphQLQuery(name = "findTxWithOtherThanSig", description = "find a Transaction having more than one issuer")
	public List<Transaction> findTxWithOtherThanSig() {
		try (var txs = txRepository.findTxWithOtherThanSig()) {
			return txs.collect(Collectors.toList());
		} catch (final Exception e) {
			logger.error("findTxWithOtherThanSig " + e);
		}
		return null;
	}

}