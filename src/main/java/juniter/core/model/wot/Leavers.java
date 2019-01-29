package juniter.core.model.wot;

public class Leavers extends Join {
    public Leavers(String toDUP) {
        super(toDUP);
        type = "LEAVE";
    }
}
