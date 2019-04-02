package juniter.repository.jpa.net;

import juniter.core.model.dbo.net.EndPoint;
import juniter.core.model.dbo.net.Peer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public interface EndPointsRepository extends JpaRepository<EndPoint, Long> {

	@Query("select ep from EndPoint ep where api = 'BMAS' ")
	Stream<EndPoint> endpointsBMAS();

	@Query("select ep from EndPoint ep where api = 'BMAS' ")
	List<EndPoint> endpointssBMAS();


	@Query("select ep from EndPoint ep where api = 'WS2P'  ")
	Stream<EndPoint> endpointsWS2P();

	default List<String> enpointsURL() {
		return endpointsBMAS().filter(ep -> ep.getDomain() != null).map(ep -> ep.url()).collect(Collectors.toList());
	}

	Optional<EndPoint> findByPeerAndEndpoint(Peer pubkey, String ep_string);

	@Override
	<S extends EndPoint> S save(S endpoint);

	@Override
	<S extends EndPoint> List<S> saveAll(Iterable<S> entities);
//	@Override
//	default <S extends EndPoint> List<S> saveAll(Iterable<S> entities){
//		NetworkService.LOG.info("Saving stuff ");
//		return super.saveAll(entities);
//	};

}