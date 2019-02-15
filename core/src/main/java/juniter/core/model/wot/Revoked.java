package juniter.core.model.wot;

import juniter.core.model.business.DUPComponent;
import juniter.core.utils.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;

import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Revoked implements DUPComponent, Serializable, Comparable<Revoked> {

	private static final long serialVersionUID = 2875594811917743111L;

	private static final Logger LOG = LogManager.getLogger();

	@Size(max = 45)
	@Pattern(regexp = Constants.Regex.PUBKEY)
	private String revoked;

	private String signature;


	public Revoked(String rev) {
		final var vals = rev.split(":");
		revoked = vals[0] ;
		signature = vals[1];
		LOG.debug("Parsed Revoked... " + rev);
	}

	@Override
	public int compareTo(@NonNull Revoked o) {
		return toDUP().compareTo(o.toDUP());
	}


	public String toDUP() {
		return revoked + ":" + signature;
	}

	@Override
	public String toString() {
		return toDUP();
	}

}