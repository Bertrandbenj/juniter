package juniter.core.model.dto.node;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class WithDTO  {

    private Result result;

    public WithDTO(List<Integer> blocks) {
        super();
        this.result = new Result(blocks);
    }


    @Data
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Result  {

        private List<Integer> blocks;

    }

}
