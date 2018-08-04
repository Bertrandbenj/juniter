package juniter.model.persistence.wot;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import juniter.model.persistence.Pubkey;

@Embeddable
public class Revoked implements Serializable {

	private static final long serialVersionUID = 2875594811917743111L;

	private static final Logger logger = LogManager.getLogger();

	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "revoked"))
	private Pubkey revoked = new Pubkey();

	private String signature;

	
	public Revoked() {}

	public Revoked(String joiner) {
		setRevoked(joiner);
	}

	public String getRevoked() {
		return revoked + ":" + signature ;
	}

	public void setRevoked(String rev) {
		
		logger.info("Parsing Revoked... "+rev);
		var vals = rev.split(":");
		revoked.setPubkey(vals[0]);
		signature = vals[1];
	}

	public String toRaw() {
		return getRevoked();
	}
	
	@Override
	public String toString() {
		return getRevoked();
	}
}
