package juniter.core.model.dbo.index;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Data
//@Entity
//@Table(name = "account", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    public String conditions;

    public Long bSum;

}