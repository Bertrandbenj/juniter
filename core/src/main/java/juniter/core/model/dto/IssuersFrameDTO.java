package juniter.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class IssuersFrameDTO   {

    private Integer number;
    private Integer issuersFrame;
    private Integer issuersFrameVar;
    private Integer powMin;
    private Long medianTime;

}