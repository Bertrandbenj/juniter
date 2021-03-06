package juniter.core.model.dbo.sandbox;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.dbo.BStamp;
import juniter.core.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sb_member", schema = "public")
@Inheritance(strategy = InheritanceType.JOINED)

@JsonIgnoreProperties(ignoreUnknown = true)
public class MemberSandboxed implements   Serializable, Comparable<MemberSandboxed> {

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
    public int compareTo(@NonNull MemberSandboxed o) {
        return pubkey.compareTo(o.pubkey);
    }



    public String toDUPdoc(boolean signed) {

        return "";
    }



}
