package juniter.service.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import juniter.model.Block;
import juniter.model.bma.WithWrapper;
import juniter.service.rest.BlockchainService;
import juniter.utils.Constants;

import static java.util.stream.Collectors.toList;

@ConditionalOnExpression("${juniter.bma.enabled:false} && ${juniter.simpleloader.enabled:false}")
@Component
public class SimpleLoader implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(SimpleLoader.class);

	@Autowired
	BlockchainService blockchainService;
	
	private  RestTemplate restTemplate = new RestTemplate();

	@Override
	public void run(String... args) throws Exception {
		var start = System.nanoTime();

		fetchUsefullBlocks().stream().map(i -> blockchainService.block(i)).collect(toList());

		var elapsed = Long.divideUnsigned(System.nanoTime() - start, 1000000);
		logger.info("Elapsed time: " + elapsed + "ms");
	}

	@Transactional
	private List<Integer> fetchUsefullBlocks() {
		String url = Constants.Defaults.NODE  + "blockchain/with/";
		logger.info("Loading from : " + url);
		List<Integer> res =  new ArrayList<Integer>();
		try {

			var ww = restTemplate.getForObject(url + "tx", WithWrapper.class);
			logger.info(" - Fetching Tx List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());
			
			ww = restTemplate.getForObject(url + "certs", WithWrapper.class);
			logger.info(" - Fetching Certs List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());
			
			ww = restTemplate.getForObject(url+ "newcomers", WithWrapper.class);
			logger.info(" - Fetching NewComers List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());
			
			ww = restTemplate.getForObject(url+ "leavers", WithWrapper.class);
			logger.info(" - Fetching Leavers List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());
			
			ww = restTemplate.getForObject(url+ "revoked", WithWrapper.class);
			logger.info(" - Fetching Revoked List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());
			
			ww = restTemplate.getForObject(url+ "excluded", WithWrapper.class);
			logger.info(" - Fetching Excluded List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());
			
			ww = restTemplate.getForObject(url+ "actives", WithWrapper.class);
			logger.info(" - Fetching Actives List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());
			
			ww = restTemplate.getForObject(url+ "ud", WithWrapper.class);
			logger.info(" - Fetching UD List : " + ww.getResult().getBlocks().size());
			res.addAll(ww.getResult().getBlocks());
			
			return res.stream().distinct().sorted().collect(toList());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ArrayList<Integer>();
	}

}
