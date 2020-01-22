package juniter.core.model.dbo.wot;

import juniter.core.model.dbo.BStamp;
import juniter.core.model.meta.DUPJoiner;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Table(name = "wot_joiner", schema = "public")
public class Joiner extends Member implements DUPJoiner {

    public Joiner(String toDUP) {
        parse(toDUP);
    }


    public void parse(String joiner) {
        final var vals = joiner.split(":");
        pubkey = vals[0];
        signature = vals[1];
        signed = new BStamp(vals[2]);
        i_block_uid = vals[3];
        uid = vals[4];
    }

}
