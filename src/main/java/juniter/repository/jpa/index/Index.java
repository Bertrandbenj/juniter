package juniter.repository.jpa.index;

import juniter.core.model.BStamp;
import juniter.core.model.Block;
import juniter.core.validation.GlobalValid;
import juniter.repository.jpa.BlockRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
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
public class Index implements GlobalValid, Serializable {


	private static final long serialVersionUID = 1321654987;

	private static final Logger LOG = LogManager.getLogger();

	@Autowired
	CINDEXRepository cRepo ;

	@Autowired
	IINDEXRepository iRepo ;

	@Autowired
	MINDEXRepository mRepo ;

	@Autowired
	SINDEXRepository sRepo ;

	@Autowired
	BINDEXRepository bRepo ;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	BlockRepository blockRepo;



	public void init(boolean resetDB){
		if(resetDB){
			cRepo.deleteAll();
			iRepo.deleteAll();
			mRepo.deleteAll();
			sRepo.deleteAll();
			bRepo.deleteAll();
		}

		resetLocalIndex();
		IndexB.clear();
		IndexB.addAll(indexBGlobal().collect(Collectors.toList()));
		LOG.info(IndexB);

	}



	@Transactional(readOnly = true)
	@Override
	public Optional<Block> createdOnBlock(BStamp bstamp) {
		return blockRepo.cachedBlock(bstamp.getNumber());
	}

	@Override
	public Optional<Block> createdOnBlock(Integer number) {
		return blockRepo.cachedBlock(number);
	}

	@Override
	public boolean commit(BINDEX indexB, Set<IINDEX> indexI, Set<MINDEX> indexM, Set<CINDEX> indexC,
						  Set<SINDEX> indexS) {


		bRepo.save(modelMapper.map(indexB, juniter.repository.jpa.index.BINDEX.class));
		iRepo.saveAll(indexI.stream().map(i->modelMapper.map(i, juniter.repository.jpa.index.IINDEX.class)).collect(Collectors.toList()));
		iig.addAll(indexI);
		mRepo.saveAll(indexM.stream().map(i->modelMapper.map(i, juniter.repository.jpa.index.MINDEX.class)).collect(Collectors.toList()));
		cRepo.saveAll(indexC.stream().map(i->modelMapper.map(i, juniter.repository.jpa.index.CINDEX.class)).collect(Collectors.toList()));
		sRepo.saveAll(indexS.stream().map(i->modelMapper.map(i, juniter.repository.jpa.index.SINDEX.class)).collect(Collectors.toList()));


		LOG.info("Commit -  Certs: +" + indexC.size() + "," + indexCGlobal().count() + //
				"  Membship: +" + indexM.size() + "," + indexMGlobal().count() + //
				"  Idty: +" + indexI.size() + "," + indexIGlobal().count() + //
				"  IndexS: +" + indexS.size() + "," + indexSGlobal().count() + //
				"  IndexB: +" + indexB);

		return true;
	}



	public Stream<BINDEX> indexBGlobal() {
		return bRepo.findAll().stream().map(c-> modelMapper.map(c, BINDEX.class));
	}


	@Override
	public Stream<CINDEX> indexCGlobal() {
		return cRepo.findAll().stream().map(c-> modelMapper.map(c, CINDEX.class));
	}

	List<IINDEX> iig = new ArrayList<>();

	@Override
	public Stream<IINDEX> indexIGlobal() {
		return iRepo.findAll().stream().map(c-> modelMapper.map(c, IINDEX.class));//.collect(Collectors.toList()).stream();
	}

	@Override
	public Stream<MINDEX> indexMGlobal() {
		return mRepo.findAll().stream().map(c-> modelMapper.map(c, MINDEX.class));
	}

	@Override
	public Stream<SINDEX> indexSGlobal() {
		return sRepo.findAll().stream().map(c-> modelMapper.map(c, SINDEX.class));
	}


}
