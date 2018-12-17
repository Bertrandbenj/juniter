package juniter.service.gva.tx;

import com.google.common.collect.Lists;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import juniter.repository.jpa.TxRepository;
import juniter.repository.jpa.index.SINDEXRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

	private static final Logger LOG = LogManager.getLogger();

	@Autowired private TxRepository txRepository;

	@Autowired private SINDEXRepository sRepo;

	@Autowired private ModelMapper modelMapper ;


	@Transactional(readOnly=true)
	@GraphQLQuery(name = "pendingTransactions", description = "find pending Transactions ")
	@GraphQLNonNull
	public List<@GraphQLNonNull Transaction> pendingTransactions() {
		return Lists.newArrayList();
	}

	@Transactional(readOnly=true)
	@GraphQLQuery(name = "transactionsOfIssuer", description = "find pending Transactions ")
	@GraphQLNonNull
	public List<@GraphQLNonNull Transaction> transactionsOfIssuer(@GraphQLNonNull @GraphQLArgument(name = "issuer") String issuer) {
		return sRepo.transactionsOfIssuer(issuer)
				.map(t-> modelMapper.map(t, Transaction.class))
				.collect(Collectors.toList());
	}

	@Transactional(readOnly=true)
	@GraphQLQuery(name = "transactionsOfReceiver", description = "find Transactions of Receiver")
	@GraphQLNonNull
	public List<@GraphQLNonNull Transaction> transactionsOfReceiver(@GraphQLNonNull @GraphQLArgument(name = "receiver") String receiver) {
		return sRepo.transactionsOfReceiver(receiver)
				.map(t-> modelMapper.map(t, Transaction.class))
				.collect(Collectors.toList());
	}

	@Transactional(readOnly=true)
	@GraphQLQuery(name = "transactionByHash", description = "find Transactions ")
	public List<Transaction> transactionByHash(@GraphQLNonNull @GraphQLArgument(name = "hash") String hash) {
		return txRepository.findByTHash(hash).stream()
				.map(t-> modelMapper.map(t, Transaction.class))
				.collect(Collectors.toList());
	}

	@Transactional(readOnly=true)
	@GraphQLQuery(name = "sourcesOfPubkey", description = "find a wallet's sources  ")
	@GraphQLNonNull
	public List<@GraphQLNonNull Source> sourcesOfPubkey(@GraphQLNonNull @GraphQLArgument(name = "pub") String pub) {
		return sRepo.sourcesOfPubkey(pub).map(s-> modelMapper.map(s, Source.class)).collect(Collectors.toList());
	}


	// 						=============  Next comes the mutation =============

	@Transactional
	@GraphQLMutation(name = "submitTransaction", description = "post a transaction document")
	@GraphQLNonNull
	public Transaction submitTransaction(@GraphQLNonNull @GraphQLArgument(name = "raw") String raw) {
		LOG.info(" GVA - submitTransaction");
		return new Transaction();
	}


}