package juniter.core.model.dbo.index;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "accounts", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    public String conditions;

    public Long bSum ;

}