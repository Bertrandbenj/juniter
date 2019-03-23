package juniter.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public class NodeSummaryDTO  {

    private Card duniter;


    public NodeSummaryDTO() {
        duniter = new Card("juniter", "1.1.0", 66);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    class Card implements Serializable {

        private static final long serialVersionUID = -6400285666088830671L;

        private String software;
        private String version;
        private Integer forkWindowSize;

    }
}
