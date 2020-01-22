package juniter.core.model.meta;

import juniter.core.model.dbo.BStamp;

public interface DUPMember extends DUPDocument, SimpleIssuer,  HasWritten {
    String getSignature();

    BStamp getSigned();

    String getUid();

    String getI_block_uid();

    String getPubkey();

    void setPubkey(String pk) ;

    @Override
    default String issuer() {
        return getPubkey();
    }

    @Override
    default String signature() {
        return getSignature();
    }

    @Override
    default String toDUP() {
        return getPubkey() + ":" + getSignature() + ":" + getSigned() + ":" + getI_block_uid() + ":" + getUid();
    }

    @Override
    default String type(){
        return "Membership";
    }


    @Override
    default String toDUPdoc(boolean signed) {
        return "Version: " + getVersion() + "\n" +
                "Type: " + type() + "\n" +
                "Currency: " + getCurrency() + "\n" +
                "Issuer: " + getPubkey() + "\n" +
                "UniqueID: " + getUid() + "\n" +
                "Timestamp: " + getSigned() + "\n" +
                (signed?getSignature():"")
                ;
    }

    @Override
    default void setIssuer(String iss) {
        setPubkey(iss);
    }
}
