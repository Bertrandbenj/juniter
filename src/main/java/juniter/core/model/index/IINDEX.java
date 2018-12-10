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
@Table(name = "IINDEX", schema = "public") // , indexes = @Index(columnList = "number,hash"))
@JsonIgnoreProperties(ignoreUnknown = true)
//@IdClass(BStamp.class)

public class IINDEX implements Serializable {

    private static final long serialVersionUID = -6400219627974830671L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;


    private String op;
    private String uid;
    private String pub;


    private String hash;
    private String sig;

    private String created_on;
    private String written_on;
    private boolean member;
    private boolean wasMember;
    private boolean kick;
    private Integer wotbid;
    private Integer writtenOn;


}