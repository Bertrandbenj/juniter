package juniter.core.event;

public class MaxPeerBlock extends CoreEvent<Integer> {

    private MaxPeerBlock(Integer what, String message) {
        super(what, message);
        name = getClass().getSimpleName();
    }

    public MaxPeerBlock(Integer what) {
        this(what, "maxPeer");
    }
}
