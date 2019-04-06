package juniter.core.model.dto.raw;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class WrapperIdentity implements Wrapper {

    String identity;

    public WrapperIdentity(String rawDoc) {
        this.identity = rawDoc;
    }

}