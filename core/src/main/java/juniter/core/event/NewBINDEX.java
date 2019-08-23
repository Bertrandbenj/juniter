package juniter.core.event;

import juniter.core.model.dbo.index.BINDEX;


public class NewBINDEX extends CoreEvent<BINDEX> {

    public NewBINDEX(BINDEX what, String message) {
        super(what, message);
        name = getClass().getSimpleName();
    }

    public NewBINDEX(BINDEX what) {
        this(what, "Validated " + what);
    }
}
