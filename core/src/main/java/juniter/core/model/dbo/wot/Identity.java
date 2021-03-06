package juniter.core.model.dbo.wot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.dbo.BStamp;
import juniter.core.model.meta.DUPIdentity;
import juniter.core.utils.Constants;
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
@Table(name = "wot_identity", schema = "public", indexes = {
        @Index(columnList = "uid"),
        @Index(columnList = "pubkey"),
        @Index(columnList = "written_number"),
        @Index(columnList = "written_medianTime"),
        @Index(columnList = "signed_medianTime"),
        @Index(columnList = "signed_number")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Identity implements DUPIdentity, Comparable<Identity> {

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

    private Short version;

    public Identity(String identity) {
        final var vals = identity.split(":");
        pubkey = vals[0];
        signature = vals[1];
        signed = new BStamp(vals[2]);
        uid = vals[3];
        version = 10;
    }

    public Identity(String pubkey, String signature, String signed, String uid, String written) {
        this.pubkey = pubkey;
        this.signature = signature;
        this.signed = new BStamp(signed);
        this.uid = uid;
    }

    @Override
    public int compareTo(@NonNull Identity o) {
        return pubkey.compareTo(o.pubkey);
    }


    public String toDUP() {
        return pubkey + ":" + signature + ":" + signed.stamp() + ":" + uid;
    }


    @Override
    public String toString() {
        return toDUP();
    }

}
