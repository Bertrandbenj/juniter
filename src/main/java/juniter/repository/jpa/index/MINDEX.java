package juniter.repository.jpa.index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
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

    String op;
    String type;
    String pub;
    String created_on;
    String written_on;
    Long expires_on;
    Long expired_on;
    Long revokes_on;
    Long revoked_on;
    Integer leaving;
    String revocation;
    Long chainable_on;
    Integer writtenOn;


}