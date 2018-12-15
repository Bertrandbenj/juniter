package juniter.core.model.wot;

import juniter.core.model.BStamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Renew implements Serializable, Comparable<Renew> {

	private static final long serialVersionUID = -5880074424437322665L;

	private static final Logger LOG = LogManager.getLogger();

	private String pubkey;

	private String signature;

	private String createdOn;

	private String buid2;

	private String pseudo;

	public Renew(String active) {
		LOG.debug("Parsing Renew... " + active);
		final var vals = active.split(":");
		pubkey = vals[0];
		signature = vals[1];
		createdOn = vals[2];
		buid2 = vals[3];
		pseudo = vals[4];
	}

	public String activepk() {
		return pubkey;
	}

	@Override
	public int compareTo(Renew o) {
		return pubkey.compareTo(o.pubkey) ;
	}

	public BStamp createdOn() {
		return new BStamp(createdOn);
	}

	public String signature(){
		return signature;
	}

	public String toDUP() {
		return pubkey + ":" + signature + ":" + createdOn + ":" + buid2 + ":" + pseudo;
	}

	@Override
	public String toString() {
		return toDUP();
	}
}
