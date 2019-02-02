package juniter.core.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PeeringPeersDTO implements Serializable {

    private static final long serialVersionUID = -4464417074954356696L;

    private Integer depth;

    private Integer nodeCounts;

    private Integer leavesCount;

    private String root;

    private List<String> leaves;

    private LeafDTO leaf;


}
