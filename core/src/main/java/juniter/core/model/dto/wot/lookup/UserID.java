package juniter.core.model.dto.wot.lookup;

import juniter.core.model.dbo.BStamp;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserID {
    private String uid;
    private MetaLookup meta;
    private boolean revoked;
    private BStamp revoked_on;
    private String revocation_sig;
    private String self; // signature
    private List<OtherLookup> others;
}