package juniter.core.model.dto.node;

import lombok.*;

import java.io.Serializable;


@Data
public class NodeSummaryDTO  {

    private Card duniter;


    public NodeSummaryDTO() {
        duniter = new Card("juniter", "1.1.0", 66);
    }

    @Data
    @AllArgsConstructor
    class Card  {

        private String software;
        private String version;
        private Integer forkWindowSize;

    }
}