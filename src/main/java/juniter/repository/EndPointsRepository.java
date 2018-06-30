package juniter.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import juniter.model.net.EndPoint;
import juniter.model.net.Peer;

@Repository
public interface EndPointsRepository extends JpaRepository<EndPoint, Long> {
	@Override
	<S extends EndPoint> S save(S endpoint);
	
	Optional<EndPoint> findByPeerAndEndpoint(Peer pubkey, String ep_string);
	
	@Query("select ep from EndPoint ep where api = 'BMAS' and port = '443' ")
	Stream<EndPoint> streamUsableEndpoints();
	
	default List<String> enpointsURL(){
		return streamUsableEndpoints()
				.filter(ep-> ep.getDomain() != null)
				.map(ep -> ep.url())
				.collect(Collectors.toList());
	};
	
	@Override
	<S extends EndPoint> List<S> saveAll(Iterable<S> entities);
//	@Override
//	default <S extends EndPoint> List<S> saveAll(Iterable<S> entities){
//		PeeringService.logger.info("Saving stuff ");
//		return super.saveAll(entities);
//	};
}