package juniter.core.model.meta;

import juniter.core.model.dbo.BStamp;
import juniter.core.model.dbo.net.EndPoint;

import java.util.List;
import java.util.stream.Collectors;

public interface DUPPeer extends DUPDocument, HasPubkey  {

    List<EndPoint> endpoints();
    BStamp getBlock();

    void setBlock(BStamp stamp);
    void setPubkey(String pk);


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
        return "Peer";
    }

    default String toDUP(){
        return toDUPdoc(false);
    }

    default String toDUPdoc(boolean signed) {
        return "Version: " + getVersion() +
                "\nType: Peer" +
                "\nCurrency: " + getCurrency() +
                "\nPublicKey: " + issuer() +
                "\nBlock: " + getBlock() +
                "\nEndpoints:\n"
                + endpoints().stream().map(EndPoint::getEndpoint).collect(Collectors.joining("\n"))
                + "\n" +
                (signed ? getSignature() + "\n" : "");
    }

    @Override
    default void setIssuer(String iss) {
        setPubkey(iss);
    }


}
