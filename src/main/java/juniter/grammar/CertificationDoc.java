package juniter.grammar;

import juniter.core.validation.LocalValid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CertificationDoc implements WotDocument, LocalValid {

	private String version;
	private String currency;
	private String issuer;
	private String idtyIssuer;
	private String idtyUniqueID;
	private String idtyTimestamp;
	private String idtySignature;
	private String certSignature;
	private String signature;

	public CertificationDoc(String version, String currency, String issuer, String idtyIssuer, String idtyUniqueID, String idtyTimestamp, String idtySignature, String certSignature) {
		this.version = version;
		this.currency = currency;
		this.issuer = issuer;
		this.idtyIssuer = idtyIssuer;
		this.idtyUniqueID = idtyUniqueID;
		this.idtyTimestamp = idtyTimestamp;
		this.idtySignature = idtySignature;
		this.certSignature = certSignature;
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
				"Type: Certification\n" + //
				"Currency: " + currency + "\n" + //
				"Issuer: " + issuer + "\n" + //
				"IdtyIssuer: " + idtyIssuer + "\n" + //
				"IdtyUniqueID: " + idtyUniqueID + "\n" + //
				"IdtyTimestamp: " + idtyTimestamp + "\n" + //
				"IdtySignature: " + idtySignature + "\n" + //
				"CertTimestamp: " + certSignature + "\n";
	}

}
