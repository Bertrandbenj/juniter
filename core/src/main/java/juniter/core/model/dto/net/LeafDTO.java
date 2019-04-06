package juniter.core.model.dto.net;

import juniter.core.model.dbo.net.Peer;
import lombok.*;


@Data
@AllArgsConstructor
public class LeafDTO  {

    private String hash ;

    private Peer value;

}
