package juniter.core.model.dto.wot.requirements;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReqDTO {

    List<ReqIdtyDTO> identities;

}
