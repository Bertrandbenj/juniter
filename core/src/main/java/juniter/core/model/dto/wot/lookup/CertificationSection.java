package juniter.core.model.dto.wot.lookup;

import juniter.core.model.dbo.BStamp;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CertificationSection {

    String pubkey;
    String uid;
    Boolean isMember;
    Boolean wasMember;
    Certtime cert_time;
    String sigDate;
    BStamp written;
    String signature;


}
