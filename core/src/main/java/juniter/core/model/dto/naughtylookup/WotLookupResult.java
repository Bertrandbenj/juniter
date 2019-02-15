package juniter.core.model.dto.naughtylookup;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class WotLookupResult implements Serializable {
    private String pubkey;
    private List<UserID> uids;
    private List<SignedSection> signed;
}