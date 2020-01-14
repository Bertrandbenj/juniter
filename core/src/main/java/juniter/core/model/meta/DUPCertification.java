package juniter.core.model.meta;

public interface DUPCertification extends DUPDocument, SimpleIssuer, HasSignature, HasWritten  {

    String getCertifier();

    @Override
    default String issuer() {
        return getCertifier();
    }

    @Override
    default String signature(){
        return getSignature();
    }

    @Override
    default String getType(){
        return "Certification";
    }

}
