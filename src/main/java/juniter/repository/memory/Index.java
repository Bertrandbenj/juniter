package juniter.repository.memory;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.common.collect.Lists;

import juniter.core.validation.GlobalValid;
import juniter.repository.jpa.BlockRepository;

public class Index implements GlobalValid {

	private static final Logger LOG = LogManager.getLogger();


	private List<CINDEX> indexCG = Lists.newArrayList();
	private List<MINDEX> indexMG = Lists.newArrayList();
	private List<IINDEX> indexIG = Lists.newArrayList();
	private List<SINDEX> indexSG = Lists.newArrayList();

	@Autowired
	private BlockRepository blockRepo;

	@Override
	public boolean commit(List<BINDEX> indexb, Set<IINDEX> indexi, Set<MINDEX> indexm, Set<CINDEX> indexc,
			Set<SINDEX> indexs) {

		indexCG.addAll(indexc);
		indexMG.addAll(indexm);
		indexIG.addAll(indexi);
		indexSG.addAll(indexs);
		LOG.info("Commited Certs: +" + indexc.size() + "," + indexCG.size() + //
				"  Membship: +" + indexm.size() + "," + indexMG.size() + //
				"  Idty: +" + indexi.size() + "," + indexIG.size() + //
				"  IndexS: +" + indexs.size() + "," + indexSG.size() + //
				"  IndexB: +" + indexb.size());

		return true;
	}

	public void doIt() throws JsonParseException, JsonMappingException, IOException {
		final long time = System.currentTimeMillis();
		long delta = System.currentTimeMillis() - time;
		final var current = blockRepo.currentBlockNumber();
		final DecimalFormat decimalFormat = new DecimalFormat("##.###%");


		for (int i = 0; i < 52; i++) {

			final var block = blockRepo.block(i).get();

			if (validate(block)) {
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

				LOG.info(perc + ", elapsed time " + format(delta) + " which is " + perBlock
						+ " ms per block validated, estimating: " + format(estimate) + " total");
			}
		}

		delta = System.currentTimeMillis() - time;
		LOG.info("Finished validation, took :  " + format(delta));

	}

	private String format(long millis) {
		return String.format("%d min, %d sec - %d ms", //
				TimeUnit.MILLISECONDS.toMinutes(millis), //
				TimeUnit.MILLISECONDS.toSeconds(millis)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
				millis);
	}

	@Override
	public Stream<CINDEX> indexCGlobal() {
		return indexCG.stream();
	}

	@Override
	public Stream<IINDEX> indexIGlobal() {
		return indexIG.stream();
	}

	@Override
	public Stream<MINDEX> indexMGlobal() {
		return indexMG.stream();
	}

	@Override
	public Stream<SINDEX> indexSGlobal() {
		return indexSG.stream();
	}

}
