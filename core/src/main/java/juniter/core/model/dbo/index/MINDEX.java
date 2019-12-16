package juniter.core.model.dbo.index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.dbo.BStamp;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.function.BinaryOperator;

/**
 * <pre>
 * * MembershipDTO unicity
 *
 * The local MINDEX has a unicity constraint on PUBLIC_KEY
 *
 * Functionally: a user has only 1 status change allowed per block.
 *
 *
 *
 * Revocation implies exclusion
 *
 * Each local MINDEX ̀op = 'UPDATE', revoked = BLOCKSTAMP
 *
 * operations must match a single local IINDEX
 *
 * op = 'UPDATE', pub = PUBLIC_KEY, member = false operation.
 *
 * Functionally: a revoked member must be immediately excluded.
 *
 * </pre>
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "index_m", schema = "public", indexes = {
        @Index(columnList = "op"),
        @Index(columnList = "pub"),
        @Index(columnList = "written_number"),
        @Index(columnList = "expires_on"),
        @Index(columnList = "revoked_number"),
        @Index(columnList = "signed_number"),
        @Index(columnList = "revokes_on"),
        @Index(columnList = "revokes_on,expires_on")
}, uniqueConstraints = {
        @UniqueConstraint(columnNames = {"written_number", "pub", "op"})
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class MINDEX implements Comparable<MINDEX> {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String op;

    private String type;

    private String pub;

    private BStamp signed;

    private BStamp written;

    private Long expires_on;

    private Long expired_on;

    private Long revokes_on;

    private BStamp revoked;

    private Boolean leaving;

    private String revocation;

    private Long chainable_on;

    private Long renewal;

    private Long expiry;


    // Transient variable local to the Block we are validating
    public transient long age;
    public transient boolean numberFollowing;
    public transient boolean distanceOK;
    public transient boolean onRevoked;
    public transient boolean joinsTwice;
    public transient boolean enoughCerts;
    public transient boolean leaverIsMember;
    public transient boolean activeIsMember;
    public transient boolean revokedIsMember;
    public transient boolean alreadyRevoked;
    public transient boolean revocationSigOK;
    public transient boolean isBeingRevoked;
    public transient long unchainables;


    public MINDEX(String op, String pub, BStamp created_on, BStamp written_on, String type, Long expires_on, Long expired_on,
                  Long revokes_on, BStamp revoked, String revocation_sig, Boolean leaving, Long chainable_on) {
        this.op = op;
        this.pub = pub;
        this.written = written_on;
        this.revoked = revoked;
        this.signed = created_on;
        this.type = type;
        this.expires_on = expires_on;
        this.expired_on = expired_on;
        this.revokes_on = revokes_on;
        this.revocation = revocation_sig;
        this.leaving = leaving;
        this.chainable_on = chainable_on;
    }

    public void setSigned(BStamp signed) {
        this.signed =  signed;
    }


    @Override
    public int compareTo(@NonNull MINDEX o) {
        return (pub + type).compareTo(o.pub + o.type);
    }


    public static final BinaryOperator<MINDEX> reducer = (m1, m2) -> {

        // var top = m1.written.compareTo(m2.written) > 0 ? m2 : m1;
        //
        MINDEX bot, top;
        if (m1.written.getNumber() > m2.written.getNumber()) {
            top = m1;
            bot = m2;
        } else {
            top = m2;
            bot = m1;
        }
        //System.out.println("Reducing" + top+ "\n  with  " + bot + "\nrevok: "  + bot.getRevoked() +" "+ top.getRevoked());

        if (top.getSigned() == null)
            top.setSigned(bot.getSigned());

        if (top.getLeaving() == null)
            top.setLeaving(bot.getLeaving());

        if (top.getRevoked() == null) {
            top.setRevoked(bot.getRevoked());
        }

        if (top.getRevokes_on() == null)
            top.setRevokes_on(bot.getRevokes_on());

        if (top.getRevocation() == null)
            top.setRevocation(bot.getRevocation());

        if (top.getExpired_on() == null)
            top.setExpired_on(bot.getExpired_on());

        if (top.getExpires_on() == null)
            top.setExpires_on(bot.getExpires_on());

        return top;

    };



}