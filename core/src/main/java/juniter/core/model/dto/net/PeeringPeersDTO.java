package juniter.core.model.dto.net;

import lombok.Data;

import java.util.List;

@Data
public class PeeringPeersDTO  {


    private Integer depth;

    private Integer nodeCounts;

    private Integer leavesCount;

    private String root;

    private List<String> leaves;

    private LeafDTO leaf;


}
