package juniter.core.model.dto.wot.lookup;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class BnumSection  implements Serializable {
    private Integer block;
}
