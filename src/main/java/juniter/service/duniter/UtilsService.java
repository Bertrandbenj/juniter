package juniter.service.duniter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import juniter.crypto.SecretBox;
import juniter.repository.BlockRepository;

/**
 * 
 * Utils sub-root of the HTTP API 
 * 
 * <pre>
 * |-- utils/
 * |   |-- key
 * |   |-- with/
 * |       |-- newcomers
 *  </pre>
 * @author ben
 *
 */
@ConditionalOnExpression("${juniter.utils.enabled:false}")
@RestController
@RequestMapping("/utils")
public class UtilsService {

	private static final Logger logger = LogManager.getLogger();


	@Autowired
	BlockRepository repository;

	@RequestMapping("/")
	public String index() {
		return "<h1>Greetings to Juniter Utils</h1>" + "<ul><li>/utils/key/{secret}/{pass}</li><li>/utils/error</li></ul>";
	}

//	@RequestMapping(value = "/error")
//	public String toError() {
//		logger.info("Entering /blockchain/error");
//		return "redirect: /error.html";
//	}

	/**
	 * request : /key/{secret}/{pass}
	 * ex: /key/huhu/huhu  =>> 5vmjbDbjWZMYSNTi8sV3QrChAFdXj9Y7WZFAqzjJxmk6
	 * @param secret
	 * @param pass
	 * @return
	 */
	@RequestMapping(value = "/key/{secret}/{pass}", method = RequestMethod.GET)
	public String key(@PathVariable("secret") String secret, @PathVariable("pass") String pass ) {
		logger.info("Entering /key/{secret="+secret+"}/{pass="+pass+"}");
		SecretBox sb = new SecretBox(secret, pass);
		return "/key/huhu/huhu  =>> 5vmjbDbjWZMYSNTi8sV3QrChAFdXj9Y7WZFAqzjJxmk6<br>Result :"+ sb.getPublicKey();
	}


}
