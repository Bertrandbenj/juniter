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
public class Active implements Serializable, Comparable<Active> {

	private static final long serialVersionUID = -5880074424437322665L;

	private static final Logger LOG = LogManager.getLogger();

	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "active"))
	private Pubkey active = new Pubkey();

	private String signature;

	@AttributeOverride(name = "buid", column = @Column(name = "buid1"))
	@Valid
	private String buid1;// = new BStamp();

	//	@AttributeOverride(name = "buid2", column = @Column(name = "buid2"))
	@Valid
	private String buid2;

	private String pseudo;

	public Active() {
	}

	public Active(String Active) {
		setActive(Active);
	}

	public String activepk() {
		return active.getPubkey();
	}

	@Override
	public int compareTo(Active o) {
		return active.getPubkey().compareTo(o.active.getPubkey());
	}

	public String getActive() {
		return active + ":" + signature + ":" + buid1 + ":" + buid2 + ":" + pseudo;
	}

	public void setActive(String leaver) {

		LOG.debug("Parsing Active... " + leaver);
		final var vals = leaver.split(":");
		active.setPubkey(vals[0]);
		signature = vals[1];
		buid1 = vals[2];
		buid2 = vals[3];
		pseudo = vals[4];
		// this.leaver = joiner;
	}

	public String toDUP() {
		return getActive();
	}

	@Override
	public String toString() {
		return getActive();
	}
}
