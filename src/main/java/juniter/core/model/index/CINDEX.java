package juniter.core.model.index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "CINDEX", schema = "public") // , indexes = @Index(columnList = "number,hash"))
@JsonIgnoreProperties(ignoreUnknown = true)
//@IdClass(BStamp.class)
public class CINDEX implements Serializable {

    private static final long serialVersionUID = -640021978930671L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    private String op;
    private String issuer;
    private String receiver;
    private Integer createdOn;
    private String written_on;
    private String sig;
    private Long expires_on;
    private Long expired_on;
    private Long chainable_on;
    private String from_wid;
    private String to_wid;
    private Integer writtenOn;


}