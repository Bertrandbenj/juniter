package juniter.grammar;

import juniter.core.validation.LocalValid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class IdentityDoc implements WotDocument, LocalValid {
    private String version;
    private String currency;
    private String issuer;
    private String uniqueID;
    private String timestamp;
    private String signature;


    public IdentityDoc(String version, String currency, String issuer, String uniqueID, String timestamp) {
        this.version = version;
        this.currency = currency;
        this.issuer = issuer;
        this.uniqueID = uniqueID;
        this.timestamp = timestamp;

    }

    @Override
    public boolean isValid() {
        return isV10(version) && isG1(currency) && verifySignature(unsignedDoc(), signature, issuer);
    }

    @Override
    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return unsignedDoc() + signature + "\n";
    }

    public String unsignedDoc() {
        return "Version: " + version + "\n" + //
                "Type: Identity\n" + //
                "Currency: " + currency + "\n" + //
                "Issuer: " + issuer + "\n" + //
                "UniqueID: " + uniqueID + "\n" + //
                "Timestamp: " + timestamp + "\n";
    }

}
