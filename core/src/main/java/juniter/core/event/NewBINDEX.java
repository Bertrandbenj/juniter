package juniter.core.event;

import juniter.core.event.CoreEvent;
import juniter.core.model.dbo.index.BINDEX;


public class NewBINDEX extends CoreEvent<BINDEX> {

    public NewBINDEX(BINDEX what, String message) {
        super(what, message);
        name = "BINDEX";
    }
}
