package juniter.service.bma;

import juniter.core.model.dto.tx.Source;
import juniter.core.model.dto.tx.TransactionDTO;
import juniter.core.model.dto.tx.TxHistory;
import juniter.core.model.dbo.index.SINDEX;
import juniter.core.model.dbo.tx.Transaction;
import juniter.repository.jpa.block.TxRepository;
import juniter.repository.jpa.index.SINDEXRepository;
import lombok.Data;
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
import org.springframework.web.bind.annotation.*;

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

    private static final Logger LOG = LogManager.getLogger(TxService.class);

    @Autowired
    private TxRepository repository;

    @Autowired
    private SINDEXRepository sRepo;


    @Autowired
    private ModelMapper modelMapper;

//	@Autowired
//	private TxInRepository inRepo;


    @Transactional(readOnly = true)
    @GetMapping(value = "/history/{pubkey}")
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
    @GetMapping(value = "/history/{pubkey}/pending" )
    public TxHistory pendingHistory(@PathVariable("pubkey") String pubkey) {
        LOG.info("Entering /history/{pubkey}/pending " + pubkey  );

        var sent = new ArrayList<TransactionDTO>();
        var received = new ArrayList<TransactionDTO>();
        var receiving = new ArrayList<TransactionDTO>();
        var sending = new ArrayList<TransactionDTO>();
        var pending = new ArrayList<TransactionDTO>();

        //TODO fill from sandboxes AND / OR branch getSize

        return new TxHistory(pubkey, sent, received, receiving, sending, pending);
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/history/{pubkey}/blocks/{from}/{to}" )
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
    @GetMapping(value = "/history/{pubkey}/times/{from}/{to}" )
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
    @GetMapping(value = "/sources/{pubkey}")
    public Wrapper sources(@PathVariable("pubkey") String pubkey) {
        LOG.info("Entering /sources/{pubkey= " + pubkey + "}");
        return new Wrapper(
                pubkey,
                sRepo.sourcesOfPubkey(pubkey).map(SINDEX::asSourceBMA).collect(Collectors.toList())
        );

    }


    @Data
    @NoArgsConstructor
    public class Wrapper   {

        String currency = "g1";
        String pubkey;
        List<Source> sources = new ArrayList<>();

        Wrapper(String pubkey, List<Source> sources) {
            this.pubkey = pubkey;
            this.sources = sources;
        }

    }


    @PostMapping(value = "/process")
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
