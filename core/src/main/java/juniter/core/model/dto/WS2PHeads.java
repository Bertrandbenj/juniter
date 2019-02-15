package juniter.core.model.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Builder
@Data
public class WS2PHeads implements Serializable {

    private List<Head> heads;


}
