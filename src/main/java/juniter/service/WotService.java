package juniter.service;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import juniter.repository.BlockRepository;

@RestController
@RequestMapping("/wot")
public class WotService {
	private static final Logger logger = LogManager.getLogger();

	@Autowired
	private BlockRepository blockRepo;

//	@Autowired
//	private TxInRepository inRepo;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	void handle(HttpServletResponse response) throws IOException {
		response.sendRedirect("/html/");
	}
	
	@RequestMapping(value = "/wot/requirements/{pubkey}", method = RequestMethod.GET)
	public String requirements(@PathVariable("pubkey") String pubkeyOrUid) {
		logger.info("Entering /requirements/{pubkey= " + pubkeyOrUid+ "}");
		return "not implemented yet";
	}

	
	@RequestMapping(value = "/wot/certifier-of/{pubkeyOrUid}", method = RequestMethod.GET)
	public String certifierOf(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {
		logger.info("Entering /certifier-of/{pubkeyOrUid= " + pubkeyOrUid+ "}");
		return "not implemented yet";
	}

	
	@RequestMapping(value = "/certified-by/{pubkeyOrUid}", method = RequestMethod.GET)
	public String certifiedBy(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {
		logger.info("Entering /certified-by/{pubkeyOrUid= " + pubkeyOrUid+ "}");
		return "not implemented yet";
	}
	
	@RequestMapping(value = "/identity-of/{pubkeyOrUid}", method = RequestMethod.GET)
	public String identityOf(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {
		logger.info("Entering /identity-of/{pubkeyOrUid= " + pubkeyOrUid+ "}");
		return "not implemented yet";
	}


	@RequestMapping(value = "/lookup/{pubkeyOrUid}", method = RequestMethod.GET)
	public String lookup(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {
		logger.info("Entering /wot/lookup/{pubkeyOrUid= " + pubkeyOrUid+ "}");
		return "not implemented yet";
	}
	
	@RequestMapping(value = "/members", method = RequestMethod.GET)
	public String members() {
		logger.info("Entering /wot/members");
		return "not implemented yet";
	}
}
