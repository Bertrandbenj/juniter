package juniter.service.bma;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import juniter.core.model.wot.Certification;
import juniter.repository.jpa.CertsRepository;

@RestController
@ConditionalOnExpression("${juniter.bma.enabled:false}")
@RequestMapping("/wot")
public class WotService {
	private static final Logger logger = LogManager.getLogger();

	@Autowired
	private CertsRepository wotRepo;

//	@Autowired
//	private TxInRepository inRepo;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	void handle(HttpServletResponse response) throws IOException {
		response.sendRedirect("/html/");
	}
	
	@RequestMapping(value = "/requirements/{pubkey}", method = RequestMethod.GET)
	public String requirements(@PathVariable("pubkey") String pubkeyOrUid) {
		logger.info("Entering /wot/requirements/{pubkey= " + pubkeyOrUid+ "}");
		return "not implemented yet";
	}

	@Transactional(readOnly=true)
	@RequestMapping(value = "/certifiers-of/{pubkeyOrUid}", method = RequestMethod.GET)
	public List<Certification> certifiersOf(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {
		logger.info("Entering /wot/certifiers-of/{pubkeyOrUid= " + pubkeyOrUid+ "}");
		
		return wotRepo.streamCertifiersOf(pubkeyOrUid).collect(Collectors.toList());
	}

	@Transactional(readOnly=true)
	@RequestMapping(value = "/certified-by/{pubkeyOrUid}", method = RequestMethod.GET)
	public List<Certification> certifiedBy(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {
		logger.info("Entering /wot/certified-by/{pubkeyOrUid= " + pubkeyOrUid+ "}");
		return  wotRepo.streamCertifiedBy(pubkeyOrUid).collect(Collectors.toList());
	}
	
	@RequestMapping(value = "/identity-of/{pubkeyOrUid}", method = RequestMethod.GET)
	public String identityOf(@PathVariable("pubkeyOrUid") String pubkeyOrUid) {
		logger.info("Entering /wot/identity-of/{pubkeyOrUid= " + pubkeyOrUid+ "}");
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
