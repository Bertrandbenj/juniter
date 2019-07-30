package juniter.repository.jpa.net;

import juniter.core.model.dbo.net.Max;
import juniter.core.model.dbo.net.Peer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Repository to manage {@link Peer} instances.
 */
@Repository
public interface PeersRepository extends JpaRepository<Peer, Long> {


    @Override
    <S extends Peer> S save(S peer);


    @Override
    <S extends Peer> List<S> saveAll(Iterable<S> arg0);


    @Query("select p from Peer p")
    Stream<Peer> streamAllPeers();

    @Query("select p from Peer p where CONCAT(p.block.number,'-',p.block.hash) LIKE CONCAT('%', ?1, '%') ")
    Stream<Peer> peerWithBlock(String s);

    @Query("DELETE FROM Peer p WHERE p.pubkey = ?1 AND p.block.number <> ?2")
    @Modifying
    void clean(String pubkey, Integer block);

//    @Query("DELETE FROM Peer p WHERE p.pubkey = ?1 AND block = ?2")
//    @Modifying
//    void cleanOld(String pubkey, String block);
//
//    @Query("DELETE FROM Peer p WHERE ?1 id NOT IN (SELECT id, max(block) FROM Peer p1 GROUP BY pubkey) ")
//    @Modifying
//    void cleanupNotIn(Integer currentBlock);

    @Query("SELECT new juniter.core.model.dbo.net.Max(pubkey, MAX(p.block.number)) FROM Peer p GROUP BY pubkey")
    List<Max> maxByPeer();


    Optional<Peer> findByPubkey(String pubkey);

    @Transactional
    @Modifying
    default void cleanup(Integer currentBlock) {
        maxByPeer().forEach(m -> {
            clean(m.getPubkey(), m.getMax());
            if (m.getMax() < currentBlock - 500) {
                findByPubkey(m.getPubkey()).ifPresent(this::delete);
                //cleanOld(m.getPubkey(), m.getMax());
            }
        });
    }


}