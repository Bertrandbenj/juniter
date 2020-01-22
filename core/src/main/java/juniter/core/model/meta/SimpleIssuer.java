package juniter.core.model.meta;

public interface SimpleIssuer {

    String issuer() ;
    String signature();


    String getSignature();
    void setSignature(String signature);

    default void setIssuer(String iss){ }

}
