package juniter.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Source {
    String type;
    Integer noffset;
    String identifier;
    Integer amount;
    Integer base;
    String conditions;


}
