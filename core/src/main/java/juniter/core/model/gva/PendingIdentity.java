package juniter.core.model.gva;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.leangen.graphql.annotations.GraphQLNonNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PendingIdentity {

    private Boolean revoked;
    private String buid;
    private Boolean member;
    private Boolean kick;
    private Boolean leaving;
    private Boolean wasMember;
    private String pubkey;
    private String uid;
    private String sig;
    private String revocation_sig;
    private String hash;
    private Boolean written;
    private Integer revoked_on;
    private Integer expires_on;
    private List<PendingCertification> certs;
    private List<PendingMembership> memberships;


    @GraphQLNonNull
    public String getBuid() {
        return buid;
    }

    @GraphQLNonNull
    public Boolean getMember() {
        return member;
    }

    @GraphQLNonNull
    public Boolean getKick() {
        return kick;
    }

    @GraphQLNonNull
    public Boolean getWasMember() {
        return wasMember;
    }

    @GraphQLNonNull
    public String getPubkey() {
        return pubkey;
    }

    @GraphQLNonNull
    public String getUid() {
        return uid;
    }

    @GraphQLNonNull
    public String getSig() {
        return sig;
    }

    @GraphQLNonNull
    public String getHash() {
        return hash;
    }

    @GraphQLNonNull
    public Boolean getWritten() {
        return written;
    }

    @GraphQLNonNull
    public Integer getExpires_on() {
        return expires_on;
    }

    @GraphQLNonNull
    public List<@GraphQLNonNull PendingCertification> getCerts() {
        return certs;
    }

    @GraphQLNonNull
    public List<@GraphQLNonNull PendingMembership> getMemberships() {
        return memberships;
    }

    @GraphQLNonNull
    public Boolean getRevoked() {
        return revoked;
    }


}
