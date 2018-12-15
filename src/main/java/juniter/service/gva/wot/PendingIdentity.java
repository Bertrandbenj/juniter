package juniter.service.gva.wot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.leangen.graphql.annotations.GraphQLNonNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PendingIdentity  {

    @GraphQLNonNull Boolean revoked;
    String buid;
    Boolean member;
    Boolean kick;
    Boolean leaving;
    Boolean wasMember;
    String pubkey;
    String uid;
    String sig;
    String revocation_sig;
    String hash;
    Boolean written;
    Integer revoked_on;
    Integer expires_on;
    List<PendingCertification> certs;
    List<PendingMembership> memberships;

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class PendingCertification {
        Boolean linked;
        Boolean written;
        Integer written_block;
        String  written_hash;
        String sig;
        Integer  block_number;
        String block_hash;
        String target;
        String to;
        String from;
        Integer block;
        Boolean expired;
        Integer expires_on;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class PendingMembership {
        String membership;
        String issuer;
        Integer number;
        Integer blockNumber;
        String blockHash;
        String userid;
        String certts;
        String block;
        String fpr;
        String idtyHash;
        Boolean written;
        Integer written_number;
        Integer expires_on;
        String signature;
        Boolean expired;
        Integer block_number;
    }

}
