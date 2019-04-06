package juniter.core.model.dto.raw;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class WrapperRevocation implements Wrapper{

    String revocation;

    public WrapperRevocation(String rawDoc) {
        this.revocation = rawDoc;
    }
}