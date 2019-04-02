package juniter.core.model.dbo.wot;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "wot_excluded", schema = "public")
public class Excluded extends Member implements Serializable {
    private static final long serialVersionUID = -9043525414764126719L;
    public Excluded(String dup){
        parse(dup);
    }

    public Excluded(){
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
