package juniter.core.model.dbo.wot;

import juniter.core.model.dbo.BStamp;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Table(name = "wot_renew", schema = "public")
public class Renew extends Member {

    public Renew(String toDUP) {
        parse(toDUP);
    }

    public void parse(String joiner) {
        final var vals = joiner.split(":");
        pubkey = vals[0];
        signature = vals[1];
        signed  = new BStamp(vals[2]);
//        signedOn = Integer.parseInt(signed.split("-")[0]);
//        signedHash = signed.split("-")[1];
        i_block_uid = vals[3];
        uid = vals[4];
    }

}
