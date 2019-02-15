package juniter.repository.jpa;

import juniter.core.model.business.net.Peer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
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

	@Query("select p from Peer p where block = ?1 ")
	Stream<Peer> peerWithBlock(String s);
}