package juniter.core.event;

public class LogIndex extends CoreEvent<String> {

    public LogIndex(String what) {
        super(what, "index : ");
        name = getClass().getSimpleName();
    }
}
