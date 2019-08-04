package juniter.core.event;

import juniter.core.event.CoreEvent;
import juniter.core.model.dbo.DBBlock;


public class DecrementCurrent extends CoreEvent<Boolean> {

    public DecrementCurrent() {
        super(true, "revert");
        name = getClass().getSimpleName();
    }
}
