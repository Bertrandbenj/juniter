package juniter.core.model.meta;

public interface DUPRevoked extends DUPMember {

    @Override
    default String type() {
        return "Revocation";
    }

    String getRevocation();

    @Override
    default String toDUP() {
        return getPubkey() + ":" + getRevocation();
    }

    @Override
    default String toDUPdoc(boolean signed) {
        return "Version: " + getVersion() +
                "\nType: " + type() +
                "\nCurrency: g1" +
                "\nIssuer: " + getPubkey() +
                "\nIdtyUniqueID: " + getUid() +
                "\nIdtyTimestamp: " + getI_block_uid() +
                "\nIdtySignature: " + getSignature() +
                "\n" + (signed ? getRevocation() + "\n" : "")
                ;
    }
}
