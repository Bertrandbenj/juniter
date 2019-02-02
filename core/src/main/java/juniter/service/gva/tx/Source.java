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


    public Source(String type, Integer noffset, String identifier, Integer amount, Integer base, String conditions, Boolean consumed) {
        this.type = type;
        this.noffset = noffset;
        this.identifier = identifier;
        this.amount = amount;
        this.base = base;
        this.conditions = conditions;
        this.consumed = consumed;
    }
}
