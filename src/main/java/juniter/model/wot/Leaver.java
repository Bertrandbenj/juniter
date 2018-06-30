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
public class Leaver implements Serializable {

	private static final long serialVersionUID = -4288798570176707871L;


	private static final Logger logger = LogManager.getLogger();


	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "leaver"))
	private PubKey leaver = new PubKey();

	private String signature;

	@AttributeOverride(name = "buid", column = @Column(name = "buid1"))
	@Valid private Buid buid1 = new Buid();
	
//	@AttributeOverride(name = "buid2", column = @Column(name = "buid2"))
	@Valid private String buid2 ;

	private String pseudo;

	public Leaver() { }

	public Leaver(String Leaver) {
		setLeaver(Leaver);
	}

	public String getLeaver() {
		return leaver + ":" + signature + ":" + buid1 + ":" + buid2 +":"+pseudo;
	}

	public void setLeaver(String leaver) {
		
		logger.info("Parsing Leaver... "+leaver);
		var vals = leaver.split(":");
		this.leaver.setPubkey(vals[0]);
		signature = vals[1];
		buid1.setBuid(vals[2]);
		buid2 = vals[3];
		pseudo = vals[4];
		//this.leaver = joiner;
	}

	public String toRaw() {
		return getLeaver();
	}
	
	@Override
	public String toString() {
		return getLeaver();
	}
}
