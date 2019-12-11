package juniter.core.model.dbo.index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.dbo.BStamp;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "BINDEX", schema = "public", indexes = {
        @Index(columnList = "issuer"),
        @Index(columnList = "time"),
        @Index(columnList = "number")
}, uniqueConstraints = @UniqueConstraint(columnNames = {"number"}))
@JsonIgnoreProperties(ignoreUnknown = true)
public class BINDEX implements Comparable<BINDEX> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer version;
    private Integer size;
    private String hash;
    private String issuer;
    private Long time;
    private Integer number;
        private String currency;
    private String previousHash;
    private String previousIssuer;
    private Integer membersCount;
    private Boolean issuerIsMember;
    private Integer issuersCount;
    private Integer issuersFrame;
    private Integer issuersFrameVar;
    private Integer issuerDiff;
    private Integer avgBlockSize;
    private Long medianTime;
    private Integer dividend;
    private Long mass;
    private Long massReeval;
    private Integer unitBase;
    private Integer powMin;

    private Long udTime;
    private Long diffTime;
    private Long speed;

    public Integer new_dividend;
    public Long udReevalTime;

    public transient long diffNumber;
    public transient int powRemainder;
    public transient int powZeros;

    @Override
    public int compareTo(@NonNull BINDEX o) {
        return Integer.compare(number, o.number);
    }


    public BStamp bstamp() {
        return new BStamp(number, hash, medianTime);
    }

}