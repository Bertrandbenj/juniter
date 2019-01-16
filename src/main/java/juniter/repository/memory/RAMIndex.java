package juniter.repository.memory;

import com.google.common.collect.Lists;
import juniter.core.model.BStamp;
import juniter.core.model.DBBlock;
import juniter.core.validation.GlobalValid;
import juniter.repository.jpa.BlockRepository;
import juniter.repository.jpa.index.Account;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The simplest possible implementation of the GlobalValid interface in pure
 * java
 *
 * @author BnimajneB
 *
 */
@Service
public class RAMIndex implements GlobalValid, Serializable {


	private static final long serialVersionUID = 1321654987;

	private static final Logger LOG = LogManager.getLogger();



	private List<CINDEX> indexCG = Lists.newArrayList();
	private List<MINDEX> indexMG = Lists.newArrayList();
	private List<IINDEX> indexIG = Lists.newArrayList();
	private List<SINDEX> indexSG = Lists.newArrayList();


	@Autowired
	BlockRepository blockRepo;


	@Transactional(readOnly = true)
	@Override
	public Optional<DBBlock> createdOnBlock(BStamp bstamp) {
		return blockRepo.cachedBlock(bstamp.getNumber());
	}

	@Override
	public Optional<DBBlock> createdOnBlock(Integer number) {
		return blockRepo.cachedBlock(number);
	}


	@Override
	public boolean commit(BINDEX indexB, Set<IINDEX> indexI, Set<MINDEX> indexM, Set<CINDEX> indexC, Set<SINDEX> indexS,
						  List<IINDEX> consumeI, List<MINDEX> consumeM, List<CINDEX> consumeC, List<SINDEX> consumeS) {


		indexCG.addAll(indexC);
		indexMG.addAll(indexM);
		indexIG.addAll(indexI);
		indexSG.addAll(indexS);
		IndexB.add(indexB);


		LOG.info("Commit -  Certs: +" + indexC.size() + "," + indexCG.size() +
				"  Membship: +" + indexM.size() + "," + indexMG.size() +
				"  Idty: +" + indexI.size() + "," + indexIG.size() +
				"  localS: +" + indexS.size() + "," + indexSG.size() +
				"  IndexB: +" + indexB);

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

	@Override
	public Stream<Account> lowAccounts() {
		return null;
	}


	public String dumpToTxT() {
		return  "CINDEX\n"+ indexCG.stream().map(CINDEX::toString).collect(Collectors.joining("\n"))
				+ "\nIINDEX\n"+ indexIG.stream().map(IINDEX::toString).collect(Collectors.joining("\n"))
				+ "\nMINDEX\n"+ indexMG.stream().map(MINDEX::toString).collect(Collectors.joining("\n"))
				+ "\nSINDEX\n"+ indexSG.stream().map(SINDEX::toString).collect(Collectors.joining("\n")) ;
	}
}
