package juniter.service.graphql;

import io.leangen.graphql.annotations.GraphQLQuery;
import juniter.model.persistence.tx.Transaction;
import juniter.repository.TxRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GQLTxService {

	@Autowired
	private TxRepository txRepository;

	@Transactional
	@GraphQLQuery(name = "findTxToOneself", description = "find a Self Transaction having one input issuer equals to the output issuer ")
	public Transaction findTxToOneself() {
		return txRepository.findTransactionOneAndSameIssuerAndDest();
	}
	
	@Transactional
	@GraphQLQuery(name = "findTxWithMultipleIssuers", description = "find a Transaction having more than one issuer")
	public Transaction findTxWithMultipleIssuers() {
		return txRepository.findTransactionWithMultipleIssuers();
	}

}