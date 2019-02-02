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

    Boolean revoked;
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

        @GraphQLNonNull
        public Boolean getLinked() {
            return linked;
        }

        @GraphQLNonNull
        public Boolean getWritten() {
            return written;
        }

        @GraphQLNonNull
        public String getSig() {
            return sig;
        }

        @GraphQLNonNull
        public Integer getBlock_number() {
            return block_number;
        }

        @GraphQLNonNull
        public String getBlock_hash() {
            return block_hash;
        }

        @GraphQLNonNull
        public String getTarget() {
            return target;
        }

        @GraphQLNonNull
        public String getTo() {
            return to;
        }

        @GraphQLNonNull
        public String getFrom() {
            return from;
        }

        @GraphQLNonNull
        public Integer getBlock() {
            return block;
        }

        @GraphQLNonNull
        public Integer getExpires_on() {
            return expires_on;
        }
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

        @GraphQLNonNull
        public String getMembership() {
            return membership;
        }

        @GraphQLNonNull
        public String getIssuer() {
            return issuer;
        }

        @GraphQLNonNull
        public Integer getNumber() {
            return number;
        }

        @GraphQLNonNull
        public Integer getBlockNumber() {
            return blockNumber;
        }

        @GraphQLNonNull
        public String getBlockHash() {
            return blockHash;
        }

        @GraphQLNonNull
        public String getUserid() {
            return userid;
        }

        @GraphQLNonNull
        public String getCertts() {
            return certts;
        }

        @GraphQLNonNull
        public String getBlock() {
            return block;
        }

        @GraphQLNonNull
        public String getFpr() {
            return fpr;
        }

        @GraphQLNonNull
        public String getIdtyHash() {
            return idtyHash;
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
        public String getSignature() {
            return signature;
        }

        @GraphQLNonNull
        public Integer getBlock_number() {
            return block_number;
        }
    }

}
