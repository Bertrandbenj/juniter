package juniter.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import juniter.utils.Constants;

/**
 * ex : [ "BASIC_MERKLED_API metab.ucoin.io 88.174.120.187 9201" ]
 * 
 * @author ben
 *
 */
@Embeddable public class Buid implements Serializable {

	private static final long serialVersionUID = -165962007943111454L;
	
	private static final Logger logger = LogManager.getLogger();

	@Column(insertable=false,updatable=false)
	private Integer blockNumber;
	
	@Pattern(regexp = Constants.Regex.HASH)
	@Size(max = 64)
	@Column(insertable=false,updatable=false)
	private String blockHash;

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
		blockHash = pat[1];
	}
}