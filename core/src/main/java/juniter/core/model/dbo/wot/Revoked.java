package juniter.core.model.dbo.wot;

import juniter.core.model.meta.DUPRevoked;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@Table(name = "wot_revoked", schema = "public")
public class Revoked extends Member implements DUPRevoked   {

    public Revoked(String dup) {
        parse(dup);
    }


    public void parse(String excl) {
        final var vals = excl.split(":");
        pubkey = vals[0];
        revocation = vals[1];
    }




}
