package juniter.core.event;

import juniter.core.model.dbo.DBBlock;


public class NewBlock extends CoreEvent<DBBlock> {

    public NewBlock(DBBlock what, String message) {
        super(what, message);
        name = getClass().getSimpleName();
    }


    public NewBlock(DBBlock what) {
        this(what, "new Block");
    }
}
