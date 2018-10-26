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
public class Leaver implements Serializable, Comparable<Leaver> {

	private static final long serialVersionUID = -4288798570176707871L;

	private static final Logger LOG = LogManager.getLogger();

	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "leaver"))
	private Pubkey leaver = new Pubkey();

	private String signature;

	@AttributeOverride(name = "buid", column = @Column(name = "buid1"))
	@Valid
	private String buid1; // = new BStamp();

//	@AttributeOverride(name = "buid2", column = @Column(name = "buid2"))
	@Valid
	private String buid2;

	private String pseudo;

	public Leaver() {
	}

	public Leaver(String Leaver) {
		setLeaver(Leaver);
	}

	@Override
	public int compareTo(Leaver o) {
		return getLeaver().compareTo(o.getLeaver());
	}

	public String getLeaver() {
		return leaver + ":" + signature + ":" + buid1 + ":" + buid2 + ":" + pseudo;
	}

	public String leaver() {
		return leaver.getPubkey();
	}

	public void setLeaver(String leaver) {

		LOG.info("Parsing Leaver... " + leaver);
		final var vals = leaver.split(":");
		this.leaver.setPubkey(vals[0]);
		signature = vals[1];
		buid1 = vals[2];
		buid2 = vals[3];
		pseudo = vals[4];
		// this.leaver = joiner;
	}

	public String toDUP() {
		return getLeaver();
	}

	@Override
	public String toString() {
		return getLeaver();
	}
}
