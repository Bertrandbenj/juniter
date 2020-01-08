package juniter.core.model.meta;

public interface BlockMeta extends HasHash {

    String hash = "huhu";

    default String toDUP(){
        return getHash() + " " + BlockMeta.hash;
    }
}
