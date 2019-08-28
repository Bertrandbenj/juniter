package juniter.core.model.dbo.wot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.dbo.BStamp;
import juniter.core.model.dbo.DUPDocument;
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
        @Index(columnList = "written_number"),
        @Index(columnList = "signed_number"),
        @Index(columnList = "excluded")
})
@Inheritance(strategy = InheritanceType.JOINED)

@JsonIgnoreProperties(ignoreUnknown = true)
public class Member implements DUPDocument, Serializable, Comparable<Member> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Pattern(regexp = Constants.Regex.PUBKEY)
    protected String pubkey;

    protected String signature;

    protected BStamp signed;

    protected String i_block_uid;

    protected String uid;

    protected String revoked_on;

    protected String revocation;

    protected Boolean excluded;

    protected BStamp written;

    @Override
    public int compareTo(@NonNull Member o) {
        return pubkey.compareTo(o.pubkey);
    }

    @Override
    public String toDUP() {
        return pubkey + ":" + signature + ":" + signed.getNumber() + "-" + signed.getHash() + ":" + i_block_uid + ":" + uid;
    }

    public String toDUPdoc(boolean signed) {

        return "";
    }


    @Override
    public String toString() {
        return toDUP();
    }


}
