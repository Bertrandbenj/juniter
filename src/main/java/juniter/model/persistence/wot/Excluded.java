package juniter.model.persistence.wot;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import juniter.model.persistence.Pubkey;

@Embeddable
public class Excluded implements Serializable {

	private static final long serialVersionUID = -8542771529353910205L;

	private static final Logger logger = LogManager.getLogger();

	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "joinerKey"))
	Pubkey excluded = new Pubkey();

	public Excluded() { }

	public Excluded(String joiner) {
		setJoiner(joiner);
	}

	public String getExcluded() {
		return excluded.getPubkey();
	}

	public void setJoiner(String joiner) {
		logger.info("Parsing Excluded... " + joiner);
		var vals = joiner.split(":");
		excluded.setPubkey(vals[0]);
	}

	public String toRaw() {
		return getExcluded();
	}

	@Override
	public String toString() {
		return getExcluded();
	}
}
