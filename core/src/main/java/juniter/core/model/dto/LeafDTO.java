package juniter.core.model.dto;

import juniter.core.model.dbo.net.Peer;
import lombok.*;


@Data
@AllArgsConstructor
public class LeafDTO  {

    private String hash ;

    private Peer value;

}
