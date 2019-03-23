package juniter.core.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PeeringPeersDTO  {


    private Integer depth;

    private Integer nodeCounts;

    private Integer leavesCount;

    private String root;

    private List<String> leaves;

    private LeafDTO leaf;


}
