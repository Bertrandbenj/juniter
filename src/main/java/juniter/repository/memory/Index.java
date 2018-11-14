package juniter.repository.memory;

import com.google.common.collect.Lists;
import juniter.core.validation.GlobalValid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

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
	public boolean commit(List<BINDEX> indexB, Set<IINDEX> indexI, Set<MINDEX> indexM, Set<CINDEX> indexC,
						  Set<SINDEX> indexS) {

		indexCG.addAll(indexC);
		indexMG.addAll(indexM);
		indexIG.addAll(indexI);
		indexSG.addAll(indexS);
		LOG.info("Commited Certs: +" + indexC.size() + "," + indexCG.size() + //
				"  Membship: +" + indexM.size() + "," + indexMG.size() + //
				"  Idty: +" + indexI.size() + "," + indexIG.size() + //
				"  IndexS: +" + indexS.size() + "," + indexSG.size() + //
				"  IndexB: +" + indexB.size());

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
