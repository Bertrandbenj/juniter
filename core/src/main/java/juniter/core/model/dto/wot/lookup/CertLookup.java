package juniter.core.model.dto.wot.lookup;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CertLookup {
    String pubkey;
    String uid;
    String sigDate;
    boolean isMember;
    List<CertificationSection> certifications;

}
