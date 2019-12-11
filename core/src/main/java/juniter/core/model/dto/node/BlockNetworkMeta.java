package juniter.core.model.dto.node;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BlockNetworkMeta {

    private Integer number;
    private Integer issuersFrame;
    private Integer issuersFrameVar;
    private Integer powMin;
    private Long medianTime;

}