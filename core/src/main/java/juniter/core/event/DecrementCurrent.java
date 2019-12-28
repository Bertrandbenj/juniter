package juniter.core.event;

public class DecrementCurrent extends CoreEvent<Boolean> {

    public DecrementCurrent() {
        super(true, "");
        name = getClass().getSimpleName();
    }
}
