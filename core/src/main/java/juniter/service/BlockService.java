package juniter.service;

import juniter.core.event.DecrementCurrent;
import juniter.core.event.NewBlock;
import juniter.core.model.dbo.BStamp;
import juniter.core.model.dbo.DBBlock;
import juniter.core.model.dbo.tx.Transaction;
import juniter.core.model.dbo.wot.Certification;
import juniter.core.model.dbo.wot.Identity;
import juniter.core.model.dbo.wot.Member;
import juniter.core.model.dto.node.IssuersFrameDTO;
import juniter.core.validation.BlockLocalValid;
import juniter.repository.jpa.block.BlockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BlockService implements BlockLocalValid {

    @Autowired
    private BlockRepository blockRepo;

    @Autowired
    private ApplicationEventPublisher coreEventBus;


    public Optional<DBBlock> block(Integer num) {
        return blockRepo.block(num);
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


    public Optional<DBBlock> blockOpt(Integer num, String hash) {
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

    public Optional<DBBlock> current() {
        return blockRepo.findTop1ByOrderByNumberDesc();
    }

    public List<Integer> withUD() {
        return blockRepo.withUD();
    }

    public Stream<DBBlock> with(Predicate<DBBlock> predicate) {

        return blockRepo.streamAllBlocks()
                .filter(predicate)
                .sorted();
    }

    public List<DBBlock> listBlocksFromTo(int i, int i1) {
        return blockRepo.streamBlocksFromTo(i, i1).collect(Collectors.toList());
    }

    public Stream<DBBlock> streamBlocksFromTo(int i, int i1) {
        return blockRepo.streamBlocksFromTo(i, i1);
    }

    public DBBlock save(DBBlock b) {
        return blockRepo.save(b);
    }

    public Optional<DBBlock> localSave(DBBlock block) throws AssertionError {
        //LOG.error("localsavng  "+node.getNumber());
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

                coreEventBus.publishEvent(new NewBlock(block));
                return Optional.of(save(block));

            } catch (Exception e) {
                LOG.error("Error localSave block " + block.getNumber(), e);
                return Optional.empty();
            }
        } else {
            LOG.error("Error localSave block " + block.getNumber());

        }


        return Optional.empty();
    }

    public List<IssuersFrameDTO> issuersFrameFromTo(int i, Integer current) {
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
}
