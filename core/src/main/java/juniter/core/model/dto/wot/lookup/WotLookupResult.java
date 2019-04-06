package juniter.core.model.dto.wot.lookup;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class WotLookupResult  {
    private String pubkey;
    private List<UserID> uids;
    private List<SignedSection> signed;
}