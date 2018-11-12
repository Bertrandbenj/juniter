package juniter.grammar;

import juniter.core.model.BStamp;
import juniter.core.validation.LocalValid;

public class IdentityDocument implements Document, LocalValid {
	String version;
	String currency;
	String issuer;
	String uniqueID;
	BStamp timestamp;
	String signature;

	public IdentityDocument() {

	}

	public String getCurrency() {
		return currency;
	}

	public String getIssuer() {
		return issuer;
	}

	public String getSignature() {
		return signature;
	}

	public BStamp getTimestamp() {
		return timestamp;
	}

	public String getUniqueID() {
		return uniqueID;
	}

	public String getVersion() {
		return version;
	}

	@Override
	public boolean isValid() {
		return isV10(version) && isG1(currency) && verifySignature(unsignedDoc(), signature, issuer);
	}

	public void setBlock(BStamp block) {
		timestamp = block;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public void setTimestamp(BStamp timestamp) {
		this.timestamp = timestamp;
	}

	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}

	public void setVersion(String version) {
		this.version = version;
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
				"UniqueID: " + issuer + "\n" + //
				"Timestamp: " + timestamp + "\n";
	}

}
