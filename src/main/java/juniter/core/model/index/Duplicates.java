package juniter.core.model.index;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@ToString
@Entity
@Table(name = "duplicatesBelow", schema = "public")
@NoArgsConstructor
public class Duplicates implements Serializable {
    private static final long serialVersionUID = -640724954830671000L;

    @Id
    public String pub;

    private Long count;

    public Duplicates(String pub, Long count) {
        this.pub = pub;
        this.count = count;
    }


}