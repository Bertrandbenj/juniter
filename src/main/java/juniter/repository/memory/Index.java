package juniter.repository.memory;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.Lists;

import juniter.core.validation.GlobalValid;

/**
 * The simplest possible implementation of the GlobalValid interface in pure
 * java
 *
 * @author BnimajneB
 *
 */
public class Index implements GlobalValid {

	private static final Logger LOG = LogManager.getLogger();


	private List<CINDEX> indexCG = Lists.newArrayList();
	private List<MINDEX> indexMG = Lists.newArrayList();
	private List<IINDEX> indexIG = Lists.newArrayList();
	private List<SINDEX> indexSG = Lists.newArrayList();

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
