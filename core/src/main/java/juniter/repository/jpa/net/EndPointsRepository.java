package juniter.repository.jpa.net;

import juniter.core.model.dbo.net.EndPoint;
import juniter.core.model.dbo.net.EndPointType;
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


    @Query("select ep from EndPoint ep where api IN (:type)  ")
    List<EndPoint> get(EndPointType... type);

    @Query("select ep from EndPoint ep where api = 'WS2P'  ")
    Stream<EndPoint> endpointsWS2P();

    @Query("select ep from EndPoint ep where api = 'WS2P'  ")
    List<EndPoint> endpointssWS2P();

    default List<String> enpointsURL() {
        return endpointsBMAS()
                .filter(ep -> ep.getDomain() != null)
                .map(EndPoint::url)
                .collect(Collectors.toList());
    }

    @Query("select ep from EndPoint ep where peer = '%1' AND endpoint = '%2'  ")
    Optional<EndPoint> findByPeerAndEndpoint(String pubkey, String endpoint);


}