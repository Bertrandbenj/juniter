package juniter.core.model.dto.naughtylookup;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class OtherLookup implements Serializable {
    private String pubkey;
    private MetaLookup meta;
    private List<String> uids;
    private boolean isMember;
    private boolean wasMember;
    private String signature;
}