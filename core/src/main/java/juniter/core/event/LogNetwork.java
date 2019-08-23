package juniter.core.event;

public class LogNetwork extends CoreEvent<String> {

    public LogNetwork(String what) {
        super(what, "net : ");
        name = getClass().getSimpleName();
    }
}
