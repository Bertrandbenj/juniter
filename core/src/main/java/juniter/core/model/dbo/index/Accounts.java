package juniter.core.model.dbo.index;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Data
@Entity
@Immutable
@Table(name = "huhu", schema = "public", indexes = {
        @Index(columnList = "conditions")
})
@Subselect(value = "SELECT conditions, sum(case WHEN consumed THEN 0-amount ELSE amount end) bSum " +
        "FROM index_s " +
        "GROUP BY conditions " +
        "ORDER by conditions")
@AllArgsConstructor
@NoArgsConstructor
public class Accounts {

    @Id
    public String conditions;

    public Long bSum;

}