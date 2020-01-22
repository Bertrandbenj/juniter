package juniter.core.model.meta;

public interface DUPCertification extends DUPDocument, SimpleIssuer, HasWritten  {

    String getCertifier();
    String getCertified();
    Integer getSignedOn();

    @Override
    default String issuer() {
        return getCertifier();
    }

    @Override
    default String signature(){
        return getSignature();
    }

    @Override
    default String type(){
        return "Certification";
    }

    default String toDUP() {
        return getCertifier() + ":" + getCertified() + ":" + getSignedOn() + ":" + getSignature();
    }

    @Override
    default String toDUPdoc(boolean signed) {
        return "Version: "+ getVersion()
                +"\nType: "+type()
                +"\n";
    }


}
