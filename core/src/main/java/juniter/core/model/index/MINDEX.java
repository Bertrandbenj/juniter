package juniter.core.model.index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.util.function.BinaryOperator;

@Data
@NoArgsConstructor
@Entity
@ToString
@Table(name = "MINDEX", schema = "public") // , indexes = @Index(columnList = "number,hash"))
@JsonIgnoreProperties(ignoreUnknown = true)
//@IdClass(BStamp.class)
public class MINDEX implements Serializable {

    private static final long serialVersionUID = -6400219827778830671L;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    private String op;
    private String type;
    private String pub;
    private String created_on;
    private String written_on;
    private Long expires_on;
    private Long expired_on;
    private Long revokes_on;
    private String revoked_on;
    private Boolean leaving;
    private String revocation;
    private Long chainable_on;
    private Integer writtenOn;
    private Long renewal;
    private Long expiry;

    public static final BinaryOperator<MINDEX> reducer = (m1, m2) -> {

        // var top = m1.written_on.compareTo(m2.written_on) > 0 ? m2 : m1;
        //System.out.println("Reducing" + m1 + "\n" + m2);
        MINDEX bot, top;
        if (m1.writtenOn < m2.writtenOn) {
            top = m1;
            bot = m2;
        } else {
            top = m2;
            bot = m1;
        }

        if (top.getCreated_on() == null)
            top.setCreated_on(bot.getCreated_on().toString());


        if (top.getLeaving() == null)
            top.setLeaving(bot.getLeaving());


        if (top.getCreated_on() == null)
            top.setCreated_on(bot.getCreated_on().toString());

        if (top.getRevoked_on() == null)
            top.setRevoked_on(bot.getRevoked_on());
        if (top.getRevokes_on() == null)
            top.setRevokes_on(bot.getRevokes_on());
        if (top.getRevocation() == null)
            top.setRevocation(bot.getRevocation());

        if (top.getExpired_on() == null)
            top.setExpired_on(bot.getExpired_on());
        if (top.getExpires_on() == null)
            top.setExpires_on(bot.getExpires_on());


        return top;


        //return reduceM(pub).sorted((m1, m2) -> m1.written_on.compareTo(m2.written_on)).findFirst().get();
    };


}