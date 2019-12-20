package juniter.core.model.dto.wot.requirements;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IdtyCerts {

    String from;
    String to;
    String sig ;
    Long timestamp;
    Long expiresIn;

}
