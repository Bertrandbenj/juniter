package juniter.core.model.wot;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import juniter.core.model.Pubkey;

@Embeddable
public class Revoked implements Serializable, Comparable<Revoked> {

	private static final long serialVersionUID = 2875594811917743111L;

	private static final Logger logger = LogManager.getLogger();

	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "revoked"))
	private Pubkey revoked = new Pubkey();

	private String signature;

	public Revoked() {
	}

	public Revoked(String joiner) {
		setRevoked(joiner);
	}

	@Override
	public int compareTo(Revoked o) {
		// TODO Auto-generated method stub
		return getRevoked().compareTo(o.getRevoked());
	}

	public String getRevoked() {
		return revoked + ":" + signature;
	}

	public String revoked() {
		return revoked.getPubkey();
	}

	public void setRevoked(String rev) {

		logger.info("Parsing Revoked... " + rev);
		final var vals = rev.split(":");
		revoked.setPubkey(vals[0]);
		signature = vals[1];
	}

	public String toDUP() {
		return getRevoked();
	}

	@Override
	public String toString() {
		return getRevoked();
	}
}
