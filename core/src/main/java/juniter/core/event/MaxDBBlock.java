package juniter.core.event;

public class MaxDBBlock extends CoreEvent<Integer> {

    public MaxDBBlock(Integer what, String message) {
        super(what, message);
        name = getClass().getSimpleName();
    }

    public MaxDBBlock(Integer what) {
        this(what, "");
    }
}
