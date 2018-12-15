package juniter.repository.jpa.index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
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
    String hash;
    String issuer;
    Long time;
    Integer number;
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

}