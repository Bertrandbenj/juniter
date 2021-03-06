package juniter.core.model.dto.wot.lookup;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OtherLookup  {
    private String pubkey;
    private OtherBnumSection meta;
    private List<String> uids;
    private boolean isMember;
    private boolean wasMember;
    private String signature;
}