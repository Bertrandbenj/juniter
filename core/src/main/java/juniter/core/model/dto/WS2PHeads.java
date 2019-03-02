package juniter.core.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

//@Builder
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WS2PHeads implements Serializable {

    private static final long serialVersionUID = -4464417074568456696L;

    private List<HeadDTO> heads;


}
