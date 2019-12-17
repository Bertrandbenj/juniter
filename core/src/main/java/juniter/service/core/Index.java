package juniter.service.core;

import com.codahale.metrics.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import juniter.core.event.CurrentBNUM;
import juniter.core.event.Indexing;
import juniter.core.event.LogIndex;
import juniter.core.event.NewBINDEX;
import juniter.core.model.dbo.BStamp;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.index.*;
import juniter.core.utils.TimeUtils;
import juniter.core.validation.GlobalValid;
import juniter.repository.jpa.index.*;
import juniter.service.bma.loader.BlockLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
public class Index implements GlobalValid {

    private static final Logger LOG = LogManager.getLogger(Index.class);


    @Autowired
    private ApplicationEventPublisher coreEventBuss;


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
    private BlockService blockService;


    @Value("${juniter.startIndex:false}")
    private boolean startIndex;

    @PersistenceContext
    private EntityManager entityManager;

    private Boolean indexing = false;


    @PostConstruct
    public void init() {
        LOG.info("Init Indexes: ");

        init(false,"g1");
    }

    /**
     * hack to create account VIEW after SQL table's init.
     * TODO: might not work on every SQL backend, please adapt
     */
    @PostConstruct
    @Transactional
    public void createAccountView() {
        try {

            if (accountRepo.count() <= 0) {
                entityManager.getTransaction().begin();

                var q1 = entityManager.createNativeQuery("DROP TABLE IF EXISTS account;");


                var q2 = entityManager.createNativeQuery(
                        "CREATE OR REPLACE VIEW account AS " +
                                "SELECT conditions, sum(case WHEN consumed THEN 0-amount ELSE amount end) bSum " +
                                "FROM index_s GROUP BY conditions ORDER by conditions;");

                entityManager.joinTransaction();

                q1.executeUpdate();
                q2.executeUpdate();
                entityManager.getTransaction().commit();
                LOG.info("Successfully added Account view ");
            }

        } catch (Exception e) {
            LOG.error("creating Account view failed, please do manually  ", e.getMessage());
        }

    }

    @Transactional
    @Counted(absolute = true)
    private void init(boolean resetDB, String ccy) {
        try {
            var params = blockService.paramsByCCY(ccy);
            conf.accept(params);
        } catch (Exception e) {
            LOG.error(e, e);
        }

        if (resetDB) {
            cRepo.deleteAll();
            iRepo.deleteAll();
            mRepo.deleteAll();
            sRepo.deleteAll();
            bRepo.deleteAll();
        }

        resetLocalIndex();
        IndexB.clear();
        IndexB.addAll(indexBGlobal());
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

        LOG.info("Initialized BINDEX[" + IndexB.size() + "] at " + head().map(BINDEX::getNumber) + "");

    }

    public List<String> getIssuers() {
        return IndexB.stream().map(BINDEX::getIssuer).distinct().collect(Collectors.toList());

    }

    @Transactional
    @Override
    public Optional<DBBlock> createdOnBlock(BStamp bstamp) {

        if (bstamp.getNumber().equals(0))
            return blockService.block(0);
        return Optional.of(blockService.block(bstamp.getNumber())
                .orElseGet(() -> blockLoader.fetchAndSaveBlock(bstamp.getNumber())));
    }

    @Override
    public Optional<DBBlock> createdOnBlock(Integer number) {
        return blockService.block(number);
    }

    @Transactional
    @Override
    @Counted(absolute = true)
    public boolean commit(BINDEX indexB,
                          Set<IINDEX> indexI, Set<MINDEX> indexM, Set<CINDEX> indexC, Set<SINDEX> indexS) {

        // Platform.runLater(() ->  Database.refresh(indexB,indexI,indexM,indexC,indexS,consumeI,consumeM,consumeC,consumeS) );


        iRepo.saveAll(new ArrayList<>(indexI));
        //iig.addAll(indexI);
        mRepo.saveAll(new ArrayList<>(indexM));
        cRepo.saveAll(new ArrayList<>(indexC));
        sRepo.saveAll(new ArrayList<>(indexS));


        if (indexB != null) {
            bRepo.save(indexB);

            LOG.info("Commit " +indexB.getNumber() +
                    " - Certs: +" + indexC.size() + ",-" + cRepo.count() +
                    " Membship: +" + indexM.size() + ",-" + mRepo.count() +
                    " Idty: +" + indexI.size() + ",-" + iRepo.count() +
                    " localS: +" + indexS.size() + "," + sRepo.count());
        }

        IndexB.add(indexB);
        return true;
    }


    private List<BINDEX> indexBGlobal() {
        return bRepo.allAsc();
    }


    @Override
    public Stream<CINDEX> indexCGlobal() {
        return cRepo.findAll().stream();
    }

    //List<IINDEX> iig = new ArrayList<>();

    @Override
    public Stream<IINDEX> indexIGlobal() {
        return iRepo.findAll().stream();
    }

    @Override
    public Stream<CINDEX> getC(String issuer, String receiver) {
        if (receiver != null && issuer != null) {
            return cRepo.getCert(issuer, receiver).stream();

        } else if (receiver != null && issuer == null) {
            return cRepo.receivedBy(receiver).stream();

        } else if (receiver == null && issuer != null) {
            return cRepo.issuedBy(issuer).stream();
        }

        return Stream.empty();
    }

    @Override
    public Stream<Account> lowAccounts() {
        return accountRepo.lowAccounts().stream();
    }

    @Override
    public Optional<MINDEX> reduceM(String pub) {
        return mRepo.member(pub).stream()
                .sorted((m2, m1) -> m1.getWritten().getMedianTime().compareTo(m2.getWritten().getMedianTime()))
                //.peek(x -> LOG.info("reducing " + x))
                .reduce(MINDEX.reducer);
    }

    @Override
    public Stream<CINDEX> findCertsThatShouldExpire(Long mTime) {
        return cRepo.findCertsThatShouldExpire(mTime).stream();
    }


    @Override
    public Stream<String> findPubkeysThatShouldExpire(Long mTime) {
        return mRepo.findPubkeysThatShouldExpire3(mTime).stream();
    }


    @Override
    public Stream<String> findRevokesOnLteAndRevokedOnIsNull(Long mTime) {
        return mRepo.findRevokesOnLteAndRevokedOnIsNull(mTime).stream();
    }

    @Transactional(readOnly = true)
    @Override
    public Stream<IINDEX> idtyByPubkey(String pub) {
        return iRepo.idtyByPubkey(pub).stream();
    }

    @Transactional(readOnly = true)
    @Override
    public Stream<IINDEX> idtyByUid(String uid) {
        return iRepo.byUid(uid).stream();
    }

    @Override
    public Stream<SINDEX> sourcesByConditions(String conditions) {
        return sRepo.sourcesByConditions(conditions)
                .stream()
                ;
    }

    @Override
    public Stream<SINDEX> sourcesByConditions(String identifier, Integer pos) {
        return sRepo.sourcesByIdentifierAndPos(identifier, pos)
                .peek(s -> LOG.info("mapped i p " + s))
                ;
    }

    @Override
    public Stream<MINDEX> indexMGlobal() {
        return mRepo.findAll().stream();
    }

    @Override
    public Stream<SINDEX> indexSGlobal() {
        return sRepo.sourceNotConsumed()
                .peek(s -> LOG.info("mapped all " + s));
    }

    @Override
    public Integer certStock(String issuer, Long asOf) {
        return cRepo.certStock(issuer, asOf);
    }

    @Timed
    @Override
    public void trimGlobal(BINDEX head, int bIndexSize) {

        int bellow = Math.max(0, head.getNumber() - bIndexSize);

        LOG.debug("Trimming " + bIndexSize + " at " + head.getNumber());
        bRepo.trim(bIndexSize);
        sRepo.trim(bellow);
        //iRepo.trimRecords(bellow);
        //mRepo.trimRecords(bellow);
        cRepo.trim(bellow);
    }

    @Override
    public void trimSandbox(DBBlock block) {

    }

    @Autowired
    private BlockLoader blockLoader;

    /**
     * mostly technical function to error handle, parametrized, log the validation function
     * <p>
     * Asynchronous to the thread that calls it, once at a time
     * FIXME java synchronized will be a problem for more than one currency indexing
     *
     * @param syncUntil a specific block number
     * @param quick     whether or not to index quickly
     */
    @Async
    @Timed(longTask = true, histogram = true)
    public synchronized void indexUntil(int syncUntil, boolean quick, String ccy) {

        LOG.info("indexUntil " + syncUntil + " , quick? " + quick + " , ccy? " + ccy);
        init(false, ccy);
        var baseTime = System.currentTimeMillis();
        var time = System.currentTimeMillis();
        long delta;
        int reverted = 0;

        for (int bnum = head().map(h -> h.getNumber() + 1).orElse(0); bnum <= syncUntil; bnum++) {
            final int finalBnum = bnum;


            final var block = blockService.block(finalBnum).orElseGet(() -> blockLoader.fetchAndSaveBlock(finalBnum));

            try {
                if (completeGlobalScope(block, !quick)) {
                    LOG.debug("Validated " + block);
                    coreEventBuss.publishEvent(new NewBINDEX(head_()));
                } else {
                    coreEventBuss.publishEvent(new LogIndex("NOT Validated " + block));


                    break;
                }
            } catch (AssertionError | Exception e) {
                LOG.error("ERROR Validating " + block + " - " + e.getMessage(), e);

                coreEventBuss.publishEvent(new LogIndex("ERROR Validating " + block + " - " + e.getMessage()));
                //coreEventBuss.publishEvent(new PossibleFork());

                break;
            }

            if (block.getDividend() != null) {
                var newTime = System.currentTimeMillis();
                delta = newTime - time;
                var baseDelta = time - baseTime;
                final var perBlock = delta / 288;
                final var estimate = (syncUntil - block.getNumber()) * perBlock;

                var log = "Validation : elapsed time " + TimeUtils.format(baseDelta) + " which is " + perBlock
                        + " ms per node, estimating: " + TimeUtils.format(estimate) + "left";

                coreEventBuss.publishEvent(new LogIndex(log));
                time = newTime;
            }
        }

        delta = System.currentTimeMillis() - time;
        LOG.info("Finished validation, took :  " + TimeUtils.format(delta));
        coreEventBuss.publishEvent(new Indexing(false));

    }

    public void reset(boolean resetDB, String ccy) {
        init(resetDB, ccy);
    }

    public void revert1(String ccy) {
        bRepo.head().ifPresent(h -> {
            bRepo.delete(h);

            iRepo.deleteAll(
                    iRepo.writtenOn(h.getNumber(), h.getHash())
            );
            mRepo.deleteAll(
                    mRepo.writtenOn(h.getNumber(), h.getHash())
            );
            cRepo.deleteAll(
                    cRepo.writtenOn(h.getNumber(), h.getHash())
            );
            sRepo.deleteAll(
                    sRepo.writtenOn(h.getNumber(), h.getHash())
            );


            coreEventBuss.publishEvent(new CurrentBNUM(h.getNumber() - 1));

            coreEventBuss.publishEvent(new LogIndex("Reverted to " + h.getNumber() + " from " + h));

            reset(false, ccy);
        });
    }
}
