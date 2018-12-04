package juniter.service.bma.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.Serializable;

/**
 * Pubkey : signature : buid : buid : pseudo;
 *
 * @author ben
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Certification implements Serializable {
	private static final Logger LOG = LogManager.getLogger();

	private static final long serialVersionUID = -9160917061296193207L;



	public Certification(String version, String currency, String issuer, String idtyIssuer, String idtyUniqueID, String idtyTimestamp, String idtySignature, String certTimestamp) {
		this.version = version;
		this.currency = currency;
		this.issuer = issuer;
		this.idtyIssuer = idtyIssuer;
		this.idtyUniqueID = idtyUniqueID;
		IdtyTimestamp = idtyTimestamp;
		IdtySignature = idtySignature;
		CertTimestamp = certTimestamp;
	}

	private String version;

	private String type = "Certification";

	private String currency;

	private String issuer ;

	private String idtyIssuer;

	private String idtyUniqueID;
	private String IdtyTimestamp;
	private String IdtySignature;
	private String CertTimestamp;

	public Certification() {

	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getIssuer() {
		return issuer;
	}

	public void setIssuer(String issuer) {
		this.issuer = issuer;
	}

	public String getIdtyIssuer() {
		return idtyIssuer;
	}

	public void setIdtyIssuer(String idtyIssuer) {
		this.idtyIssuer = idtyIssuer;
	}

	public String getIdtyUniqueID() {
		return idtyUniqueID;
	}

	public void setIdtyUniqueID(String idtyUniqueID) {
		this.idtyUniqueID = idtyUniqueID;
	}

	public String getIdtyTimestamp() {
		return IdtyTimestamp;
	}

	public void setIdtyTimestamp(String idtyTimestamp) {
		IdtyTimestamp = idtyTimestamp;
	}

	public String getIdtySignature() {
		return IdtySignature;
	}

	public void setIdtySignature(String idtySignature) {
		IdtySignature = idtySignature;
	}

	public String getCertTimestamp() {
		return CertTimestamp;
	}

	public void setCertTimestamp(String certTimestamp) {
		CertTimestamp = certTimestamp;
	}
}
