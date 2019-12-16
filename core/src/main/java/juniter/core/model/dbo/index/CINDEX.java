package juniter.core.model.dbo.index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.dbo.BStamp;
import juniter.core.model.dbo.DBBlock;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.util.function.BinaryOperator;

/**
 * <pre>
 * <h1>Certifications</h1>
 *
 * The local CINDEX has a unicity constraint on PUBKEY_FROM, PUBKEY_TO
 *
 * The local CINDEX has a unicity constraint on PUBKEY_FROM, except for block#0
 *
 * The local CINDEX must not match a MINDEX operation on
 *     PUBLIC_KEY = PUBKEY_FROM,
 *     member = false or PUBLIC_KEY = PUBKEY_FROM,
 *     leaving = true
 *
 *
 * <h1>Functionally</h1>
 *
 * a block cannot have 2 identical certifications (A -> B)
 * a block cannot have 2 certifications from a same public key, except in block#0
 * a block cannot have a cert to a leaver or an excluded
 * </pre>
 */
@Data
@NoArgsConstructor
@Entity
@Table(name = "index_c", schema = "public", indexes = {
        @Index(columnList = "op"),
        @Index(columnList = "issuer"),
        @Index(columnList = "receiver"),
        @Index(columnList = "createdOn"),
        @Index(columnList = "written_number")
}, uniqueConstraints = {
       // @UniqueConstraint(columnNames = {"receiver", "issuer", "written_number"})
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CINDEX implements Comparable<CINDEX> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    private String op;

    private String issuer;

    private String receiver;

    private Integer createdOn;

    private BStamp written;

    private String sig;

    private Long expires_on;

    private Long expired_on;

    private Long chainable_on;


    //  Local to the Block we are validating
    private transient long unchainables;
    private transient long age;
    private transient long stock;
    private transient boolean toMember;
    private transient boolean toNewcomer;
    private transient boolean toLeaver;
    private transient boolean isReplay;
    private transient boolean sigOK;
    private transient boolean fromMember;
    private transient DBBlock created_on;
    /**
     *  introduced as of version 11
     *  allow replaying cert prior to its expiry
     */
    private transient boolean isReplayable;
    private Long replayable_on;


    public CINDEX(String op, String issuer, String receiver, Integer createdOn, BStamp written_on, String sig,
                  long expires_on, long chainable_on, Long expired_on, Long replayable_on) {

        this.op = op;
        this.issuer = issuer;
        this.receiver = receiver;
        this.createdOn = createdOn;
        this.written = written_on;
        this.sig = sig;
        this.expires_on = expires_on;
        this.chainable_on = chainable_on;
        this.expired_on = expired_on;
        this.replayable_on = replayable_on;
    }

    public CINDEX putCreatedOn(DBBlock b) {
        created_on = b;
        return this;
    }

    public CINDEX(String op, String issuer, String receiver, Integer created_on, Long expired_on, BStamp written) {

        this.op = op;
        this.issuer = issuer;
        this.receiver = receiver;
        this.createdOn = created_on;
        this.expired_on = expired_on;
        this.written = written;
    }

    @Override
    public int compareTo(@NonNull CINDEX o) {
        return (receiver + "-" + issuer).compareTo(o.receiver + "-" + o.issuer);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CINDEX))
            return false;
        final var c = (CINDEX) obj;
        return c.issuer.equals(issuer) && c.receiver.equals(receiver) && c.op.equals(op);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public static final BinaryOperator<CINDEX> reducer = (c1, c2) -> {

        // var top = m1.written.compareTo(m2.written) > 0 ? m2 : m1;
        //System.out.println("Reducing" + m1 + "\n" + m2);
        CINDEX bot, top;
        if (c1.written.getNumber() > c2.written.getNumber()) {
            top = c1;
            bot = c2;
        } else {
            top = c2;
            bot = c1;
        }

        //System.out.println(c1.written.getNumber() +" > "+ c2.written.getNumber() + "\ntop "+ top+"\nbot "+bot+"\n");

        if (top.getCreated_on() == null)
            top.setCreated_on(bot.getCreated_on());

        if (top.getExpired_on() == null)
            top.setExpired_on(bot.getExpired_on());

        if (top.getExpires_on() == null)
            top.setExpires_on(bot.getExpires_on());

        if (top.getReceiver() == null)
            top.setReceiver(bot.getReceiver());

        if (top.getIssuer() == null)
            top.setIssuer(bot.getIssuer());

        if (top.getChainable_on() == null)
            top.setChainable_on(bot.getChainable_on());

        if (top.getSig() == null)
            top.setSig(bot.getSig());

        return top;

    };


}