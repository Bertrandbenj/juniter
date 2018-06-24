package juniter.repository;

import java.util.List;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import juniter.model.Peer;

/**
 * Repository to manage {@link Peer} instances.
 */
@Repository
public interface PeersRepository extends JpaRepository<Peer, Long> {

	
	@Override
	<S extends Peer> S save(S peer);
	
	 @Transactional
	default Peer saveOrCreate(Peer peer) {
		return save(findByPubkey(peer.getPubkey())//
					.stream().findFirst().orElse(peer));
	};
	
	
	@Override
	<S extends Peer> List<S> saveAll(Iterable<S> arg0);
	

	List<Peer> findByPubkey(String pubkey);
	
	List<Peer> findByPubkeyOrderByBlockDesc(String lastname);
	
	
//	@Override 
//	default <S extends Peer> List<S>  saveAll(Iterable<S> peers){
//		return Stream.of(peers).filter(arg0);
//	};

	@Query("select p from Peer p")
	Stream<Peer> streamAllPeers();



		

}