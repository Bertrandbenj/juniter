package juniter.core.model.dbo.index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.crypto.Crypto;
import juniter.core.model.dbo.BStamp;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.function.BinaryOperator;

import static juniter.core.validation.GlobalValid.wotbidIncrement;

/**
 * <pre>
 * * UserID and PublicKey unicity
 *
 * The local IINDEX has a unicity constraint on USER_ID.
 * The local IINDEX has a unicity constraint on PUBLIC_KEY.
 *
 *  Each local IINDEX op = 'CREATE' operation  must match a single local MINDEX
 *      op = 'CREATE',
 *      pub = PUBLIC_KEY operation.
 *
 * Functionally:
 *
 *     UserID and public key must be unique in a block,
 *     each new identity must have an opt-in document attached.
 * </pre>
 */
@Data
@NoArgsConstructor
//@AllArgsConstructor
@Entity
@Table(name = "IINDEX", schema = "public", indexes = {
        @Index(columnList = "uid"),
        @Index(columnList = "op"),
        @Index(columnList = "pub"),
        @Index(columnList = "written_number"),
        @Index(columnList = "signed_number")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class IINDEX implements Comparable<IINDEX> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String op;

    private String uid;

    private String pub;

    private String hash;

    private String sig;

    private BStamp signed;

    private BStamp written;

    private Boolean member;

    private Boolean wasMember;

    private Boolean kick;

    private Integer wotbid;


    // Transient variable local to the Block we are validating
    transient long age;
    transient boolean uidUnique;
    transient boolean pubUnique;
    transient boolean excludedIsMember;
    transient boolean isBeingKicked;
    transient boolean hasToBeExcluded;

    public IINDEX(String op, String pub, BStamp written_on, boolean kick) {
        this.op = op;
        this.pub = pub;
        this.written = written_on;
        this.kick = kick;
    }

    public IINDEX(String op, String uid, String pub, BStamp created_on, BStamp written_on, boolean member,
                  Boolean wasMember, boolean kick, String sig) {
        this.op = op;
        this.uid = uid;
        this.pub = pub;
        this.signed = created_on;
        this.written = written_on;
        this.member = member;
        this.wasMember = wasMember;
        this.kick = kick;
        this.sig = sig;
        this.hash = Crypto.hash(uid + created_on + pub);

        if (uid != null) {
            this.wotbid = wotbidIncrement.getAndIncrement();
        }

    }

    @Override
    public int compareTo(@NonNull IINDEX o) {
        return pub.compareTo(o.pub);
    }


    public static final BinaryOperator<IINDEX> reducer = (i1, i2) -> {

        IINDEX bot, top;
        if (i1.written.getNumber() < i2.written.getNumber()) {
            top = i1;
            bot = i2;
        } else {
            top = i2;
            bot = i1;
        }

        if (top.getSigned() == null)
            top.setSigned(bot.getSigned());
        if (top.getHash() == null)
            top.setHash(bot.getHash());
        if (top.getSig() == null)
            top.setSig(bot.getSig());
        if (top.getMember() == null)
            top.setMember(bot.getMember());
        if (top.getWasMember() == null)
            top.setWasMember(bot.getWasMember());
        if (top.getKick() == null)
            top.setKick(bot.getKick());
        if (top.getWotbid() == null)
            top.setWotbid(bot.getWotbid());
        if (top.getUid() == null)
            top.setUid(bot.getUid());


        return top;
    };


}