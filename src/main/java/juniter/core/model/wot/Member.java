package juniter.core.model.wot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.BStamp;
import juniter.core.model.DUPComponent;
import juniter.core.utils.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Embeddable
@Table(name = "wot_member", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class Member implements DUPComponent, Serializable, Comparable<Member> {

    private static final Logger LOG = LogManager.getLogger();

    private static final long serialVersionUID = 4413010134975591059L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Pattern(regexp = Constants.Regex.PUBKEY)
    protected String pubkey;

    protected String signature;

    protected String createdOn;

    protected String i_block_uid;

    protected String uid;

    protected String revoked_on;

    protected String revocation;



    protected String type;

    protected Boolean excluded ;

    protected Member (String member){
        parse(member);
    }


    @Override
    public int compareTo(@NonNull Member o) {
        return pubkey.compareTo(o.pubkey);
    }

    @Override
    public String toDUP() {
        return pubkey + ":" + signature + ":" + createdOn + ":" + i_block_uid + ":" + uid;
    }

    public BStamp createdOn() {
        return new BStamp(createdOn);
    }
    public BStamp idtyOn() {
        return new BStamp(i_block_uid);
    }
    public BStamp revokedOn() {
        return new BStamp(revoked_on);
    }



    @Override
    public String toString() {
        return toDUP();
    }


    public abstract void parse(String joiner);


}
