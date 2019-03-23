package juniter.core.model.index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.function.BinaryOperator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "IINDEX", schema = "public", indexes = {
        @Index(name = "ind_iuid", columnList = "uid" ),
        @Index(name = "ind_iop", columnList = "op" ),
        @Index(name = "ind_ipub", columnList = "pub" ),
        @Index(name = "ind_iwritten_on", columnList = "written_on" ),
        @Index(name = "ind_iwrittenOn", columnList = "writtenOn" ),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class IINDEX   {

    public static final BinaryOperator<IINDEX> reducer =(i1, i2) -> {

        IINDEX bot, top;
        if (i1.writtenOn < i2.writtenOn) {
            top = i1;
            bot = i2;
        } else {
            top = i2;
            bot = i1;
        }

        if (top.getCreated_on() == null)
            top.setCreated_on(bot.getCreated_on());
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



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;


    String op;
    String uid;
    String pub;
    String hash;
    String sig;
    String created_on;
    String written_on;
    Boolean member;
    Boolean wasMember;
    Boolean kick;
    Integer wotbid;
    int writtenOn;




}