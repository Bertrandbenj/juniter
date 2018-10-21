package juniter.core.model.wot;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import juniter.core.model.BStamp;
import juniter.core.model.Pubkey;

@Embeddable
public class Joiner implements Serializable, Comparable<Joiner> {

	private static final Logger logger = LogManager.getLogger();

	private static final long serialVersionUID = 4413010134970991059L;

//	@Column(name = "joiner", nullable = true, length = 350)
//	private String joiner;

	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "joinerKey"))
	private Pubkey joinerKey = new Pubkey();

	private String signature;

	@Valid
//	@Embedded
//	@AttributeOverride(name = "stamp", column = @Column(name = "hash"))
//	    @AttributeOverrides( {
//	        @AttributeOverride(name="buid1.number", column = @Column(name="") ),
//	        @AttributeOverride(name="foundedFromOwnResources.amount", column = @Column(name="previousReport_foundedFromOwnResources")),
//	        @AttributeOverride(name="personalContribution.amount", column = @Column(name="previousReport_personalContribution"))
//	    } )
	private String buid1;// = new BStamp();

//	@Valid
//	@AttributeOverride(name = "buid2", column = @Column(name = "buid2"))
	private String buid2;

	private String pseudo;

	public Joiner() {

	}

	public Joiner(String joiner) {
		setJoiner(joiner);
	}

	public String bstamp() {
		return buid1;
	}

	@Override
	public int compareTo(Joiner o) {
		return joinerKey.getPubkey().compareTo(o.joinerKey.getPubkey());
	}

	public BStamp createdOn() {
		return new BStamp(buid1);
	}

	public String getJoiner() {
		return joinerKey + ":" + signature + ":" + buid1 + ":" + buid2 + ":" + pseudo;
	}

	public String pseudo() {
		return pseudo;
	}

	public String pub() {
		return joinerKey.getPubkey();
	}

	public void setJoiner(String joiner) {
		logger.debug("Parsing Joiner... " + joiner);
		final var vals = joiner.split(":");
		joinerKey.setPubkey(vals[0]);
		signature = vals[1];
		buid1 = vals[2];
		buid2 = vals[3];
		pseudo = vals[4];
	}

	public String toDUP() {
		return getJoiner();
	}

	@Override
	public String toString() {
		return getJoiner();
	}

	public BStamp writtenOn() {
		return new BStamp(buid2);
	}
}
