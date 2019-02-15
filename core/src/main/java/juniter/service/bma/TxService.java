package juniter.service.bma;

import juniter.core.model.dto.Source;
import juniter.core.model.dto.TransactionDTO;
import juniter.core.model.dto.TxHistory;
import juniter.core.model.index.SINDEX;
import juniter.core.model.business.tx.Transaction;
import juniter.repository.jpa.TxRepository;
import juniter.repository.jpa.index.SINDEXRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Handles publication of transaction documents
 *
 * @author ben
 */
@RestController
@ConditionalOnExpression("${juniter.useBMA:false}")
@RequestMapping("/tx")
public class TxService {

    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    private TxRepository repository;

    @Autowired
    private SINDEXRepository sRepo;


    @Autowired
    private ModelMapper modelMapper;

//	@Autowired
//	private TxInRepository inRepo;


    @Transactional(readOnly = true)
    @RequestMapping(value = "/history/{pubkey}", method = RequestMethod.GET)
    public TxHistory history(@PathVariable("pubkey") String pubkey) {
        // TODO: COMPLETE the history and tidy the result if need be to match the duniter api exactly
        var sent = new ArrayList<TransactionDTO>();
        var received = new ArrayList<TransactionDTO>();
        var receiving = new ArrayList<TransactionDTO>();
        var sending = new ArrayList<TransactionDTO>();
        var pending = new ArrayList<TransactionDTO>();

        try (var s = repository.transactionsOfIssuer(pubkey)) {
            sent.addAll(s.map(tx -> modelMapper.map(tx, TransactionDTO.class)).collect(Collectors.toList()));
        } catch (Exception e) {
            LOG.error("tx/history TransactionSentBy ", e);
        }

        try (var s = repository.transactionsOfReceiver(pubkey)) {
            received.addAll(s.map(tx -> modelMapper.map(tx, TransactionDTO.class)).collect(Collectors.toList()));
        } catch (Exception e) {
            LOG.error("tx/history TransactionReceivedBy ", e);
        }

        return new TxHistory(pubkey, sent, received, receiving, sending, pending);
    }

    @Transactional(readOnly = true)
    @RequestMapping(value = "/history/{pubkey}/pending", method = RequestMethod.GET)
    public TxHistory pendingHistory(@PathVariable("pubkey") String pubkey) {
        LOG.info("Entering /history/{pubkey}/pending " + pubkey  );

        var sent = new ArrayList<TransactionDTO>();
        var received = new ArrayList<TransactionDTO>();
        var receiving = new ArrayList<TransactionDTO>();
        var sending = new ArrayList<TransactionDTO>();
        var pending = new ArrayList<TransactionDTO>();

        //TODO fill from sandboxes AND / OR branch size

        return new TxHistory(pubkey, sent, received, receiving, sending, pending);
    }

    @Transactional(readOnly = true)
    @RequestMapping(value = "/history/{pubkey}/blocks/{from}/{to}", method = RequestMethod.GET)
    public TxHistory historyFilterByBlockRange(@PathVariable("pubkey") String pubkey,
                                            @PathVariable("pubkey") String from,
                                            @PathVariable("pubkey") String to) {
        LOG.info("Entering /history/{pubkey}/blocks/{from}/{to}.. " + pubkey + " " + from + "->" + to);

        var sent = new ArrayList<TransactionDTO>();
        var received = new ArrayList<TransactionDTO>();
        var receiving = new ArrayList<TransactionDTO>();
        var sending = new ArrayList<TransactionDTO>();
        var pending = new ArrayList<TransactionDTO>();

        try (var s = repository.transactionsOfIssuerWindowedByBlock(pubkey, from, to)) {
            sent.addAll(s.map(tx -> modelMapper.map(tx, TransactionDTO.class)).collect(Collectors.toList()));
        } catch (Exception e) {
            LOG.error("tx/history TransactionSentBy ", e);
        }

        try (var s = repository.transactionsOfReceiverWindowedByBlock(pubkey, from, to)) {
            received.addAll(s.map(tx -> modelMapper.map(tx, TransactionDTO.class)).collect(Collectors.toList()));
        } catch (Exception e) {
            LOG.error("tx/history TransactionReceivedBy ", e);
        }

        return new TxHistory(pubkey, sent, received, receiving, sending, pending);
    }


    @Transactional(readOnly = true)
    @RequestMapping(value = "/history/{pubkey}/times/{from}/{to}", method = RequestMethod.GET)
    public TxHistory historyFilterByTimeRange(@PathVariable("pubkey") String pubkey, @PathVariable("pubkey") String from,
                                           @PathVariable("pubkey") String to) {
        LOG.info("Entering /history/{pubkey}/times/{from}/{to}.. " + pubkey + " " + from + "->" + to);

        var sent = new ArrayList<TransactionDTO>();
        var received = new ArrayList<TransactionDTO>();
        var receiving = new ArrayList<TransactionDTO>();
        var sending = new ArrayList<TransactionDTO>();
        var pending = new ArrayList<TransactionDTO>();

        try (var s = repository.transactionsOfIssuerWindowedByTime(pubkey, from, to)) {
            sent.addAll(s.map(tx -> modelMapper.map(tx, TransactionDTO.class)).collect(Collectors.toList()));
        } catch (Exception e) {
            LOG.error("tx/history TransactionSentBy ", e);
        }

        try (var s = repository.transactionsOfReceiverWindowedByTime(pubkey, from, to)) {
            received.addAll(s.map(tx -> modelMapper.map(tx, TransactionDTO.class)).collect(Collectors.toList()));
        } catch (Exception e) {
            LOG.error("tx/history TransactionReceivedBy ", e);
        }

        return new TxHistory(pubkey, sent, received, receiving, sending, pending);
    }


    @Transactional(readOnly = true)
    @RequestMapping(value = "/sources/{pubkey}", method = RequestMethod.GET)
    public Wrapper sources(@PathVariable("pubkey") String pubkey) {
        LOG.info("Entering /sources/{pubkey= " + pubkey + "}");
        return new Wrapper(
                pubkey,
                sRepo.sourcesOfPubkey(pubkey).map(SINDEX::asSourceBMA).collect(Collectors.toList())
        );

    }


    @Getter
    @Setter
    @NoArgsConstructor
    public class Wrapper implements Serializable {

        private static final long serialVersionUID = 7842838617478484444L;

        String currency = "g1";
        String pubkey;
        List<Source> sources = new ArrayList<>();

        Wrapper(String pubkey, List<Source> sources) {
            this.pubkey = pubkey;
            this.sources = sources;
        }

    }


    @RequestMapping(value = "/process", method = RequestMethod.POST)
    ResponseEntity<Transaction> processTx(HttpServletRequest request, HttpServletResponse response) {

        LOG.info("POSTING /tx/process ..." + request.getRemoteHost());

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            LOG.info(in.lines().collect(Collectors.joining("\n")));
        } catch (Exception e) {
            LOG.error("error reading network/peering/peers inputStream ", e);
        }


        var tx = new Transaction();
        final var headers = new HttpHeaders();


        return new ResponseEntity<>(tx, headers, HttpStatus.OK);
    }

}
