package juniter.service.gva.tx;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Source {
    String type;
    Integer noffset;
    String identifier;
    Integer amount;
    Integer base;
    String conditions;
    Boolean consumed;
}
