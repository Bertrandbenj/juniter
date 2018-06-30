package juniter.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import juniter.model.net.wrappers.TxHistory;
import juniter.model.tx.Transaction;
import juniter.repository.TxInRepository;
import juniter.repository.TxRepository;

/**
 * Handles publication of transaction documents 
 * 
 * 
 * @author ben
 *
 */
@RestController
@RequestMapping("/blockchain")
public class TxService {
	
	private static final Logger logger = LogManager.getLogger();

	@Autowired
	private TxRepository repository;
	
	@Autowired
	private TxInRepository inRepo;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	void handle(HttpServletResponse response) throws IOException {
		response.sendRedirect("/html/tx");
	}
	
	@RequestMapping(value = "/tx/history/{pubkey}", method = RequestMethod.GET)
	public TxHistory history(@PathVariable("pubkey") String  pubkey) {
			
		List<Transaction> sent = repository
				.streamTransactionSentBy(pubkey)
				.collect(Collectors.toList());
		
		List<Transaction> received = new ArrayList<Transaction>();
		
		return new TxHistory(pubkey, sent, received);
		
	}


}
