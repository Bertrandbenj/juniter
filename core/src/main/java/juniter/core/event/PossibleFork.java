package juniter.core.event;


public class PossibleFork extends CoreEvent<Boolean> {

    public PossibleFork() {
        this(true);
    }

    public PossibleFork(Boolean fork) {
        super(fork, "");
        name = getClass().getSimpleName();
    }
}
