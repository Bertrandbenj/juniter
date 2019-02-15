package juniter.service.gva;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.leangen.graphql.annotations.GraphQLNonNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
