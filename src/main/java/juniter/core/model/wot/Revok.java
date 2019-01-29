package juniter.core.model.wot;

public class Revok extends Member {

    public Revok(String dup){
        super(dup);
        type = "REVOKED";
    }

    @Override
    public void parse(String excl) {
        final var vals = excl.split(":");
        pubkey = vals[0] ;
        revocation = vals[1];
    }

    @Override
    public String toDUP() {
        return pubkey + ":" + revocation  ;
    }

}
