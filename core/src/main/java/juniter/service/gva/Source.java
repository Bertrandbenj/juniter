package juniter.service.gva;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Source {
    private String type;
    private Integer noffset;
    private String identifier;
    private Integer amount;
    private Integer base;
    private String conditions;
    private Boolean consumed;

}
