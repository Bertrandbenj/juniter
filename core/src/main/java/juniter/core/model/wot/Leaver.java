package juniter.core.model.wot;

import juniter.core.model.BStamp;
import juniter.core.model.DUPComponent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;

import javax.persistence.Embeddable;
import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Leaver implements DUPComponent, Serializable, Comparable<Leaver> {

	private static final long serialVersionUID = -4288798570176707871L;

	private static final Logger LOG = LogManager.getLogger();

	private String pubkey ;

	private String signature;

	private String createdOn;

	private String i_block_uid;

	private String uid;


	public Leaver(String leaver) {
		LOG.debug("Parsing Leaver... " + leaver);
		final var vals = leaver.split(":");
		pubkey = vals[0];
		signature = vals[1];
		createdOn = vals[2];
		i_block_uid = vals[3];
		uid = vals[4];
	}

	@Override
	public int compareTo(@NonNull Leaver o) {
		return toDUP().compareTo(o.toDUP());
	}

	public String toDUP() {
		return pubkey + ":" + signature + ":" + createdOn + ":" + i_block_uid + ":" + uid;
	}

	@Override
	public String toString() {
		return toDUP();
	}

	public BStamp createdOn() {
		return new BStamp(createdOn);
	}
}
