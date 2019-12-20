package juniter.service.core;

import juniter.core.model.dbo.index.SINDEX;
import juniter.core.model.dbo.tx.Transaction;
import juniter.core.model.technical.Dividend;
import juniter.repository.jpa.block.TxRepository;
import juniter.repository.jpa.index.SINDEXRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TxRepository txRepository;

    @Autowired
    private SINDEXRepository sRepo;


    public List<Transaction> transactionsOfIssuer(String pubkey) {
        return txRepository.transactionsOfIssuer(pubkey);
    }

    public List<Transaction> transactionsOfReceiver(String pubkey) {
        return txRepository.transactionsOfReceiver(pubkey);
    }

    public List<Transaction> transactionsOfIssuerWindowedByBlock(String pubkey, Integer from, Integer to) {
        return txRepository.transactionsOfIssuerWindowedByBlock(pubkey, from, to);
    }

    public List<Transaction> transactionsOfReceiverWindowedByBlock(String pubkey, Integer from, Integer to) {
        return txRepository.transactionsOfReceiverWindowedByBlock(pubkey, from, to);
    }

    public List<Transaction> transactionsOfIssuerWindowedByTime(String pubkey, Long from, Long to) {
        return txRepository.transactionsOfIssuerWindowedByTime(pubkey, from, to);
    }

    public List<Transaction> transactionsOfReceiverWindowedByTime(String pubkey, Long from, Long to) {
        return txRepository.transactionsOfReceiverWindowedByTime(pubkey, from, to);
    }

    public List<SINDEX> sourcesOfPubkey(String pubkey) {
        return sRepo.sourcesOfPubkey(pubkey);
    }

    public List<Dividend> dividendsOf(String pubkey){
        return txRepository.dividendsOf(pubkey);
    }

    public List<Transaction> findByTHash(String hash){
        return txRepository.findByTHash(hash);
    }
}
