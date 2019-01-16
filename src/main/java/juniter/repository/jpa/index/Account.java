package juniter.repository.jpa.index;

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
@Table(name = "accounts", schema = "public")
@NoArgsConstructor
public class Account implements Serializable {
    private static final long serialVersionUID = -640724971830671000L;

    @Id
    public String conditions;

    public Account(String acc, Long sum) {
        this.conditions = acc;
        this.bSum = sum;
    }

    public Long bSum ;
}