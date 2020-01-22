package juniter.core.model.meta;

import juniter.core.model.dbo.BStamp;


public interface DUPIdentity extends DUPDocument, HasPubkey {

    String getUid();

    BStamp getSigned();

    @Override
    default String issuer() {
        return getPubkey();
    }


    @Override
    default String signature() {
        return getSignature();
    }

    @Override
    default String type() {
        return "Identity";
    }

    @Override
    default String toDUPdoc(boolean signed) {
        return "Version: " + getVersion() + "\n" +
                "Type: " + type() + "\n" +
                "Currency: " + getCurrency() + "\n" +
                "Issuer: " + getPubkey() + "\n" +
                "UniqueID: " + getUid() + "\n" +
                "Timestamp: " + getSigned() + "\n" +
                (signed ? getSignature() : "")
                ;
    }
}
