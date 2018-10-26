package juniter.service.bma;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import juniter.core.model.tx.Transaction;
import juniter.repository.jpa.TxRepository;
import juniter.service.bma.model.TxHistory;

/**
 * Handles publication of transaction documents
 * 
 * 
 * @author ben
 *
 */
@RestController
@ConditionalOnExpression("${juniter.bma.enabled:false}")
@RequestMapping("/tx")
public class TxService {

	private static final Logger LOG = LogManager.getLogger();

	@Autowired
	private TxRepository repository;
	
	@Autowired
    private ModelMapper modelMapper; 

//	@Autowired
//	private TxInRepository inRepo;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	void handle(HttpServletResponse response) throws IOException {
		response.sendRedirect("/html/");
	}

	@Transactional(readOnly = true)
	@RequestMapping(value = "/history/{pubkey}", method = RequestMethod.GET)
	public TxHistory history(@PathVariable("pubkey") String pubkey) {
		// TODO: COMPLETE the history and tidy the result if need be to match the
		// duniter api exactly
		List<Transaction> sent = new ArrayList<Transaction>();
		List<Transaction> received = new ArrayList<Transaction>();
		List<Transaction> receiving = new ArrayList<Transaction>();
		List<Transaction> sending = new ArrayList<Transaction>();
		List<Transaction> pending = new ArrayList<Transaction>();

		try (var s = repository.streamTransactionSentBy(pubkey)) {
			sent.addAll(s.collect(Collectors.toList()));
		} catch (Exception e) {
			LOG.error("tx/history TransactionSentBy ", e);
		}

		try (var s = repository.streamTransactionReceivedBy(pubkey)) {
			received.addAll(s.collect(Collectors.toList()));
		} catch (Exception e) {
			LOG.error("tx/history TransactionReceivedBy ", e);
		}

		return new TxHistory(pubkey, sent, received, receiving, sending, pending);
	}

	@Transactional(readOnly = true)
	@RequestMapping(value = "/history/{pubkey}/pending", method = RequestMethod.GET)
	public TxHistory pendingHistory(@PathVariable("pubkey") String pubkey) {
		return null;
	}

	@RequestMapping(value = "/sources/{pubkey}", method = RequestMethod.GET)
	public String sources(@PathVariable("pubkey") String pubkey) {
		LOG.info("Entering /sources/{pubkey= " + pubkey+ "}");
		return "not implemented yet";
	}

	@RequestMapping(value = "/history/{pubkey}/blocks/{from}/{to}", method = RequestMethod.GET)
	public String historyFilterByBlockRange(@PathVariable("pubkey") String pubkey, @PathVariable("pubkey") String from,
			@PathVariable("pubkey") String to) {
		LOG.info("Entering /history/{pubkey}/blocks/{from}/{to}.. " + pubkey + " " + from + "->" + to);
		return "not implemented yet";
	}

	@RequestMapping(value = "/history/{pubkey}/times/{from}/{to}", method = RequestMethod.GET)
	public String historyFilterByTimeRange(@PathVariable("pubkey") String pubkey, @PathVariable("pubkey") String from,
			@PathVariable("pubkey") String to) {
		LOG.info("Entering /history/{pubkey}/times/{from}/{to}.. " + pubkey + " " + from + "->" + to);
		return "not implemented yet";
	}
}
