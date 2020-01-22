package juniter.core.model.dbo.wot;

import juniter.core.model.meta.DUPExcluded;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Table(name = "wot_excluded", schema = "public")
public class Excluded extends Member implements DUPExcluded {

    public Excluded(String dup) {
        pubkey = dup;
        excluded = true;
    }

}
