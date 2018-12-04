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
public class Identity implements Serializable {
	private static final Logger LOG = LogManager.getLogger();

	private static final long serialVersionUID = -9160916061296193207L;


	public Identity(String version, String currency, String issuer, String uniqueID) {
		this.version = version;

		this.currency = currency;
		this.issuer = issuer;
		this.uniqueID = uniqueID;
	}

	private String version;

	private String type = "Identity";

	private String currency;

	private String issuer ;

	private String uniqueID;


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

	public String getUniqueID() {
		return uniqueID;
	}

	public void setUniqueID(String uniqueID) {
		this.uniqueID = uniqueID;
	}


}
