package juniter.core.event;

import juniter.core.event.CoreEvent;
import juniter.core.model.dbo.DBBlock;


public class CurrentBNUM extends CoreEvent<Integer> {

    public CurrentBNUM(Integer what) {
        super(what, "Current Block : ");
        name = getClass().getSimpleName();
    }
}
