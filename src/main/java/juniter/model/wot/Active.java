package juniter.model.wot;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import juniter.model.base.Buid;
import juniter.model.base.PubKey;


@Embeddable
public class Active implements Serializable {

	private static final long serialVersionUID = -5880074424437322665L;

	private static final Logger logger = LogManager.getLogger();

	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "active"))
	private PubKey active = new PubKey();

	private String signature;

	@AttributeOverride(name = "buid", column = @Column(name = "buid1"))
	@Valid private Buid buid1 = new Buid();
	
//	@AttributeOverride(name = "buid2", column = @Column(name = "buid2"))
	@Valid private String buid2 ;

	private String pseudo;

	public Active() { }

	public Active(String Active) {
		setActive(Active);
	}

	public String getActive() {
		return active + ":" + signature + ":" + buid1 + ":" + buid2 +":"+pseudo;
	}

	public void setActive(String leaver) {
		
		logger.info("Parsing Active... "+leaver);
		var vals = leaver.split(":");
		this.active.setPubkey(vals[0]);
		signature = vals[1];
		buid1.setBuid(vals[2]);
		buid2 = vals[3];
		pseudo = vals[4];
		//this.leaver = joiner;
	}

	public String toRaw() {
		return getActive();
	}
	
	@Override
	public String toString() {
		return getActive();
	}
}
