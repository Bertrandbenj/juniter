package juniter.service.gva.tx;

import com.google.common.collect.Lists;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import juniter.core.model.tx.Transaction;
import juniter.repository.jpa.TxRepository;
import juniter.repository.jpa.index.SINDEXRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TransactionService {

	private static final Logger LOG = LogManager.getLogger();

	@Autowired
	private TxRepository txRepository;


	@Autowired SINDEXRepository sRepo;


	@Transactional
	@GraphQLQuery(name = "pendingTransactions", description = "find pending Transactions ")
	@GraphQLNonNull
	public List<@GraphQLNonNull Transaction> pendingTransactions() {
		return Lists.newArrayList();
	}

	@Transactional
	@GraphQLQuery(name = "transactionsOfIssuer", description = "find pending Transactions ")
	@GraphQLNonNull
	public List<@GraphQLNonNull Transaction> transactionsOfIssuer(@GraphQLNonNull @GraphQLArgument(name = "issuer") String issuer) {
		return Lists.newArrayList();
	}

	@Transactional
	@GraphQLQuery(name = "transactionsOfReceiver", description = "find Transactions of Receiver")
	@GraphQLNonNull
	public List<@GraphQLNonNull Transaction> transactionsOfReceiver(@GraphQLNonNull @GraphQLArgument(name = "receiver") String receiver) {
		return Lists.newArrayList();
	}

	@Transactional
	@GraphQLQuery(name = "transactionByHash", description = "find pending Transactions ")
	public List<Transaction> transactionByHash(@GraphQLNonNull @GraphQLArgument(name = "hash") String hash) {
		return Lists.newArrayList();
	}

	@Transactional
	@GraphQLQuery(name = "sourcesOfPubkey", description = "find a wallet's sources  ")
	@GraphQLNonNull
	public List<@GraphQLNonNull Source> sourcesOfPubkey(@GraphQLNonNull @GraphQLArgument(name = "pub") String pub) {
		return Lists.newArrayList();
	}


}