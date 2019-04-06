package juniter.core.model.dbo.wot;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Table(name = "wot_excluded", schema = "public")
public class Excluded extends Member  {
    public Excluded(String dup){
        parse(dup);
    }


    public void parse(String excl) {
        pubkey = excl;
        excluded=true;
    }

    @Override
    public String toDUP() {
        return pubkey  ;
    }

}
