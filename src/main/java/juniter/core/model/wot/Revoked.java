package juniter.core.model.wot;

import juniter.core.model.BStamp;
import juniter.core.model.Pubkey;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.Valid;
import java.io.Serializable;

@Embeddable
public class Revoked implements Serializable, Comparable<Revoked> {

	private static final long serialVersionUID = 2875594811917743111L;

	private static final Logger LOG = LogManager.getLogger();

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

		LOG.debug("Parsing Revoked... " + rev);
		final var vals = rev.split(":");
		revoked.setPubkey(vals[0]);
		signature = vals[1];
	}

	public String signature(){
		return signature;
	}

	public String toDUP() {
		return getRevoked();
	}

	@Override
	public String toString() {
		return getRevoked();
	}

	public BStamp createdOn() {
		return new BStamp("1234-REVOKED");
	}
}
