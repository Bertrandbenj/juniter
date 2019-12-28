package juniter.service.core;

import io.micrometer.core.annotation.Timed;
import juniter.core.event.DecrementCurrent;
import juniter.core.event.NewBINDEX;
import juniter.core.event.NewBlock;
import juniter.core.model.technical.CcyStats;
import juniter.core.model.dbo.BStamp;
import juniter.core.model.dbo.ChainParameters;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.tx.Transaction;
import juniter.core.model.dbo.wot.Certification;
import juniter.core.model.dbo.wot.Identity;
import juniter.core.model.dbo.wot.Member;
import juniter.core.model.dto.node.BlockNetworkMeta;
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
import java.util.stream.Collectors;
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

    //public Optional<DBBlock> current(Integer num) {
    //    return blockRepo.findTop1ByNumber(num);
    //}


    private Optional<DBBlock> blockOpt(Integer num, String hash) {
        return blockRepo.block(num, hash);
    }


    public DBBlock block(Integer num, String hash) {
        return blockRepo.block(num, hash).orElseThrow();
    }


    public DBBlock block(BStamp bStamp) {
        return blockRepo.block(bStamp.getNumber(), bStamp.getHash()).orElseThrow();
    }


    public Integer currentBlockNumber() {
        return current().map(DBBlock::getNumber).orElse(0);
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
    public Optional<DBBlock> current() {
        return blockRepo.findTop1ByOrderByNumberDesc();
    }

    public DBBlock currentOrFetch() {
        return current().orElseGet(() -> blockLoader.fetchAndSaveBlock("current"));
    }


    private DBBlock current(String ccy) {
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
        return blockRepo.streamBlocksFromTo(i, i1).collect(Collectors.toList());
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


        // Do the saving after some checks
        if (checkBlockIsLocalValid(block) && blockOpt(block.getNumber(), block.getHash()).isEmpty()) {

            try {

                var cur = blockRepo.findTop1ByOrderByNumberDesc();
                var res = Optional.ofNullable(blockRepo.save(block));

                if (res.isPresent() && cur.isPresent()) {
                    if (cur.get().getNumber() < res.get().getNumber()) {
                        coreEventBus.publishEvent(new NewBlock(res.get()));
                    }
                }

                return res;

            } catch (Exception e) {
                LOG.error("Error safeSave block " + block.getNumber(), e);
                return Optional.empty();
            }
        } else {
            LOG.error("Error safeSave block " + block.getNumber()
                    + " :  BlockIsLocalValid " + checkBlockIsLocalValid(block)
                    + ", block doesn't exists just yet " + blockOpt(block.getNumber(), block.getHash()).isEmpty());
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
