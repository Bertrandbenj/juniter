package juniter.core.event;

public class Indexing extends CoreEvent<Boolean> {

    public Indexing(Boolean isIndexing) {
        super(isIndexing, "revert");
        name = getClass().getSimpleName();
    }

}
