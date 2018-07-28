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
import juniter.model.persistence.Signature;

/**
 * Pubkey : signature : buid : buid : pseudo;
 * 
 * @author ben
 *
 */
@Embeddable
public class Identity implements Serializable {
	private static final Logger logger = LogManager.getLogger();

	private static final long serialVersionUID = -9160916061297193207L;


	@Valid 
	@AttributeOverride(name = "pubkey", column = @Column(name = "newidentity"))
	private PubKey newidentity = new PubKey();
	
	@Valid 
	@AttributeOverride(name = "signature", column = @Column(name = "signature"))
	private Signature signature = new Signature();
	
	@Valid 
	@AttributeOverride(name = "buid", column = @Column(name = "buid"))
	private Buid buid = new Buid();
	
	private String pseudo;
	

	public Identity() {

	}

	public Identity(String identity) {
		setIdentity(identity);
	}

	public String getIdentity() {
		return newidentity+":"+signature+":"+buid+":"+pseudo;
	}

	public void setIdentity(String identity) {
		logger.debug("Parsing Identity... "+identity);
		var vals = identity.split(":");
		newidentity.setPubkey(vals[0]);
		signature.setSignature(vals[1]);
		buid.setBuid(vals[2]);
		pseudo = vals[3];
	}

	public String toRaw() {
		return getIdentity();
	}
	
	public String toSring() {
		return getIdentity();
	}

	
}
