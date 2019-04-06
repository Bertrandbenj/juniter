package juniter.core.model.dbo;

public interface DenormalizeSignatureStamp {

    void setSignedOn(Integer number);
    void setSignedHash(String hash);
    void setSignedTime(Long medianTime);

    default void setSigned(DBBlock block){
        setSignedOn(block.getNumber());
        setSignedHash(block.getHash());
        setSignedTime(block.getMedianTime());
    }

    default void setSigned(BStamp block){
        setSignedOn(block.getNumber());
        setSignedHash(block.getHash());
    }
}
