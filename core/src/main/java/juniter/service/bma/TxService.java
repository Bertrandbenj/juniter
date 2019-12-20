package juniter.service.bma;

import juniter.core.model.dbo.index.SINDEX;
import juniter.core.model.dbo.sandbox.TransactionSandboxed;
import juniter.core.model.dbo.tx.Transaction;
import juniter.core.model.dto.tx.History;
import juniter.core.model.dto.tx.Source;
import juniter.core.model.dto.tx.TransactionDTO;
import juniter.core.model.dto.tx.TxHistory;
import juniter.service.core.Sandboxes;
import juniter.service.core.TransactionService;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private TransactionService txService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private Sandboxes sandboxes;


    @Transactional(readOnly = true)
    @GetMapping(value = "/history/{pubkey}")
    public TxHistory history(@PathVariable("pubkey") String pubkey) {
        // TODO: COMPLETE the history and tidy the result if need be to match the duniter api exactly
        var sent = txService
                .transactionsOfIssuer(pubkey).stream()
                .map(tx -> modelMapper.map(tx, TransactionDTO.class))
                .collect(Collectors.toList());

        var received = txService.transactionsOfReceiver(pubkey).stream()
                .map(tx -> modelMapper.map(tx, TransactionDTO.class))
                .collect(Collectors.toList());

        var receiving = new ArrayList<TransactionDTO>();
        var sending = new ArrayList<TransactionDTO>();
        var pending = new ArrayList<TransactionDTO>();


        return TxHistory.builder()
                .currency("g1")
                .pubkey(pubkey)
                .history(History.builder()
                        .pending(pending)
                        .received(received)
                        .receiving(receiving)
                        .sending(sending)
                        .sent(sent)
                        .build())
                .build();
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/history/{pubkey}/pending")
    public TxHistory pendingHistory(@PathVariable("pubkey") String pubkey) {
        LOG.info("Entering /history/{pubkey}/pending " + pubkey);

        var sent = new ArrayList<TransactionDTO>();
        var received = new ArrayList<TransactionDTO>();
        var receiving = new ArrayList<TransactionDTO>();
        var sending = new ArrayList<TransactionDTO>();
        var pending = sandboxes.getPendingTransactions().stream()
                .map(tx -> modelMapper.map(tx, TransactionDTO.class))
                .collect(Collectors.toList());

        return TxHistory.builder()
                .currency("g1")
                .pubkey(pubkey)
                .history(History.builder()
                        .pending(pending)
                        .received(received)
                        .receiving(receiving)
                        .sending(sending)
                        .sent(sent)
                        .build())
                .build();
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/history/{pubkey}/blocks/{from}/{to}")
    public TxHistory historyFilterByBlockRange(@PathVariable("pubkey") String pubkey,
                                               @PathVariable("from") String from,
                                               @PathVariable("to") String to) {
        LOG.info("Entering /history/{pubkey}/blocks/{from}/{to}.. " + pubkey + " " + from + "->" + to);

        var sent = txService.transactionsOfIssuerWindowedByBlock(pubkey, Integer.parseInt(from), Integer.parseInt(to))
                .stream()
                .map(tx -> modelMapper.map(tx, TransactionDTO.class))
                .collect(Collectors.toList());
        var received = txService.transactionsOfReceiverWindowedByBlock(pubkey, Integer.parseInt(from), Integer.parseInt(to))
                .stream().map(tx -> modelMapper.map(tx, TransactionDTO.class)).collect(Collectors.toList());
        var receiving = new ArrayList<TransactionDTO>();
        var sending = new ArrayList<TransactionDTO>();
        var pending = new ArrayList<TransactionDTO>();
        return TxHistory.builder()
                .currency("g1")
                .pubkey(pubkey)
                .history(History.builder()
                        .pending(pending)
                        .received(received)
                        .receiving(receiving)
                        .sending(sending)
                        .sent(sent)
                        .build())
                .build();
    }


    @Transactional(readOnly = true)
    @GetMapping(value = "/history/{pubkey}/times/{from}/{to}")
    public TxHistory historyFilterByTimeRange(@PathVariable("pubkey") String pubkey,
                                              @PathVariable("from") String from,
                                              @PathVariable("to") String to) {
        LOG.info("Entering /history/{pubkey}/times/{from}/{to}.. " + pubkey + " " + from + "->" + to);

        var sent = txService.transactionsOfIssuerWindowedByTime(pubkey, Long.parseLong(from), Long.parseLong(to)).stream()
                .map(tx -> modelMapper.map(tx, TransactionDTO.class))
                .collect(Collectors.toList());
        var received = txService.transactionsOfReceiver(pubkey).stream() //txService.transactionsOfReceiverWindowedByTime(pubkey, Long.parseLong(from), Long.parseLong(to)).stream()
                .map(tx -> {
                    var res = modelMapper.map(tx, TransactionDTO.class);
                    res.setBlockstampTime(tx.getWritten().getMedianTime());
                    return res;
                })
                .collect(Collectors.toList());

        var receiving = new ArrayList<TransactionDTO>();
        var sending = new ArrayList<TransactionDTO>();
        var pending = new ArrayList<TransactionDTO>();

        return new TxHistory("g1", pubkey, new History(sent, received, receiving, sending, pending));
    }


    @Transactional(readOnly = true)
    @GetMapping(value = "/sources/{pubkey}")
    public Wrapper sources(@PathVariable("pubkey") String pubkey) {
        LOG.info("Entering /sources/{pubkey= " + pubkey + "}");
        return new Wrapper(
                pubkey,
                txService.sourcesOfPubkey(pubkey).stream().map(SINDEX::asSourceBMA).collect(Collectors.toList())
        );

    }


    @Data
    @NoArgsConstructor
    public class Wrapper {

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


            sandboxes.put(new TransactionSandboxed());
        } catch (Exception e) {
            LOG.error("error reading network/peering/peers inputStream ", e);
        }


        var tx = new Transaction();
        final var headers = new HttpHeaders();

        return new ResponseEntity<>(tx, headers, HttpStatus.OK);
    }

}
