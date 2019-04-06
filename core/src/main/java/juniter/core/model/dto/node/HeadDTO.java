package juniter.core.model.dto.node;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HeadDTO  {

    private String message;
    private String sig;
    private String messageV2;
    private String sigV2;
    private Integer step;
}