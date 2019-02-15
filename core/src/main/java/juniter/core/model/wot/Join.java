package juniter.core.model.wot;

public class Join extends Member {

    public Join(String toDUP) {
        super(toDUP);
        type = "JOIN";
    }

    @Override
    public void parse(String joiner) {
        final var vals = joiner.split(":");
        pubkey = vals[0];
        signature = vals[1];
        createdOn = vals[2];
        i_block_uid = vals[3];
        uid = vals[4];
    }

}
