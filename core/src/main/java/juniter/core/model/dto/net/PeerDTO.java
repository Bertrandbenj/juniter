package juniter.core.model.dto.net;

import juniter.core.model.dbo.net.EndPoint;
import lombok.Data;

import java.util.List;


@Data
public class PeerDTO {

    private Integer version;

    private String currency;

    private String status;

    private String first_down;

    private String last_try;

    private String pubkey;

    private String block;

    private String signature;

    private List<EndPoint> endpoints;

//    public List<String> getEndpoints() {
//        return endpoints.stream().map(EndPoint::getEndpoint).collect(Collectors.toList());
//    }
}
