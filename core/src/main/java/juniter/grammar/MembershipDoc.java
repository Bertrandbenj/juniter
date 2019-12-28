package juniter.grammar;

import juniter.core.validation.LocalValid;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class MembershipDoc implements WotDocument, LocalValid {


	private String version;
	private String currency;
	private String issuer;
	private String block;
	private String membership;
	private String userID;
	private String certsTS;
	private String signature;

	public MembershipDoc(String version, String currency, String issuer, String block, String membership, String userID, String certsTS) {
		this.version = version;
		this.currency = currency;
		this.issuer = issuer;
		this.block = block;
		this.membership = membership;
		this.userID = userID;
		this.certsTS = certsTS;
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

	@Override
	public String unsignedDoc() {
		return "Version: " + version + "\n" +
				"Type: Membership\n" +
				"Currency: " + currency + "\n" +
				"Issuer: " + issuer + "\n" +
				"Block: " + block + "\n" +
				"Membership: " + membership + "\n" +
				"UserID: " + userID + "\n" +
				"CertTS: " + certsTS + "\n";
	}

}
