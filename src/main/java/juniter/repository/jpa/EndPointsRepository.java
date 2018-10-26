package juniter.repository.jpa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import juniter.core.model.net.EndPoint;
import juniter.core.model.net.Peer;

@Repository
public interface EndPointsRepository extends JpaRepository<EndPoint, Long> {

	@Query("select ep from EndPoint ep where api = 'BMAS' and port = '443' ")
	Stream<EndPoint> endpointsBMA();

	@Query("select ep from EndPoint ep where api = 'WS2P'  ")
	Stream<EndPoint> endpointsWS2P();

	default List<String> enpointsURL() {
		return endpointsBMA().filter(ep -> ep.getDomain() != null).map(ep -> ep.url()).collect(Collectors.toList());
	}

	Optional<EndPoint> findByPeerAndEndpoint(Peer pubkey, String ep_string);

	@Override
	<S extends EndPoint> S save(S endpoint);

	@Override
	<S extends EndPoint> List<S> saveAll(Iterable<S> entities);
//	@Override
//	default <S extends EndPoint> List<S> saveAll(Iterable<S> entities){
//		PeeringService.LOG.info("Saving stuff ");
//		return super.saveAll(entities);
//	};

}