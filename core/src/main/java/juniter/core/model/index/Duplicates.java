package juniter.core.model.index;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@Entity
@Table(name = "duplicatesBelow", schema = "public")
@NoArgsConstructor
public class Duplicates  {

    @Id
    public String pub;

    private Long count;

    public Duplicates(String pub, Long count) {
        this.pub = pub;
        this.count = count;
    }


}