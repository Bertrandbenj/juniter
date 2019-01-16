package juniter.core.model.wot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.BStamp;
import juniter.core.utils.Constants;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * Pubkey : signature : buid : buid : pseudo;
 *
 * @author ben
 */
@Entity
@Getter
@Setter
@Table(name = "identity", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Identity implements Serializable, Comparable<Identity> {
    private static final Logger LOG = LogManager.getLogger();

    private static final long serialVersionUID = -9160916061297193207L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(max = 45)
    @NonNull
    @Pattern(regexp = Constants.Regex.PUBKEY)
    private String newidentity;

    @Valid
    @Size(max = 88)
    @Pattern(regexp = Constants.Regex.SIGNATURE)
    private String signature;

    @Valid
    @AttributeOverride(name = "buid", column = @Column(name = "createdOn"))
    private BStamp createdOn = new BStamp();

    private String pseudo;

    public Identity() {

    }

    public Identity(String identity) {
        LOG.debug("Parsing Identity... " + identity);
        final var vals = identity.split(":");
        newidentity = vals[0];
        signature = vals[1];
        createdOn.parse(vals[2]);
        pseudo = vals[3];
    }

    @Override
    public int compareTo(@NonNull Identity o) {
        return newidentity.compareTo(o.newidentity);
    }

    public BStamp createdOn() {
        return createdOn;
    }

    public String pseudo() {
        return pseudo;
    }

    public String signature() {
        return signature;
    }


    public String pub() {
        return newidentity;
    }

    public String toDUP() {
        return newidentity + ":" + signature + ":" + createdOn + ":" + pseudo;
    }

    @Override
    public String toString() {
        return toDUP();
    }

}
