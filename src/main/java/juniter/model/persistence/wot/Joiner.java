package juniter.model.persistence.wot;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import juniter.model.persistence.Buid;
import juniter.model.persistence.PubKey;

@Embeddable
public class Joiner implements Serializable {

	private static final Logger logger = LogManager.getLogger();

	private static final long serialVersionUID = 4413010134970991059L;

//	@Column(name = "joiner", nullable = true, length = 350)
//	private String joiner;

	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "joinerKey"))
	private PubKey joinerKey = new PubKey();

	private String signature;

	@Valid 
	@AttributeOverride(name = "buid", column = @Column(name = "buid1"))
	private Buid buid1 = new Buid();
	
//	@Valid
//	@AttributeOverride(name = "buid2", column = @Column(name = "buid2"))
	private String buid2;

	private String pseudo;

	public Joiner() {

	}

	public Joiner(String joiner) {
		setJoiner(joiner);
	}

	public String getJoiner() {
		return joinerKey + ":" + signature + ":" + buid1+ ":" + buid2 +":"+pseudo;
	}

	public void setJoiner(String joiner) {
		
		logger.debug("Parsing Joiner... "+joiner);
		var vals = joiner.split(":");
		joinerKey.setPubkey(vals[0]);
		signature = vals[1];
		buid1.setBuid(vals[2]);
		buid2 = vals[3];
		pseudo = vals[4];
	}

	public String toRaw() {
		return getJoiner();
	}
	
	@Override
	public String toString() {
		return  getJoiner();
	}
}
