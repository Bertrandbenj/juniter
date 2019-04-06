package juniter.core.model.dto.raw;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class WrapperMembership implements Wrapper {

    String membership;

    public WrapperMembership(String rawDoc) {
        this.membership = rawDoc;
    }
}