package juniter.service.bma.dto;

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


    public Source(String type, Integer noffset, String identifier, Integer amount, Integer base, String conditions) {
        this.type = type;
        this.noffset = noffset;
        this.identifier = identifier;
        this.amount = amount;
        this.base = base;
        this.conditions = conditions;
    }
}
