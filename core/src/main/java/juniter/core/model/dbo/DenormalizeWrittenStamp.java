package juniter.core.model.dbo;

public interface DenormalizeWrittenStamp {

    void setWrittenOn(Integer number);
    void setWrittenHash(String hash);
    void setWrittenTime(Long medianTime);

    default void setWritten(DBBlock block){
        setWrittenOn(block.getNumber());
        setWrittenHash(block.getHash());
        setWrittenTime(block.getMedianTime());
    }
}
