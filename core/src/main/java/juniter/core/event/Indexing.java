package juniter.core.event;

public class Indexing extends CoreEvent<Boolean> {

    public Indexing(Boolean isIndexing, String message) {
        super(isIndexing, message);
        name = getClass().getSimpleName();
    }

}
