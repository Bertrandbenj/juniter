package juniter.core.model.dto.wot.lookup;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class OtherBnumSection implements Serializable {
    private Integer block_number;
    private String block_hash;
}
