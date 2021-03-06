package juniter.core.model.dbo.sandbox;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.dbo.BStamp;
import juniter.core.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * pubkey : signature : buid : buid : userid;
 *
 * @author ben
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "sb_identity", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdentitySandboxed implements  Comparable<IdentitySandboxed> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 45)
    @Pattern(regexp = Constants.Regex.PUBKEY)
    private String pubkey;

    @Valid
    @Size(max = 88)
    @Pattern(regexp = Constants.Regex.SIGNATURE)
    private String signature;

    @Valid
    private BStamp signed;

    private String uid;

    @Valid
    private BStamp written;

    public IdentitySandboxed(String identity) {
        final var vals = identity.split(":");
        pubkey = vals[0];
        signature = vals[1];
        signed = new BStamp(vals[2]);
        uid = vals[3];
    }

    @Override
    public int compareTo(@NonNull IdentitySandboxed o) {
        return pubkey.compareTo(o.pubkey);
    }



}
