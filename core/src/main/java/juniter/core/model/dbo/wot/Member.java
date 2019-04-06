package juniter.core.model.dbo.wot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.dbo.BStamp;
import juniter.core.model.dbo.DUPDocument;
import juniter.core.model.dbo.DenormalizeSignatureStamp;
import juniter.core.model.dbo.DenormalizeWrittenStamp;
import juniter.core.utils.Constants;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@Table(name = "wot_member", schema = "public", indexes = {
        @Index(columnList = "pubkey"),
        @Index(columnList = "uid"),
        @Index(columnList = "writtenOn")
})
@Inheritance(strategy = InheritanceType.JOINED)

@JsonIgnoreProperties(ignoreUnknown = true)
public class Member implements DUPDocument, Serializable, Comparable<Member>, DenormalizeWrittenStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Pattern(regexp = Constants.Regex.PUBKEY)
    protected String pubkey;

    protected String signature;

    protected Integer signedOn;

    protected String signedHash;

    protected Long signedTime;

    protected String i_block_uid;

    protected String uid;

    protected String revoked_on;

    protected String revocation;

    protected Boolean excluded;

    protected Integer writtenOn;

    protected String writtenHash;

    protected Long writtenTime;

    @Override
    public int compareTo(@NonNull Member o) {
        return pubkey.compareTo(o.pubkey);
    }

    @Override
    public String toDUP() {
        return pubkey + ":" + signature + ":" + signedOn + "-" + signedHash + ":" + i_block_uid + ":" + uid;
    }

    public BStamp createdOn() {
        return new BStamp(signedHash);
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
