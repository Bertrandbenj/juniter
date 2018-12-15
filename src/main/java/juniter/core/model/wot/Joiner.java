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
public class Joiner implements Serializable, Comparable<Joiner> {

	private static final Logger LOG = LogManager.getLogger();

	private static final long serialVersionUID = 4413010134970991059L;


	private String pubkey;

	private String signature;

	private String createdOn;

	private String i_block_uid;

	private String pseudo;



	public Joiner(String joiner) {
		LOG.debug("Parsing Joiner... " + joiner);
		final var vals = joiner.split(":");
		pubkey = vals[0];
		signature = vals[1];
		createdOn = vals[2];
		i_block_uid = vals[3];
		pseudo = vals[4];
	}


	@Override
	public int compareTo(Joiner o) {
		return pubkey.compareTo(o.pubkey);
	}



	public String bstamp() { return createdOn; }
	public BStamp createdOn() {
		return new BStamp(createdOn);
	}

	public String pseudo() {
		return pseudo;
	}
	public String pub() {
		return pubkey;
	}
	public String signature() {
		return signature;
	}


	public String toDUP() {
		return pubkey + ":" + signature + ":" + createdOn + ":" + i_block_uid + ":" + pseudo;
	}

	@Override
	public String toString() {
		return toDUP();
	}

	public BStamp writtenOn() {
		return new BStamp(i_block_uid);
	}
}
