package juniter.service.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.Map;

@Controller
@RequestMapping("/html")
public class HtmlService {
	private static final Logger LOG = LogManager.getLogger(HtmlService.class);
	
//	@Autowired
//	private RequestMappingHandlerMapping handlerMapping;
//
//	@RequestMapping(value = {"","/"},  method = RequestMethod.GET)
//	public String home(Map<String, Object> model) {
//		LOG.info("Entering /html ...");
//		var mappings = handlerMapping.getHandlerMethods();
//		var paths = mappings.keySet()//
//				.stream()//
//				.flatMap(h -> h.getPatternsCondition().getPatterns().stream())//
//				.sorted() //
//				.collect(toList());
//
//		var tpath = paths.stream().collect(toMap(
//									k -> k.split("/")[1],
//									v -> Stream.of(v.substring(v.split("/")[1].length()+1)).filter(s-> s.length()>1).collect(toList()),
//									(v1,v2) -> Stream.concat(v1.stream(), v2.stream()).collect(toList()) ));
//		var treeMap = new TreeMap<String, List<String>>();
//		treeMap.putAll(tpath);
////		LOG.info("mappings "+ mappings);
////		LOG.info("paths " +  paths);
//		LOG.info("tpath " + tpath);
//		model.put("time", new Date());
//		model.put("mappings",treeMap);
//		model.put("message", "Welcome");
//		model.put("title", "Welcome");
//		return "home";
//	}

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
