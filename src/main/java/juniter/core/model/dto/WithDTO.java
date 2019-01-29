package juniter.core.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WithDTO implements Serializable {

    private static final long serialVersionUID = -6551775706995959038L;

    private Result result;

    public WithDTO(List<Integer> blocks) {
        super();
        this.result = new Result(blocks);
    }


    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Result implements Serializable {

        private static final long serialVersionUID = 8301420797082933436L;

        private List<Integer> blocks;

    }

}
