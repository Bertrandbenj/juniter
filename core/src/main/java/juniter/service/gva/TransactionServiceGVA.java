package juniter.service.gva;

import com.google.common.collect.Lists;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import juniter.core.model.dbo.index.SINDEX;
import juniter.core.model.gva.Source;
import juniter.core.model.gva.Transaction;
import juniter.service.jpa.TransactionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceGVA {

    private static final Logger LOG = LogManager.getLogger(TransactionServiceGVA.class);

    @Autowired
    private TransactionService txService;

    @Autowired
    private ModelMapper modelMapper;


    @Transactional(readOnly = true)
    @GraphQLQuery(name = "pendingTransactions", description = "find pending Transactions ")
    @GraphQLNonNull
    public List<@GraphQLNonNull Transaction> pendingTransactions() {
        return Lists.newArrayList();
    }

    @Transactional(readOnly = true)
    @GraphQLQuery(name = "transactionsOfIssuer", description = "find pending Transactions ")
    @GraphQLNonNull
    public List<@GraphQLNonNull Transaction> transactionsOfIssuer(@GraphQLNonNull @GraphQLArgument(name = "issuer") String issuer) {
        return txService.transactionsOfIssuer(issuer).stream()
                .map(t -> modelMapper.map(t, Transaction.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @GraphQLQuery(name = "transactionsOfReceiver", description = "find Transactions of Receiver")
    @GraphQLNonNull
    public List<@GraphQLNonNull Transaction> transactionsOfReceiver(@GraphQLNonNull @GraphQLArgument(name = "receiver") String receiver) {
        return txService.transactionsOfReceiver(receiver).stream()
                .map(t -> modelMapper.map(t, Transaction.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @GraphQLQuery(name = "transactionByHash", description = "find Transactions ")
    public List<Transaction> transactionByHash(@GraphQLNonNull @GraphQLArgument(name = "hash") String hash) {
        LOG.info("transactionByHash " + hash + " " + txService.findByTHash(hash).size());
        return txService.findByTHash(hash).stream()
                .map(t -> modelMapper.map(t, Transaction.class))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @GraphQLQuery(name = "sourcesOfPubkey", description = "find a wallet's sources  ")
    @GraphQLNonNull
    public List<@GraphQLNonNull Source> sourcesOfPubkey(@GraphQLNonNull @GraphQLArgument(name = "pub") String pub) {
        return txService.sourcesOfPubkey(pub).stream().map(SINDEX::asSourceGVA).collect(Collectors.toList());
    }


    // 						=============  Next comes the mutation =============

    @Transactional
    @GraphQLMutation(name = "submitTransaction", description = "post a transaction document")
    @GraphQLNonNull
    public Transaction submitTransaction(@GraphQLNonNull @GraphQLArgument(name = "rawDocument") String raw) {
        LOG.info(" GVA - submitTransaction");
        return new Transaction();
    }


}