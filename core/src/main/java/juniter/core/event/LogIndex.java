package juniter.core.event;

import juniter.core.event.CoreEvent;


public class LogIndex extends CoreEvent<String> {

    public LogIndex(String what) {
        super(what, "index : ");
        name = getClass().getSimpleName();
    }
}
