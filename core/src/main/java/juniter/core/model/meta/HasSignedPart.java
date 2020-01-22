package juniter.core.model.meta;

public interface HasSignedPart extends SimpleIssuer{

    String getInner_hash();
    Long getNonce();

    default String signedPart() {
        return "InnerHash: " + getInner_hash() + "\n" +
                "Nonce: " + getNonce() + "\n";
    }

    default String signedPartSigned() {

        return signedPart() + getSignature() + "\n";
    }

}
