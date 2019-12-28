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


    @Query("select ep from EndPoint ep where api IN (:type)  ")
    List<EndPoint> get(EndPointType... type);

    default List<String> getUrls(EndPointType... type) {
        return get(type).stream()
                .map(EndPoint::url)
                .collect(Collectors.toList());
    }

}