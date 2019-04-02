package juniter.core.model.dto;

import juniter.core.model.dbo.net.EndPoint;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;


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

    private List<String> endpoints;

    public void setEndpoints(List<EndPoint> eps ){
        endpoints = eps.stream().map(EndPoint::getEndpoint).collect(Collectors.toList());
    }

}
