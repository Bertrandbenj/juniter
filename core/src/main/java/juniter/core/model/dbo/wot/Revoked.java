package juniter.core.model.dbo.wot;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "wot_revoked", schema = "public")
public class Revoked extends Member implements Serializable {
    private static final long serialVersionUID = -9083525414764126719L;
    public Revoked(String dup) {
        parse(dup);
    }

    public Revoked() {
    }


    public void parse(String excl) {
        final var vals = excl.split(":");
        pubkey = vals[0];
        revocation = vals[1];
    }

    @Override
    public String toDUP() {
        return pubkey + ":" + revocation;
    }

}
