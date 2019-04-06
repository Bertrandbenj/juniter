package juniter.core.model.dto.net;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.dto.node.HeadDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WS2PHeads  {

    private List<HeadDTO> heads;


}
