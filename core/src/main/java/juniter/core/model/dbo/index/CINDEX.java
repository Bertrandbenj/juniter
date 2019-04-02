package juniter.core.model.dbo.index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "CINDEX", schema = "public", indexes = {
        @Index(name="ind_op", columnList = "op"),
        @Index(name="ind_issuer", columnList = "issuer"),
        @Index(name="ind_receiver", columnList = "receiver"),
        @Index(name="ind_createdOn", columnList = "createdOn"),
        @Index(name="ind_createdOn", columnList = "written_on")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class CINDEX  {


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