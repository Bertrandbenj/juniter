package juniter.model.persistence;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ex : [ "BASIC_MERKLED_API metab.ucoin.io 88.174.120.187 9201" ]
 * 
 * @author ben
 *
 */
@Embeddable 
public class Buid implements Serializable {

	private static final long serialVersionUID = -165962007943111454L;
	
	private static final Logger logger = LogManager.getLogger();

	@Min(0) 
	@Column(name="bNumber")
	private Integer blockNumber;
	
	@Valid
	@AttributeOverride(name = "hash", column = @Column(name = "bhash"))
	private Hash blockHash = new Hash();

	public Buid() {
	}

	public Buid(String buid) {
		setBuid(buid);
	}

	public String getBuid() {
		return blockNumber + "-" + blockHash;
	}

	public void setBuid(String buid) {
		
		logger.debug("Parsing buid ... "+buid);
		
		String[] pat = buid.split("-");
		blockNumber = Integer.valueOf(pat[0]);
		blockHash.setHash(pat[1]);
	}
	
	@Override
	public String toString() {
		return getBuid();
	}
}