package juniter.service.jpa;

import io.micrometer.core.annotation.Timed;
import juniter.core.event.CurrentBNUM;
import juniter.core.event.Indexing;
import juniter.core.event.NewBINDEX;
import juniter.core.exception.DuniterException;
import juniter.core.model.dbo.BStamp;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.index.*;
import juniter.core.utils.TimeUtils;
import juniter.core.validation.GlobalValid;
import juniter.repository.jpa.block.ParamsRepository;
import juniter.repository.jpa.index.*;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
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
//@DependsOn(value = {"blockService"})
@Service
public class Index implements GlobalValid {

    private static final Logger LOG = LogManager.getLogger(Index.class);


    @Autowired
    private ApplicationEventPublisher coreEventBuss;

    @Getter
    @Autowired
    private CINDEXRepository cRepo;
    @Getter
    @Autowired
    private IINDEXRepository iRepo;
    @Getter
    @Autowired
    private MINDEXRepository mRepo;
    @Getter
    @Autowired
    private SINDEXRepository sRepo;
    @Getter
    @Autowired
    private BINDEXRepository bRepo;
    @Getter
    @Autowired
    private AccountsRepository accountRepo;

    @Autowired
    private JPABlockService blockService;

    @Autowired
    private ParamsRepository paramsRepository;

    @Autowired
    private Sandboxes sandboxes;

    public String hashAt(Integer number) {
        return blockService.block(number).map(DBBlock::getHash).orElseThrow();
    }

    public Long medianAt(Integer number) {
        return blockService.block(number).map(DBBlock::getMedianTime).orElseThrow();
    }

    @PostConstruct
    public void init() {
        LOG.info("Init Indexes: ");


        init(false, "g1");
    }


    @Transactional
    @Timed(value = "index_init")
    private void init(boolean resetDB, String ccy) {
        try {
            var params = paramsRepository.paramsByCCY(ccy).orElseThrow(()->new DuniterException("paramRepository Unavaible yet "));
            conf.accept(params);
        } catch (Exception e) {
            LOG.error("index_init ", e);
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
    public Optional<DBBlock > createdOnBlock(BStamp bstamp) {


        if (bstamp.getNumber().equals(0))
            return blockService.block(0);
        return Optional.of(blockService.blockOrFetch(bstamp.getNumber()));
    }

    @Override
    public Optional<DBBlock> createdOnBlock(Integer number) {
        return blockService.block(number);
    }

    @Transactional
    @Override
    @Timed(value = "index_commit")
    public boolean commit(BINDEX indexB,
                          Set<IINDEX> indexI, Set<MINDEX> indexM, Set<CINDEX> indexC, Set<SINDEX> indexS) {


        iRepo.saveAll(new ArrayList<>(indexI));
        mRepo.saveAll(new ArrayList<>(indexM));
        cRepo.saveAll(new ArrayList<>(indexC));
        sRepo.saveAll(new ArrayList<>(indexS));


        if (indexB != null) {
            bRepo.save(indexB);

            LOG.info("Commit " + indexB.getNumber() +
                    " - Certs: +" + indexC.size() + ",-" + cRepo.count() +
                    " Membship: +" + indexM.size() + ",-" + mRepo.count() +
                    " Idty: +" + indexI.size() + ",-" + iRepo.count() +
                    " localS: +" + indexS.size() + "," + sRepo.count());
        }

        return true;
    }

    @Override
    public List<BINDEX> indexBGlobal() {
        return bRepo.allAsc();
    }


    @Override
    public Stream<CINDEX> indexCGlobal() {
        return cRepo.findAll().stream();
    }


    @Override
    public Stream<IINDEX> indexIGlobal() {
        return iRepo.findAll().stream();
    }

    /**
     * if issuer == receiver
     * return the union of certifications issued by search and received by search
     *
     * @param issuer
     * @param receiver
     * @return certifiation
     */
    @Override
    public Stream<CINDEX> getC(String issuer, String receiver) {
        if (issuer != null && issuer.equals(receiver)) {
            return Stream.concat(cRepo.receivedBy(receiver).stream(), cRepo.issuedBy(issuer).stream());
        } else if (receiver != null && issuer != null) {
            return cRepo.getCert(issuer, receiver).stream();
        } else if (receiver != null) {
            return cRepo.receivedBy(receiver).stream();
        } else if (issuer != null) {
            return cRepo.issuedBy(issuer).stream();
        }

        return Stream.empty();
    }

    @Override
    public Stream<Accounts> lowAccounts() {
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
        return mRepo.findPubkeysThatShouldExpire(mTime).stream();
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

    @Timed(value = "index_trimGlobal", longTask = true)
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
        sandboxes.trim(block);
    }


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
    @Timed(longTask = true, histogram = true, value = "index_indexUntil")
    public synchronized void indexUntil(int syncUntil, boolean quick, String ccy) {

        coreEventBuss.publishEvent(new Indexing(true, "indexUntil " + syncUntil + " , quick? " + quick + " , ccy? " + ccy));

        init(false, ccy);
        var baseTime = System.currentTimeMillis();
        var time = System.currentTimeMillis();
        long delta;
        var lastHeadNumber = head().map(h -> h.getNumber() + 1).orElse(0);

        for (int bnum = lastHeadNumber; bnum <= syncUntil; bnum++) {

            final var block = blockService.blockOrFetch(bnum);

            try {
                if (completeGlobalScope(block, !quick)) {
                    coreEventBuss.publishEvent(new NewBINDEX(head_(), ""));
                } else {
                    coreEventBuss.publishEvent(new Indexing(false, "NOT Valid " + block));
                    break;
                }
            } catch (AssertionError | Exception e) {
                LOG.error("ERROR Validating " + block + " - " + e.getMessage(), e);

                coreEventBuss.publishEvent(new Indexing(false, "ERROR Validating " + block + " - " + e.getMessage()));
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

                coreEventBuss.publishEvent(new Indexing(true, log));
                time = newTime;
            }
        }

        delta = System.currentTimeMillis() - time;
        coreEventBuss.publishEvent(new Indexing(false, ""));

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
            blockService.deleteAt(ccy,  h.getNumber() );

            coreEventBuss.publishEvent(new CurrentBNUM(head_().getNumber()));
            coreEventBuss.publishEvent(new Indexing(false, "Reverted to " + h.getNumber() + " from " + h));

            reset(false, ccy);
        });
    }
}
