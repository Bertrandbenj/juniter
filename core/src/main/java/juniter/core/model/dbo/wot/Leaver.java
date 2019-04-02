package juniter.core.model.dbo.wot;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "wot_leaver", schema = "public")
public class Leaver extends Member implements Serializable {
    private static final long serialVersionUID = -9063525414764126719L;
    public Leaver(String toDUP) {
        parse(toDUP);
//        type = "LEAVE";
    }

    public Leaver(){
//        type = "LEAVE";
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
