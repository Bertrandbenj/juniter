package juniter.service.jpa;

import io.micrometer.core.annotation.Timed;
import juniter.core.event.DecrementCurrent;
import juniter.core.event.NewBlock;
import juniter.core.exception.DuniterException;
import juniter.core.model.dbo.BStamp;
import juniter.core.model.dbo.ChainParameters;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.tx.Transaction;
import juniter.core.model.dbo.wot.Certification;
import juniter.core.model.dbo.wot.Identity;
import juniter.core.model.dbo.wot.Member;
import juniter.core.model.dto.node.BlockNetworkMeta;
import juniter.core.model.meta.DUPMember;
import juniter.core.model.technical.CcyStats;
import juniter.core.service.BlockFetcher;
import juniter.core.service.BlockService;
import juniter.repository.jpa.block.BlockRepository;
import juniter.repository.jpa.block.ParamsRepository;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;


@Service
public class JPABlockService implements BlockService {

    private static final Logger LOG = LogManager.getLogger(JPABlockService.class);


    @Autowired
    private BlockRepository blockRepo;

    @Getter
    @Autowired
    private List<BlockFetcher> blockFetchers;

    @Autowired
    private ApplicationEventPublisher coreEventBus;

    @Autowired
    private ParamsRepository paramsRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private Index index;


    @Cacheable(value = "params")
    public ChainParameters paramsByCCY(String ccy) {

        return paramsRepository
                .paramsByCCY(ccy)
                .orElseThrow(() -> new DuniterException("paramsByCCY couldn't be set for currency" + ccy));
    }


    public List<ChainParameters> existingCCYs() {
        return paramsRepository.existingCCY();
    }

    public Optional<DBBlock> block(Integer num) {
        var blocks = blockRepo.block_("g1", num);
        if (blocks.isEmpty()) {
            return Optional.empty();
        }
        if (blocks.size() > 1) {
            return Optional.ofNullable(blockRepo.currentChained("g1", num));
        }
        return Optional.of(blocks.get(0));
    }

    public DBBlock blockOrFetch(Integer num) {
        return block(num)
                .orElseGet(() -> blockFetchers
                        .parallelStream()
                        .map(bl -> bl.fetchAndSaveBlock(num))
                        .findAny()
                        .get()
                );
    }


    public long count() {
        return blockRepo.count();
    }


    public List<DBBlock> blocks(String ccy, Integer num) {
        return blockRepo.block_(ccy, num);
    }

    public Optional<DBBlock> findTop1ByNumber(Integer num) {
        return blockRepo.findTop1ByNumber(num);
    }

    //public Optional<DBBlock> currentChained(Integer num) {
    //    return blockRepo.findTop1ByNumber(num);
    //}


    public DBBlock block(Integer num, String hash) {
        return blockRepo.block(num, hash);
    }


    public DBBlock block(BStamp bStamp) {
        return block(bStamp.getNumber(), bStamp.getHash());
    }


    public Integer currentBlockNumber() {
        return currentChained().map(DBBlock::getNumber).orElse(0);
    }


    public Stream<DBBlock> blocksIn(List<Integer> blocksToFind) {
        return blockRepo.findByNumberIn(blocksToFind);
    }

    public void findMissing(int from, int count) {
        final List<Integer> blocksToFind = IntStream.range(from, from + count).boxed().collect(toList());
        LOG.info("---blocksToFind: " + blocksToFind);

        var knownBlocks = blockRepo.findByNumberIn(blocksToFind).collect(toList());


        final List<DBBlock> blocksMissingFromRepo = blocksToFind.stream()
                .filter(b -> knownBlocks.stream().noneMatch(kb -> kb.getNumber().equals(b)))
                .flatMap(lg -> blockFetchers.stream().map(bl -> bl.fetchAndSaveBlock(lg)))
                .collect(toList());


        saveAll(blocksMissingFromRepo);

        LOG.info("---known blocks: " + knownBlocks.stream().map(DBBlock::getNumber).collect(toList()));
        LOG.info("---fetchTrimmed blocks: " + Stream.concat(blocksMissingFromRepo.stream(), knownBlocks.stream())
                .map(b -> b.getNumber().toString()).collect(joining(",")));
    }


    private void saveAll(List<DBBlock> blocksToSave) {
        blocksToSave.forEach(b -> safeSave(b));
    }


    @Timed(value = "blockservice_current")
    public Optional<DBBlock> currentChained() {
        return index.head().map(b -> blockRepo.block(b.getNumber(), b.getHash()));
    }

    public DBBlock currentOrTop() {
        return currentChained()
                .orElse(blockRepo.findTop1ByOrderByNumberDesc()
                        .orElseThrow(() -> new DuniterException("currentOrTop found neither index " + currentChained() + " nor top " + blockRepo.findTop1ByOrderByNumberDesc())));
    }

    public DBBlock currentOrFetch() {
        return currentChained()
                .orElseGet(() -> blockFetchers.stream()
                        .map(bl -> bl.fetchAndSaveBlock("current"))
                        .findAny()
                        .orElseThrow(() -> new DuniterException("currentOrFetch found neither index " + currentChained() + " nor fetched ")));
    }


    public List<Integer> withUD() {
        return blockRepo.withUD();
    }

    public List<CcyStats> statsWithUD() {
        return blockRepo.statsWithUD();
    }

    public Stream<DBBlock> with(Predicate<DBBlock> predicate) {

        return blockRepo.streamAllBlocks()
                .filter(predicate)
                .sorted();
    }

    @Transactional(readOnly = true)
    public List<DBBlock> listBlocksFromTo(int i, int i1) {
        return blockRepo.blocksFromTo(i, i1);
    }

    public Stream<DBBlock> streamBlocksFromTo(int i, int i1) {
        return blockRepo.streamBlocksFromTo(i, i1);
    }

    @Transactional
    public Optional<DBBlock> safeSave(DBBlock block) throws AssertionError {
        //LOG.error("localsavng  "+node.getNumber());

        if (block.getNumber().equals(0) && block.params() != null) {
            block.params().setCurrency(block.getCurrency());
        }

        block.getSize();

        // Denormalized Fields !
        for (Transaction tx : block.getTransactions()) {
            tx.setWritten(block.bStamp());
            tx.getHash();
        }

        for (Identity ident : block.getIdentities()) {
            ident.setWritten(block.bStamp());
        }

        for (Certification cert : block.getCertifications()) {
            cert.setWritten(block.bStamp());
        }

        for (DUPMember m : block.getRenewed()) {
            m.setWritten(block.bStamp());
        }

        for (Member m : block.getRevoked()) {
            m.setWritten(block.bStamp());
        }

        for (Member m : block.getJoiners()) {
            m.setWritten(block.bStamp());
        }

        for (Member m : block.getExcluded()) {
            m.setWritten(block.bStamp());
        }

        for (Member m : block.getMembers()) {
            m.setWritten(block.bStamp());
        }

        for (Member m : block.getLeavers()) {
            m.setWritten(block.bStamp());
        }
        var dbCurrent = blockRepo.block(block.getCurrency(), block.getNumber(), block.getHash());
        // Do the saving after some checks
        if (block.checked().isEmpty() && dbCurrent == null) {
            try {
                var res = blockRepo.save(block);

                if (!isBulkLoading()) {
                    coreEventBus.publishEvent(new NewBlock(res));
                }

                return Optional.of(res);

            } catch (Exception re) {
                LOG.warn("BlockRepo.safeSave  block " + block.getNumber() + " already exists? " + re.getMessage(), re);
                return Optional.empty();
            }
        } else {
            LOG.warn("safeSave block " + block.getNumber()
                    + " : Local Valid? " + silentCheck(block)
                    + ", Already Exists? " + dbCurrent);
        }


        return Optional.empty();
    }

    public List<BlockNetworkMeta> issuersFrameFromTo(int i, Integer current) {
        return blockRepo.issuersFrameFromTo(i, current);
    }

    public void delete(DBBlock block) {
        blockRepo.delete(block);
    }

    @Transactional
    @Modifying
    public void deleteAt(String ccy, Integer block) {
        blockRepo.block_(ccy, block).forEach(blockRepo::delete);
    }


    public List<Integer> blockNumbers(String currency, int rangeStart, int rangeEnd) {
        return blockRepo.blockNumbers(currency, rangeStart, rangeEnd);
    }

    public void truncate() {
        LOG.info(" === Reseting DB " + blockRepo.count());

        blockRepo.deleteAllInBatch();
        blockRepo.deleteAll();
        blockRepo.deleteAllInBatch();
        blockRepo.findAll().forEach(b -> {
            blockRepo.delete(b);
            //LOG.info("deleted " + b.getNumber());
            coreEventBus.publishEvent(new DecrementCurrent());
        });

        LOG.info(" === Reseting DB - DONE " + blockRepo.count());

    }

    public List<DBBlock> findAll() {
        return blockRepo.findAll();
    }


}
