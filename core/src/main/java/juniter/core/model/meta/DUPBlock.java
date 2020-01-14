package juniter.core.model.meta;

import java.util.List;

public interface DUPBlock extends DUPDocument, HasIssuer,HasTime, HasMedianTime, HasSignedPart, HasPrevious {
    @Override
    default String issuer() {
        return getIssuer();
    }

    default String signature() {
        return getSignature();
    }

    List<? extends DUPIdentity> getIdentities();

    Integer getNumber();

    Integer getDividend();

    @Override
    default String getType(){
        return "Block";
    }
}
