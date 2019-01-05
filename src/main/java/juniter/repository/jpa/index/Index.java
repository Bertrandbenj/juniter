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
 */
@Service
public class Index implements GlobalValid, Serializable {


    private static final long serialVersionUID = 1321654987;

    private static final Logger LOG = LogManager.getLogger();

    @Autowired
    CINDEXRepository cRepo;

    @Autowired
    IINDEXRepository iRepo;

    @Autowired
    MINDEXRepository mRepo;

    @Autowired
    SINDEXRepository sRepo;

    @Autowired
    BINDEXRepository bRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    BlockRepository blockRepo;


    @Transactional
    public void init(boolean resetDB) {
        if (resetDB) {
            cRepo.deleteAll();
            iRepo.deleteAll();
            mRepo.deleteAll();
            sRepo.deleteAll();
            bRepo.deleteAll();
        }

        resetLocalIndex();
        IndexB.clear();
        IndexB.addAll(indexBGlobal().sorted().collect(Collectors.toList()));
        LOG.info("Initialized index B:" + IndexB + " " );

    }


    @Transactional(readOnly = true)
    @Override
    public Optional<Block> createdOnBlock(BStamp bstamp) {
        return blockRepo.block(bstamp.getNumber(), bstamp.getHash());
    }

    @Override
    public Optional<Block> createdOnBlock(Integer number) {
        return blockRepo.block(number);
    }

    @Transactional
    @Override
    public boolean commit(BINDEX indexB,
                          Set<IINDEX> indexI, Set<MINDEX> indexM, Set<CINDEX> indexC, Set<SINDEX> indexS,
                          List<IINDEX> consumeI, List<MINDEX> consumeM, List<CINDEX> consumeC, List<SINDEX> consumeS) {

       // Platform.runLater(() ->  Database.refresh(indexB,indexI,indexM,indexC,indexS,consumeI,consumeM,consumeC,consumeS) );

        if (consumeS.size() > 0)
            LOG.info("JACKPOT");

        iRepo.deleteAll(consumeI.stream()
                .map(i -> modelMapper.map(i, juniter.repository.jpa.index.IINDEX.class))
                .collect(Collectors.toList()));

        mRepo.deleteAll(consumeM.stream()
                .map(i -> modelMapper.map(i, juniter.repository.jpa.index.MINDEX.class))
                .collect(Collectors.toList()));

        cRepo.deleteAll(consumeC.stream()
                .map(i -> modelMapper.map(i, juniter.repository.jpa.index.CINDEX.class))
                .collect(Collectors.toList()));

        sRepo.deleteAll(consumeS.stream()
                .peek(s-> System.out.println("deleting " + s))
                .map(i -> modelMapper.map(i, juniter.repository.jpa.index.SINDEX.class))
                .peek(s-> System.out.println("or should i say  " + s))
                .collect(Collectors.toList()));

        //sRepo.flush();

        bRepo.save(modelMapper.map(indexB, juniter.repository.jpa.index.BINDEX.class));
        iRepo.saveAll(indexI.stream().map(i -> modelMapper.map(i, juniter.repository.jpa.index.IINDEX.class)).collect(Collectors.toList()));
        iig.addAll(indexI);
        mRepo.saveAll(indexM.stream().map(i -> modelMapper.map(i, juniter.repository.jpa.index.MINDEX.class)).collect(Collectors.toList()));
        cRepo.saveAll(indexC.stream().map(i -> modelMapper.map(i, juniter.repository.jpa.index.CINDEX.class)).collect(Collectors.toList()));
        sRepo.saveAll(indexS.stream()
                .map(i -> modelMapper.map(i, juniter.repository.jpa.index.SINDEX.class))
                .collect(Collectors.toList()));


        LOG.info("Commit -  Certs: +" + indexC.size() + ",-" + consumeC.size() + "," + cRepo.count() + //
                "  Membship: +" + indexM.size() + ",-" + consumeM.size() + "," + mRepo.count() + //
                "  Idty: +" + indexI.size() + ",-" + consumeI.size() + "," + iRepo.count() + //
                "  localS: +" + indexS.size() + ",-" + consumeS.size() + "," + sRepo.count() + //
                "  IndexB: " + bRepo.count() +", "+ indexB );

        return true;
    }


    public Stream<BINDEX> indexBGlobal() {
        return bRepo.findAll().stream().map(c -> modelMapper.map(c, BINDEX.class));
    }


    @Override
    public Stream<CINDEX> indexCGlobal() {
        return cRepo.findAll().stream().map(c -> modelMapper.map(c, CINDEX.class));
    }

    List<IINDEX> iig = new ArrayList<>();

    @Override
    public Stream<IINDEX> indexIGlobal() {
        return iRepo.findAll().stream().map(c -> modelMapper.map(c, IINDEX.class));//.collect(Collectors.toList()).stream();
    }

    @Override
    public Stream<CINDEX> reduceC(String issuer, String receiver) {
        if (issuer == null && receiver != null) {
            return cRepo.receivedBy(receiver).map(c -> modelMapper.map(c, CINDEX.class));
        }

        if (receiver == null && issuer != null) {
            return cRepo.issuedBy(issuer).map(c -> modelMapper.map(c, CINDEX.class));
        }

        return Stream.empty();
    }

    @Override
    public Stream<MINDEX> reduceM(String pub) {
        return mRepo.member(pub).map(c -> modelMapper.map(c, MINDEX.class));
    }

    @Transactional(readOnly = true)
    @Override
    public Stream<IINDEX> reduceI(String pub) {
        return iRepo.idtyByPubkey(pub).map(c -> modelMapper.map(c, IINDEX.class));
    }

    @Override
    public Stream<IINDEX> idtyByUid(String uid) {
        return iRepo.byUid(uid).map(c -> modelMapper.map(c, IINDEX.class));
    }

    @Override
    public Stream<SINDEX> reduceS(String conditions) {
        return sRepo.sourcesByConditions(conditions)
                .map(c -> modelMapper.map(c, SINDEX.class))
                .peek(s->System.out.println("mapped cond" + s))
                ;
    }

    @Override
    public Stream<SINDEX> reduceS(String identifier, Integer pos) {
        return sRepo.sourcesByIdentifierAndPos(identifier, pos)
                .map(c -> modelMapper.map(c, SINDEX.class))
                .peek(s->System.out.println("mapped i p " + s))
                ;
    }

    @Override
    public Stream<MINDEX> indexMGlobal() {
        return mRepo.findAll().stream().map(c -> modelMapper.map(c, MINDEX.class));
    }

    @Override
    public Stream<SINDEX> indexSGlobal() {
        return sRepo.sourceNotConsumed()
                .map(c -> modelMapper.map(c, SINDEX.class))
                .peek(s-> System.out.println("mapped all " + s));
    }

    @Override
    public Long trimIndexes() {
       Long bIndexSize = GlobalValid.super.trimIndexes();
       bRepo.trim(bIndexSize);
       return bIndexSize;
    }
}
