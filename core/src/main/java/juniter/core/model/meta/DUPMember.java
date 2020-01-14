package juniter.core.model.meta;

import juniter.core.model.dbo.BStamp;

public interface DUPMember extends DUPDocument, SimpleIssuer,  HasWritten {
    String getSignature();

    BStamp getSigned();


    String getUid();

    String getI_block_uid();

    String getPubkey();

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
        return getPubkey() + ":" + getSignature() + ":" + getSigned().getNumber() + "-" + getSigned().getHash() + ":" + getI_block_uid() + ":" + getUid();
    }

    @Override
    default String getType(){
        return "Membership";
    }

    @Override
    default String toDUPdoc(boolean signed) {
        return "Version: " + getVersion() + "\n" +
                "Type: " + getType() + "\n" +
                "Currency: " + getCurrency() + "\n" +
                "Issuer: " + getPubkey() + "\n" +
                "UniqueID: " + getUid() + "\n" +
                "Timestamp: " + getSigned() + "\n" +
                (signed?getSignature():"")
                ;
    }

}
