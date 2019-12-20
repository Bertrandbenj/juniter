package juniter.core.model.dto.tx;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public
class UdDTO {

    Long time ;
    Integer block_number;
    Boolean consumed ;
    Integer amount;
    Integer base;

}
