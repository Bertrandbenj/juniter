package juniter.core.model.dto;


import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Head implements Serializable {
    private String message;
    private String sig;
    private String messageV2;
    private String sigV2;
    private Integer step;
}