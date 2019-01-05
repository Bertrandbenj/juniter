package juniter.grammar;

import juniter.core.validation.LocalValid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
public class RevocationDoc implements WotDocument, LocalValid {
	private String version;
	private String currency;
	private String issuer;
	private String idtyUniqueID;
	private String idtyTimestamp;
	private String idtySignature;
	private String signature;

	public RevocationDoc(String version, String currency, String issuer, String idtyUniqueID, String idtyTimestamp, String idtySignature) {
		this.version = version;
		this.currency = currency;
		this.issuer = issuer;
		this.idtyUniqueID = idtyUniqueID;
		this.idtyTimestamp = idtyTimestamp;
		this.idtySignature = idtySignature;
	}

	@Override
	public boolean isValid() {
		return isV10(version) && isG1(currency) && verifySignature(unsignedDoc(), signature, issuer);
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	@Override
	public String toString() {
		return unsignedDoc() + signature + "\n";
	}

	public String unsignedDoc() {
		return "Version: " + version + "\n" +
				"Type: Revocation\n" +
				"Currency: " + currency + "\n" +
				"Issuer: " + issuer + "\n" +
				"IdtyUniqueID: " + idtyUniqueID + "\n" +
				"IdtyTimestamp: " + idtyTimestamp + "\n" +
				"IdtySignature: " + idtySignature + "\n";
	}
}