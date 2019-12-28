package juniter.core.event;

public class CurrentBNUM extends CoreEvent<Integer> {

    public CurrentBNUM(Integer what) {
        super(what,"");
        name = getClass().getSimpleName();
    }
}
