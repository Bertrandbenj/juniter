package juniter.core.event;


public class PossibleFork extends CoreEvent<Boolean> {

    public PossibleFork() {
        this(true);
    }

    public PossibleFork(Boolean fork) {
        super(fork, "revert ");
        name = getClass().getSimpleName();
    }
}
