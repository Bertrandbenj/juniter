package juniter.service.core;

import io.micrometer.core.annotation.Timed;
import juniter.core.event.DecrementCurrent;
import juniter.core.event.NewBINDEX;
import juniter.core.event.NewBlock;
import juniter.core.model.dbo.BStamp;
import juniter.core.model.dbo.ChainParameters;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.tx.Transaction;
import juniter.core.model.dbo.wot.Certification;
import juniter.core.model.dbo.wot.Identity;
import juniter.core.model.dbo.wot.Member;
import juniter.core.model.dto.node.BlockNetworkMeta;
import juniter.core.model.technical.CcyStats;
import juniter.core.validation.BlockLocalValid;
import juniter.repository.jpa.block.BlockRepository;
import juniter.repository.jpa.block.ParamsRepository;
import juniter.service.bma.loader.BlockLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Service
public class BlockService implements BlockLocalValid, ApplicationListener<NewBINDEX> {

    @Autowired
    private BlockRepository blockRepo;

    @Autowired
    private BlockLoader blockLoader;

    @Autowired
    private ApplicationEventPublisher coreEventBus;

    @Autowired
    private ParamsRepository paramsRepository;


    @Autowired
    private Index index;


    @Cacheable(value = "params")
    public ChainParameters paramsByCCY(String ccy) {
        return paramsRepository.paramsByCCY(ccy);
    }

    //@Cacheable(value = "ccys")
    List<String> existingCCYs() {
        return paramsRepository.existingCCY();
    }

    public Optional<DBBlock> block(Integer num) {
        return blockRepo.block(num);
    }

    public DBBlock blockOrFetch(Integer num) {
        return block(num).orElseGet(() -> blockLoader.fetchAndSaveBlock(num));
    }


    public long count() {
        return blockRepo.count();
    }


    public List<DBBlock> blocks(Integer num) {
        return blockRepo.block_(num);
    }

    public Optional<DBBlock> findTop1ByNumber(Integer num) {
        return blockRepo.findTop1ByNumber(num);
    }

    //public Optional<DBBlock> currentStrict(Integer num) {
    //    return blockRepo.findTop1ByNumber(num);
    //}


    public DBBlock block(Integer num, String hash) {
        return blockRepo.block(num, hash);
    }


    public DBBlock block(BStamp bStamp) {
        return block(bStamp.getNumber(), bStamp.getHash());
    }


    public Integer currentBlockNumber() {
        return currentStrict().map(DBBlock::getNumber).orElse(0);
    }


    public Stream<DBBlock> lastBlocks() {
        return blockRepo.findTop10ByOrderByNumberDesc();
    }

    public Stream<DBBlock> blocksIn(List<Integer> list) {
        return blockRepo.findByNumberIn(list);
    }


    public void saveAll(List<DBBlock> blocksToSave) {
        blockRepo.saveAll(blocksToSave);
    }


    @Timed(value = "blockservice_current")
    public Optional<DBBlock> currentStrict() {
        return index.head().map(b -> blockRepo.block(b.getNumber(), b.getHash()))
                //.or(() -> blockRepo.findTop1ByOrderByNumberDesc())
                ;
    }

    public DBBlock currentOrTop() {
        return currentStrict().orElse(blockRepo.findTop1ByOrderByNumberDesc().orElseThrow());
    }

    public DBBlock currentOrFetch() {
        return currentStrict().orElseGet(() -> blockLoader.fetchAndSaveBlock("current"));
    }


    private DBBlock currentStrict(String ccy) {
        return blockRepo.current(ccy, currents.get(ccy));
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

        for (Member m : block.getRenewed()) {
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

        var cur = blockRepo.block(block.getNumber(), block.getHash());
        // Do the saving after some checks
        if (silentCheck(block) && cur != null) {
            try {
                var res = blockRepo.save(block);

                if (cur.getNumber() < res.getNumber()) {
                    coreEventBus.publishEvent(new NewBlock(res));
                }

                return Optional.of(res);

            } catch (Exception e) {
                LOG.warn("BlockRepo.safeSave  block " + block.getNumber() + " already exists");
                return Optional.empty();
            }
        } else {
            LOG.error("Error safeSave block " + block.getNumber()
                    + " :  BlockIsLocalValid " + silentCheck(block)
                    + ", block doesn't exists just yet " + cur);
        }


        return Optional.empty();
    }

    public List<BlockNetworkMeta> issuersFrameFromTo(int i, Integer current) {
        return blockRepo.issuersFrameFromTo(i, current);
    }

    public void delete(DBBlock block) {
        blockRepo.delete(block);
    }

    public List<Integer> blockNumbers() {
        return blockRepo.blockNumbers();
    }

    public void deleteAll() {
        blockRepo.deleteAll();
    }

    public void truncate() {
        blockRepo.truncate();
        blockRepo.deleteAll();
        blockRepo.truncate();
        blockRepo.findAll().forEach(b -> {
            blockRepo.delete(b);
            //LOG.info("deleted " + b.getNumber());
            coreEventBus.publishEvent(new DecrementCurrent());
        });
    }

    public List<DBBlock> findAll() {
        return blockRepo.findAll();
    }


    private Map<String, Integer> currents = new HashMap<>();

    @Override
    public void onApplicationEvent(NewBINDEX newBINDEX) {
        var currentNumber = newBINDEX.getWhat().getNumber();
        var currentCCY = newBINDEX.getWhat().getCurrency();
        currents.put(currentCCY, currentNumber);

    }
}
