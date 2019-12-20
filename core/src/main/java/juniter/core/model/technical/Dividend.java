package juniter.core.model.technical;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dividend{
    public Dividend(Integer number, Long medianTime, Integer dividend) {
        this.number = number;
        this.medianTime = medianTime;
        this.dividend = dividend;
    }

    private Integer number;
    private Long medianTime;
    private Integer dividend;
    private Integer base = 0 ;
    private Boolean consumed = false;
}