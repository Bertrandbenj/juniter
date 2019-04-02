package juniter.core.model.dbo.wot;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "wot_joiner", schema = "public")
public class Joiner extends Member implements Serializable {
    private static final long serialVersionUID = -9053525414764126719L;
    public Joiner(String toDUP) {
        parse(toDUP);
//        type = "JOIN";
    }

    public Joiner(){
//        type = "JOIN";
    }

    public void parse(String joiner) {
        final var vals = joiner.split(":");
        pubkey = vals[0];
        signature = vals[1];
        createdOn = vals[2];
        i_block_uid = vals[3];
        uid = vals[4];
    }

}
