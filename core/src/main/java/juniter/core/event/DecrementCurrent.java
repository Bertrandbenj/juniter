package juniter.core.event;

public class DecrementCurrent extends CoreEvent<Boolean> {

    public DecrementCurrent() {
        super(true, "revert");
        name = getClass().getSimpleName();
    }
}
