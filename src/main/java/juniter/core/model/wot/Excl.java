package juniter.core.model.wot;

public class Excl extends Member {

    public Excl(String dup){
        super(dup);
        type = "EXCLUDED";
    }

    @Override
    public void parse(String excl) {
        pubkey = excl;
        excluded=true;
    }

    @Override
    public String toDUP() {
        return pubkey  ;
    }

}
