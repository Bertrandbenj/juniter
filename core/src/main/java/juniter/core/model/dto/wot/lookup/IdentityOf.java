package juniter.core.model.dto.wot.lookup;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IdentityOf {
    String pubkey;
    String uid;
    String sigDate;
}
