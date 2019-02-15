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
public class PendingCertification {
    Boolean linked;
    Boolean written;
    Integer written_block;
    String written_hash;
    String sig;
    Integer block_number;
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
