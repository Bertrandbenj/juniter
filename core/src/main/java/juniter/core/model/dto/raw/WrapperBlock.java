package juniter.core.model.dto.raw;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class WrapperBlock implements Wrapper{

    String block;

    public WrapperBlock(String rawDoc) {
        this.block = rawDoc;
    }
}