package juniter.core.model.wot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.crypto.Crypto;
import juniter.core.model.BStamp;
import juniter.core.model.Pubkey;
import juniter.core.model.Signature;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import javax.validation.Valid;
import java.io.Serializable;

/**
 * Pubkey : signature : buid : buid : pseudo;
 *
 * @author ben
 *
 */
@Entity
@Getter
@Setter
@Table(name = "identity", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Identity implements Serializable, Comparable<Identity> {
	private static final Logger LOG = LogManager.getLogger();

	private static final long serialVersionUID = -9160916061297193207L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "newidentity"))
	private Pubkey newidentity = new Pubkey();

	@Valid
	@AttributeOverride(name = "signature", column = @Column(name = "signature"))
	private Signature signature = new Signature();

	@Valid
	@AttributeOverride(name = "buid", column = @Column(name = "createdOn"))
	private BStamp createdOn = new BStamp();

	private String pseudo;

	public Identity() {

	}

	public Identity(String identity) {
		LOG.debug("Parsing Identity... " + identity);
		final var vals = identity.split(":");
		newidentity.setPubkey(vals[0]);
		signature.setSignature(vals[1]);
		createdOn.parse(vals[2]);
		pseudo = vals[3];
	}

	public String getTargetHash(){
		return Crypto.hash(pseudo+createdOn+pub());
	}

	@Override
	public int compareTo(Identity o) {
		return (newidentity + "").compareTo(o.newidentity + "");
	}

	public BStamp createdOn() {
		return createdOn;
	}

	public String pseudo() {
		return pseudo;
	}

	public String signature() {
		return signature.toString();
	}

//	public String getIdentity() {
//		return newidentity.getPubkey()+":"+signature.getSignature()+":"+buid.getBuid()+":"+pseudo;
//	}

	public String pub() {
		return newidentity.getPubkey();
	}

	public String toDUP() {
		return newidentity.getPubkey() + ":" + signature.getSignature() + ":" + createdOn + ":" + pseudo;
	}

	@Override
	public String toString() {
		return toDUP();
	}

}
