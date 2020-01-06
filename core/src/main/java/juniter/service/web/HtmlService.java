package juniter.service.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/html")
public class HtmlService {
	private static final Logger LOG = LogManager.getLogger(HtmlService.class);

	@GetMapping(value = "/tx")
	public String tx(Map<String, Object> model) {
		LOG.info("Entering /html/tx ...");
		model.put("time", new Date());
		model.put("message", "Welcome");
		model.put("title", "Welcome");
		return "pings";
	}

	@GetMapping("/greeting")
	public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Model model) {
		model.addAttribute("name", name);
		return "gvaws";
	}

}



