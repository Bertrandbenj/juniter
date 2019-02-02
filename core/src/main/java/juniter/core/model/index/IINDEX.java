package juniter.core.model.index;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "IINDEX", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class IINDEX implements Serializable {

    private static final long serialVersionUID = -218627974830671L;

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
    public Boolean member;
    Boolean wasMember;
    Boolean kick;
    Integer wotbid;
    int writtenOn;


}