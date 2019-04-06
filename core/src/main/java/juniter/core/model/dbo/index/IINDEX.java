package juniter.core.model.dbo.index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.function.BinaryOperator;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "IINDEX", schema = "public", indexes = {
        @Index ( columnList = "uid" ),
        @Index(  columnList = "op" ),
        @Index(  columnList = "pub" ),
        @Index(  columnList = "written_on" ),
        @Index(  columnList = "writtenOn" ),
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class IINDEX   {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String op;
    private String uid;
    private String pub;
    private String hash;
    private String sig;
    private String created_on;
    private String written_on;
    private Boolean member;
    private Boolean wasMember;
    private Boolean kick;
    private Integer wotbid;
    private int writtenOn;


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




}