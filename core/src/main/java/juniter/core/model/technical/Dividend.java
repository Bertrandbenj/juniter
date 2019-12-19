package juniter.core.model.technical;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dividend{
    private Integer number;
    private Long medianTime;
    private Integer dividend;
}