package juniter.core.model.dbo.wot;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "wot_renew", schema = "public")
public class Renew extends Member implements Serializable {
    private static final long serialVersionUID = -9073525414764126719L;

    public Renew(String toDUP) {
        parse(toDUP);
    }

    public Renew() {

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
