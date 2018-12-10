package juniter.repository;

import juniter.core.utils.TimeUtils;
import juniter.repository.jpa.BlockRepository;
import juniter.repository.memory.Index;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DecimalFormat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DaoTestConfiguration.class)
@DataJpaTest
public class TestRepoBlockchain {

	private static final Logger LOG = LogManager.getLogger();

	@Autowired
	public BlockRepository blockRepo;

	private Index idx = new Index();


	@Test
	public void test() {
		LOG.info("Testing the local repository ");

		final long time = System.currentTimeMillis();
		long delta = System.currentTimeMillis() - time;
		final var current = blockRepo.currentBlockNumber();
		final DecimalFormat decimalFormat = new DecimalFormat("##.###%");


		for (int i = 0; i < 52; i++) {

			final var block = blockRepo.block(i).get();

			if (idx.validate(block)) {
				LOG.info("Validated " + block);
			} else {
				LOG.warn("NOT Valid " + block.toDUP());
				return;
			}

			if (i > 0 && i % 100 == 0) {
				delta = System.currentTimeMillis() - time;

				final var perBlock = delta / i;
				final var estimate = current * perBlock;
				final String perc = decimalFormat.format(1.0 * i / current);

				LOG.info(perc + ", elapsed time " + TimeUtils.format(delta) + " which is " + perBlock
						+ " ms per block validated, estimating: " + TimeUtils.format(estimate) + " total");
			}
		}

		delta = System.currentTimeMillis() - time;
		LOG.info("Finished validation, took :  " + TimeUtils.format(delta));

	}


}
