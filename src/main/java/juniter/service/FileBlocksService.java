package juniter.service;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import juniter.model.Block;

@Service
public class FileBlocksService {

	private static final Logger log = LogManager.getLogger();

	Block _0;

	Block _127128;

	@PostConstruct
	public void init() throws IOException {
		log.info("Entering FileBlocksService.init  ");

		ClassLoader cl = this.getClass().getClassLoader();
		ObjectMapper jsonMapper = new ObjectMapper();

		log.error(cl.resources("/blocks/")//
				.map(url -> url.toString()) //
				.collect(Collectors.toList()) + " " + cl.getResource("blocks/0.json"));
		
		try {
			_0 = jsonMapper.readValue(cl.getResourceAsStream("blocks/0.json"), Block.class);

			_127128 = jsonMapper.readValue(cl.getResourceAsStream("blocks/127128.json"), Block.class);

		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("Initialized " + this.getClass().getName() + " " + _0);
	}

}