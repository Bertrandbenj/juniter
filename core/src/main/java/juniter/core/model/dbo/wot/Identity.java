package juniter.core.model.dbo.wot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.dbo.BStamp;
import juniter.core.model.dbo.DUPDocument;
import juniter.core.model.dbo.DenormalizeWrittenStamp;
import juniter.core.utils.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
@Table(name = "wot_identity", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Identity implements DUPDocument, Comparable<Identity>, DenormalizeWrittenStamp {
    private static final Logger LOG = LogManager.getLogger();

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
    @AttributeOverrides({
            @AttributeOverride(name = "number", column = @Column(name = "signedOn")),
            @AttributeOverride(name = "hash", column = @Column(name = "signedHash")),
            @AttributeOverride(name = "medianTime", column = @Column(name = "signedTime"))
    })
    private BStamp signed;

    private String uid;

    private Integer writtenOn;

    private String writtenHash;

    private Long writtenTime;

    public Identity(String identity) {
        LOG.debug("Parsing Identity... " + identity);
        final var vals = identity.split(":");
        pubkey = vals[0];
        signature = vals[1];
        signed = new BStamp(vals[2]);
        uid = vals[3];
    }

    @Override
    public int compareTo(@NonNull Identity o) {
        return pubkey.compareTo(o.pubkey);
    }


    public String toDUP() {
        return pubkey + ":" + signature + ":" + signed + ":" + uid;
    }

    @Override
    public String toString() {
        return toDUP();
    }

}
