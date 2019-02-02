package juniter.core.model.index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "BINDEX", schema = "public") // , indexes = @Index(columnList = "number,hash"))
@JsonIgnoreProperties(ignoreUnknown = true)
public class BINDEX implements Serializable {

    private static final long serialVersionUID = -640721971830671L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    Integer version;
    Integer size;
    public String hash;
    String issuer;
    Long time;
    public Integer number;
    String currency;
    String previousHash ;
    String previousIssuer ;
    Integer membersCount ;
    Boolean issuerIsMember;
    Integer issuersCount ;
    Integer issuersFrame ;
    Integer issuersFrameVar ;
    Integer issuerDiff ;
    Integer avgBlockSize ;
    Long medianTime ;
    Integer dividend ;
    Long mass ;
    Long massReeval ;
    Integer unitBase ;
    Integer powMin;

    Long udTime;
    Long diffTime ;
    Long speed;

    Integer new_dividend;
    Long udReevalTime;

}