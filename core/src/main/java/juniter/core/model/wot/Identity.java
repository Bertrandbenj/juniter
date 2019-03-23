package juniter.core.model.wot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.business.BStamp;
import juniter.core.model.business.DUPComponent;
import juniter.core.utils.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Pubkey : signature : buid : buid : uid;
 *
 * @author ben
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "wot_identity", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Identity implements DUPComponent,Comparable<Identity> {
    private static final Logger LOG = LogManager.getLogger();

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 45)
    @NonNull
    @Pattern(regexp = Constants.Regex.PUBKEY)
    private String pubkey;

    @Valid
    @Size(max = 88)
    @Pattern(regexp = Constants.Regex.SIGNATURE)
    private String signature;

    @Valid
    @AttributeOverride(name = "buid", column = @Column(name = "createdOn"))
    private BStamp createdOn  ;

    private String uid;


    public Identity(String identity) {
        LOG.debug("Parsing Identity... " + identity);
        final var vals = identity.split(":");
        pubkey = vals[0];
        signature = vals[1];
        createdOn = new BStamp(vals[2]);
        uid = vals[3];
    }

    @Override
    public int compareTo(@NonNull Identity o) {
        return pubkey.compareTo(o.pubkey);
    }


    public String toDUP() {
        return pubkey + ":" + signature + ":" + createdOn + ":" + uid;
    }

    @Override
    public String toString() {
        return toDUP();
    }

}
