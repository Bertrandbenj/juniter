package juniter.core.model.dbo.wot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.dbo.BStamp;
import juniter.core.model.dbo.DUPDocument;
import juniter.core.utils.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@Table(name = "wot_member", schema = "public", indexes = {
        @Index(name = "ind_mepub", columnList = "pubkey"),
        @Index(name = "ind_meuid", columnList = "uid")
})
@Inheritance(strategy= InheritanceType.JOINED)

@JsonIgnoreProperties(ignoreUnknown = true)
public class Member implements DUPDocument, Serializable, Comparable<Member> {
    private static final long serialVersionUID = -9093525414764126719L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Pattern(regexp = Constants.Regex.PUBKEY)
    protected String pubkey;

    protected String signature;

    protected String createdOn;

    protected String i_block_uid;

    protected String uid;

    protected String revoked_on;

    protected String revocation;

//    protected String type;

    protected Boolean excluded ;


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




}
