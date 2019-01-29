package juniter.service;

import com.codahale.metrics.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import javafx.application.Platform;
import juniter.core.model.BStamp;
import juniter.core.model.DBBlock;
import juniter.core.model.index.Account;
import juniter.core.utils.TimeUtils;
import juniter.core.validation.GlobalValid;
import juniter.repository.jpa.BlockRepository;
import juniter.repository.jpa.index.*;
import juniter.service.adminfx.include.Bus;
import juniter.service.bma.loader.BlockLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
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
    private CINDEXRepository cRepo;

    @Autowired
    private IINDEXRepository iRepo;

    @Autowired
    private MINDEXRepository mRepo;

    @Autowired
    private SINDEXRepository sRepo;

    @Autowired
    private BINDEXRepository bRepo;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BlockRepository blockRepo;


    @Autowired
    private BlockLoader blockLoader;


    @Transactional
    @Counted(absolute = true)
    void init(boolean resetDB) {
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
        if (head().isPresent()) {
            BR_G11_setUdTime(head().get());
            BR_G12_setUnitBase(head().get());
            BR_G13_setDividend(head().get());
            BR_G04_setIssuersCount(head().get());
        }
//        if(head_1()!=null){
//            resetDividend(head_1() );
//        }
//        if(head().isPresent()){
//            resetDividend(head().get());
//        }

        LOG.info("Initialized index B: " + IndexB.size() + " ");

    }


    @Transactional(readOnly = true)
    @Override
    public Optional<DBBlock> createdOnBlock(BStamp bstamp) {

        if (bstamp.getNumber().equals(0))
            return blockRepo.block(0);
        return Optional.of(blockRepo.cachedBlock(bstamp)).or(() -> Optional.of(blockLoader.fetchBlock("block/" + bstamp.getNumber())));
    }

    @Override
    public Optional<DBBlock> createdOnBlock(Integer number) {
        return blockRepo.block(number);
    }

    @Transactional
    @Override
    public boolean commit(BINDEX indexB,
                          Set<IINDEX> indexI, Set<MINDEX> indexM, Set<CINDEX> indexC, Set<SINDEX> indexS) {

        // Platform.runLater(() ->  Database.refresh(indexB,indexI,indexM,indexC,indexS,consumeI,consumeM,consumeC,consumeS) );


        iRepo.saveAll(indexI.stream().map(i -> modelMapper.map(i, juniter.core.model.index.IINDEX.class)).collect(Collectors.toList()));
        //iig.addAll(indexI);
        mRepo.saveAll(indexM.stream().map(i -> modelMapper.map(i, juniter.core.model.index.MINDEX.class)).collect(Collectors.toList()));
        cRepo.saveAll(indexC.stream().map(i -> modelMapper.map(i, juniter.core.model.index.CINDEX.class)).collect(Collectors.toList()));
        sRepo.saveAll(indexS.stream()
                .map(i -> modelMapper.map(i, juniter.core.model.index.SINDEX.class))
                .collect(Collectors.toList()));


        if (indexB != null) {
            bRepo.save(modelMapper.map(indexB, juniter.core.model.index.BINDEX.class));

            LOG.info("Commit -  Certs: +" + indexC.size() + ",-" + cRepo.count() +
                    "  Membship: +" + indexM.size() + ",-" + mRepo.count() +
                    "  Idty: +" + indexI.size() + ",-" + iRepo.count() +
                    "  localS: +" + indexS.size() + "," + sRepo.count() +
                    "  IndexB: " + bRepo.count() + ", " + indexB.number);
        }


        return true;
    }


    public Stream<BINDEX> indexBGlobal() {
        return bRepo.findAll().stream().map(c -> modelMapper.map(c, BINDEX.class));
    }


    @Override
    public Stream<CINDEX> indexCGlobal() {
        return cRepo.findAll().stream().map(c -> modelMapper.map(c, CINDEX.class));
    }

    //List<IINDEX> iig = new ArrayList<>();

    @Override
    public Stream<IINDEX> indexIGlobal() {
        return iRepo.findAll().stream().map(c -> modelMapper.map(c, IINDEX.class));
    }

    @Override
    public Stream<CINDEX> reduceC(String issuer, String receiver) {
        if (issuer == null && receiver != null) {
            return cRepo.receivedBy(receiver).stream().map(c -> modelMapper.map(c, CINDEX.class));
        }

        if (receiver == null && issuer != null) {
            return cRepo.issuedBy(issuer).stream().map(c -> modelMapper.map(c, CINDEX.class));
        }

        return Stream.empty();
    }


    @Override
    public Stream<Account> lowAccounts() {
        return accountRepo.lowAccounts().stream();
    }

    @Override
    public Stream<MINDEX> reduceM(String pub) {
        return mRepo.member(pub).stream().map(c -> modelMapper.map(c, MINDEX.class));
    }

    @Override
    public Stream<MINDEX> findPubkeysThatShouldExpire(Long mTime) {
        return mRepo.findPubkeysThatShouldExpire(mTime).stream().map(c -> modelMapper.map(c, MINDEX.class));
    }


    @Override
    public Stream<MINDEX> findRevokesOnLteAndRevokedOnIsNull(Long mTime) {
        return mRepo.findRevokesOnLteAndRevokedOnIsNull(mTime).stream().map(c -> modelMapper.map(c, MINDEX.class));
    }

    @Transactional(readOnly = true)
    @Override
    public Stream<IINDEX> idtyByPubkey(String pub) {
        return iRepo.idtyByPubkey(pub).stream().map(c -> modelMapper.map(c, IINDEX.class));
    }

    @Override
    public Stream<IINDEX> idtyByUid(String uid) {
        return iRepo.byUid(uid).stream().map(c -> modelMapper.map(c, IINDEX.class));
    }

    @Override
    public Stream<SINDEX> sourcesByConditions(String conditions) {
        return sRepo.sourcesByConditions(conditions)
                .map(s -> modelMapper.map(s, SINDEX.class))
                ;
    }

    @Override
    public Stream<SINDEX> sourcesByConditions(String identifier, Integer pos) {
        return sRepo.sourcesByIdentifierAndPos(identifier, pos)
                .map(c -> modelMapper.map(c, SINDEX.class))
                .peek(s -> System.out.println("mapped i p " + s))
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
                .peek(s -> System.out.println("mapped all " + s));
    }

    @Timed
    @Override
    public int trimGlobal(BINDEX head, int bIndexSize) {

        int bellow = Math.max(0, head.number - bIndexSize);

        LOG.info("Trimming " + bIndexSize + " at " + head.number);
        bRepo.trim(bIndexSize);
        sRepo.trim(bellow);
        //iRepo.trimRecords(bellow);
        //mRepo.trimRecords(bellow);
        cRepo.trim(bellow);
        return bIndexSize;
    }

    @Override
    public void trimSandbox(DBBlock block) {

    }


    /**
     * mostly technical function to error handle, parametrized, log the validation function
     *
     * @param syncUntil blockNumber to synchronize with
     * @param quick
     */
    @Async
    @Timed(longTask = true, histogram = true)
    public void indexUntil(int syncUntil, boolean quick) {

        LOG.info("Testing the local repository ");
        init(false);
        var baseTime = System.currentTimeMillis();
        var time = System.currentTimeMillis();
        long delta;

        for (int i = head().map(h -> h.number + 1).orElse(0); i <= syncUntil && Bus.isIndexing.get(); i++) {
            final int finali = i;


            final var block = blockRepo.block(i).orElseGet(() -> blockLoader.fetchAndSaveBlock(finali));

            try {
                if (completeGlobalScope(block, !quick)) {
                    LOG.debug("Validated " + block);
                    Platform.runLater(() -> Bus.currentBindex.setValue(finali));
                } else {
                    LOG.warn("ERROR Validating " + block);
                    Platform.runLater(() -> Bus.indexLogMessage.setValue("NOT Validated " + block));

                    break;
                }
            } catch (AssertionError | Exception e) {
                LOG.warn("error validating block " + block, e);
                Platform.runLater(() -> Bus.indexLogMessage.setValue("ERROR Validating " + block + " - " + e.getMessage()));
                break;
            }

            if (block.getDividend() != null) {
                var newTime = System.currentTimeMillis();
                delta = newTime - time;
                var baseDelta = time - baseTime;
                final var perBlock = delta / 288;
                final var estimate = (syncUntil - block.getNumber()) * perBlock;

                var log = "Validation : elapsed time " + TimeUtils.format(baseDelta) + " which is " + perBlock
                        + " ms per block, estimating: " + TimeUtils.format(estimate) + "left";

                LOG.info(log);
                Platform.runLater(() -> Bus.indexLogMessage.setValue(log));
                time = newTime;
            }
        }

        delta = System.currentTimeMillis() - time;
        LOG.info("Finished validation, took :  " + TimeUtils.format(delta));
        Platform.runLater(() -> Bus.isIndexing.setValue(false));

    }

    public void reset(boolean resetDB) {
        init(resetDB);
    }
}
