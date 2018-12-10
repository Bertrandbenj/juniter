package juniter.repository.memory;

import com.google.common.collect.Lists;
import juniter.core.validation.GlobalValid;
import juniter.repository.jpa.CINDEXRepository;
import juniter.repository.jpa.IINDEXRepository;
import juniter.repository.jpa.MINDEXRepository;
import juniter.repository.jpa.SINDEXRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
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
	private ModelMapper modelMapper;


	private List<CINDEX> indexCG = Lists.newArrayList();
	private List<MINDEX> indexMG = Lists.newArrayList();
	private List<IINDEX> indexIG = Lists.newArrayList();
	private List<SINDEX> indexSG = Lists.newArrayList();



	public void reset(boolean db, boolean ram){
		if(db){
			cRepo.deleteAll();
			iRepo.deleteAll();
			mRepo.deleteAll();
			sRepo.deleteAll();
		}

		if(ram){
			indexIG.clear();
			indexMG.clear();
			indexCG.clear();
			indexSG.clear();
		}
	}

	@Override
	public boolean commit(List<BINDEX> indexB, Set<IINDEX> indexI, Set<MINDEX> indexM, Set<CINDEX> indexC,
						  Set<SINDEX> indexS) {

		indexCG.addAll(indexC);
		indexMG.addAll(indexM);
		indexIG.addAll(indexI);
		indexSG.addAll(indexS);
		ModelMapper modelMapper = new ModelMapper();

		//cRepo.save(modelMapper.map(indexC, juniter.core.model.index.CINDEX.class));

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

	public void load() {

		indexCG = cRepo.findAll().stream().map(c -> modelMapper.map(c, CINDEX.class)).collect(Collectors.toList());
		indexIG = iRepo.findAll().stream().map(c -> modelMapper.map(c, IINDEX.class)).collect(Collectors.toList());
		indexMG = mRepo.findAll().stream().map(c -> modelMapper.map(c, MINDEX.class)).collect(Collectors.toList());
		indexSG = sRepo.findAll().stream().map(c -> modelMapper.map(c, SINDEX.class)).collect(Collectors.toList());
	}

	public void dump() {
		reset(true, false);
		LOG.info("DUMPING : " );

		cRepo.saveAll(indexCG.stream().map(c-> modelMapper.map(c, juniter.core.model.index.CINDEX.class)).collect(Collectors.toList()));
		mRepo.saveAll(indexMG.stream().map(c-> modelMapper.map(c, juniter.core.model.index.MINDEX.class)).collect(Collectors.toList()));
		iRepo.saveAll(indexIG.stream().map(c-> modelMapper.map(c, juniter.core.model.index.IINDEX.class)).collect(Collectors.toList()));
		sRepo.saveAll(indexSG.stream().map(c-> modelMapper.map(c, juniter.core.model.index.SINDEX.class)).collect(Collectors.toList()));
	}

	public String dumpToTxT() {
		return  "CINDEX\n"+ indexCG.stream().map(CINDEX::toString).collect(Collectors.joining("\n"))
				+ "\nIINDEX\n"+ indexIG.stream().map(IINDEX::toString).collect(Collectors.joining("\n"))
				+ "\nMINDEX\n"+ indexMG.stream().map(MINDEX::toString).collect(Collectors.joining("\n"))
				+ "\nSINDEX\n"+ indexSG.stream().map(SINDEX::toString).collect(Collectors.joining("\n")) ;

	}
}
