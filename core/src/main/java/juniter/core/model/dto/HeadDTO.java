package juniter.core.model.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
//@Builder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeadDTO implements Serializable {
     private static final long serialVersionUID = -4464417074968459696L;

    private String message;
    private String sig;
    private String messageV2;
    private String sigV2;
    private Integer step;
}