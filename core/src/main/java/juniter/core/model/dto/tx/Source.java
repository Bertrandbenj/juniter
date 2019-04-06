package juniter.core.model.dto.tx;

import lombok.*;

@Data
@AllArgsConstructor
public class Source {
    String type;
    Integer noffset;
    String identifier;
    Integer amount;
    Integer base;
    String conditions;


}
