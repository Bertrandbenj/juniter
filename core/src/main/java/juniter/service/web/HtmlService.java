package juniter.service.web;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Controller
@RequestMapping("/html")
public class HtmlService {
	private static final Logger LOG = LogManager.getLogger();
	
	@Autowired
	private RequestMappingHandlerMapping handlerMapping;

	@RequestMapping(value = {"","/"},  method = RequestMethod.GET)
	public String home(Map<String, Object> model) {
		LOG.info("Entering /html ...");
		var mappings = handlerMapping.getHandlerMethods();
		var paths = mappings.keySet()//
				.stream()//
				.flatMap(h -> h.getPatternsCondition().getPatterns().stream())//
				.sorted() // 
				.collect(toList());
	
		var tpath = paths.stream().collect(toMap(
									k -> k.split("/")[1],
									v -> Stream.of(v.substring(v.split("/")[1].length()+1)).filter(s-> s.length()>1).collect(toList()),
									(v1,v2) -> Stream.concat(v1.stream(), v2.stream()).collect(toList()) ));
		var treeMap = new TreeMap<String, List<String>>();
		treeMap.putAll(tpath);
//		LOG.info("mappings "+ mappings);
//		LOG.info("paths " +  paths);
		LOG.info("tpath " + tpath);
		model.put("time", new Date());
		model.put("mappings",treeMap);
		model.put("message", "Welcome");
		model.put("title", "Welcome");
		return "home";
	}

	@RequestMapping(value = "/tx",  method = RequestMethod.GET)
	public String tx(Map<String, Object> model) {
		LOG.info("Entering /html/tx ...");
		model.put("time", new Date());
		model.put("message", "Welcome");
		model.put("title", "Welcome");
		return "pings";
	}

}
