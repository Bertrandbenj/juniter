package juniter.core.model.dto.requirements;

import juniter.core.model.dto.naughtylookup.MetaLookupString;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ReqIdtyDTO {
    String pubkey;
    String uid;
    String sig;
    MetaLookupString meta;
    String revocation_sig;
    String revoked_on;
    Boolean revoked;
    Boolean expired;
    Boolean outdistanced;
    Boolean isSentry;
    Boolean wasMember;
    List<IdtyCerts> certifications;
    List<IdtyCerts> pendingCerts;
    List<IdtyCerts> pendingMemberships;
    Integer membershipPendingExpiresIn ;
    Integer membershipExpiresIn;

}
