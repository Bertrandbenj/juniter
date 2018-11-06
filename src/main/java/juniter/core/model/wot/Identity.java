package juniter.core.model.wot;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import juniter.core.model.BStamp;
import juniter.core.model.Pubkey;
import juniter.core.model.Signature;

/**
 * Pubkey : signature : buid : buid : pseudo;
 *
 * @author ben
 *
 */
@Entity
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
	@AttributeOverride(name = "buid", column = @Column(name = "buid"))
	private BStamp buid = new BStamp();

	private String pseudo;

	public Identity() {

	}

	public Identity(String identity) {
		setIdentity(identity);
	}

	@Override
	public int compareTo(Identity o) {
		return (newidentity + "").compareTo(o.newidentity + "");
	}

	public BStamp createdOn() {
		return buid;
	}

	public String pseudo() {
		return pseudo;
	}

//	public String getIdentity() {
//		return newidentity.getPubkey()+":"+signature.getSignature()+":"+buid.getBuid()+":"+pseudo;
//	}

	public String pub() {
		return newidentity.getPubkey();
	}

//	public String toRaw() {
//		return getIdentity();
//	}

	public void setIdentity(String identity) {
		LOG.debug("Parsing Identity... " + identity);
		final var vals = identity.split(":");
		newidentity.setPubkey(vals[0]);
		signature.setSignature(vals[1]);
		buid.parse(vals[2]);
		pseudo = vals[3];
	}

	@Override
	public String toString() {
		return newidentity.getPubkey() + ":" + signature.getSignature() + ":" + buid + ":" + pseudo;
	}

}