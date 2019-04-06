package juniter.core.model.dbo.index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "BINDEX", schema = "public", indexes = {
        @Index(  columnList = "issuer"),
        @Index(  columnList = "time"),
        @Index(  columnList = "number")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class BINDEX  {

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
    String previousHash;
    String previousIssuer;
    Integer membersCount;
    Boolean issuerIsMember;
    Integer issuersCount;
    Integer issuersFrame;
    Integer issuersFrameVar;
    Integer issuerDiff;
    Integer avgBlockSize;
    Long medianTime;
    Integer dividend;
    Long mass;
    Long massReeval;
    Integer unitBase;
    Integer powMin;

    Long udTime;
    Long diffTime;
    Long speed;

    Integer new_dividend;
    Long udReevalTime;

}