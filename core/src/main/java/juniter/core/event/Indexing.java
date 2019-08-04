package juniter.core.event;

import juniter.core.event.CoreEvent;


public class Indexing extends CoreEvent<Boolean> {

    public Indexing(Boolean isIndexing) {
        super(isIndexing, "revert");
        name = getClass().getSimpleName();
    }

}
