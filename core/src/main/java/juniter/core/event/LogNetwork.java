package juniter.core.event;

import juniter.core.event.CoreEvent;


public class LogNetwork extends CoreEvent<String> {

    public LogNetwork(String what) {
        super(what, "net : ");
        name = getClass().getSimpleName();
    }
}
