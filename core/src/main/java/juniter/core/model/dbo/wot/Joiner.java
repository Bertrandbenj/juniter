package juniter.core.model.dbo.wot;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Table(name = "wot_joiner", schema = "public")
public class Joiner extends Member implements Serializable {
    //private static final long serialVersionUID = -9053525414764126719L;
    public Joiner(String toDUP) {
        parse(toDUP);
    }


    public void parse(String joiner) {
        final var vals = joiner.split(":");
        pubkey = vals[0];
        signature = vals[1];
        var signed  = vals[2];
        signedOn = Integer.parseInt(signed.split("-")[0]);
        signedHash = signed.split("-")[1];
        i_block_uid = vals[3];
        uid = vals[4];
    }

}
