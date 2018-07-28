package juniter.service.http;

import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/html")
public class HtmlService {
	private static final Logger logger = LogManager.getLogger();

	@RequestMapping(value = {"/","/error"},  method = RequestMethod.GET)
	public String home(Map<String, Object> model) {
		logger.info("Entering /html/ ...");
		model.put("time", new Date());
		model.put("message", "Welcome");
		model.put("title", "Welcome");
		return "home";
	}

	@RequestMapping(value = "/tx",  method = RequestMethod.GET)
	public String tx(Map<String, Object> model) {
		logger.info("Entering /html/tx ...");
		model.put("time", new Date());
		model.put("message", "Welcome");
		model.put("title", "Welcome");
		return "huhu";
	}

}
