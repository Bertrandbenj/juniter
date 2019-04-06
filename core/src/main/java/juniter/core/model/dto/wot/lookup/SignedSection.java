package juniter.core.model.dto.wot.lookup;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class SignedSection implements Serializable {
    private String pubkey;
    private String uid;
    private MetaLookupString meta;
    private BnumSection cert_time;
    private boolean isMember;
    private boolean wasMember;
    private String signature;
}