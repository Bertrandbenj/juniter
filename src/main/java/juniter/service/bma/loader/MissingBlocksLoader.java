package juniter.service.bma.loader;

import juniter.core.utils.TimeUtils;
import juniter.repository.jpa.BlockRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
/**
 * <pre>
 * </pre>
 *
 * @author ben
 *
 */
@ConditionalOnExpression("${juniter.missingloader.enabled:false}")
@Component
@Order(3)
public class MissingBlocksLoader  {

	private static final Logger LOG = LogManager.getLogger();


	@Autowired
	BlockRepository blockRepo;

	List<Integer> blackList = List.of(15144, 31202, 85448, 87566, 90830, 109327);

	@Autowired
	BlockLoader defaultLoader;

	@Transactional(readOnly = true)
	public List<Integer> missingBlockNumbers() {
		final var currentNumber = defaultLoader.fetchBlock("current").getNumber();

		final var numbers = blockRepo.blockNumbers();

		if(currentNumber > blockRepo.currentBlockNumber()){
			return IntStream
					.range(0, currentNumber)
					.boxed()
					.filter(i -> !numbers.contains(i))
					.collect(Collectors.toList());

		}else{
			return new ArrayList<Integer>();
		}



	}


	@Scheduled(fixedRate = 5 * 60 * 1000 )
	public void run() {

		LOG.info("Entering MissingBlocksLoader.runPeerCheck  ");
		final var start = System.nanoTime();


		final var missing = missingBlockNumbers();
		LOG.info("found MissingBlocks : " + missing.size() + " - blocks " );

		missing.forEach(n -> {

			if(!blackList.contains(n)){
				LOG.info("  - doFetch for : " + n);
				defaultLoader.fetchAndSaveBlock(n);
			}

		});

		var elapsed = Long.divideUnsigned(System.nanoTime() - start, 1000000);
		LOG.info("Elapsed time: " + TimeUtils.format(elapsed));
	}


}
