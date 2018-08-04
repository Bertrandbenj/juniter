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
public class BStamp implements Serializable {

	private static final long serialVersionUID = -165962007943111454L;

	private static final Logger logger = LogManager.getLogger();

	@Min(0)
	@Column(name = "bNumber")
	private Integer blockNumber;

	@Valid
	@AttributeOverride(name = "hash", column = @Column(name = "bhash"))
	private Hash blockHash = new Hash();

	public BStamp() {
	}

	public BStamp(String buid) {
		setBStamp(buid);
	}

	public Hash getBlockHash() {
		return blockHash;
	}

	public Integer getBlockNumber() {
		return blockNumber;
	}

	public String getBStamp() {
		return blockNumber + "-" + blockHash.toString();
	}

	public void setBlockHash(Hash blockHash) {
		this.blockHash = blockHash;
	}

	public void setBlockNumber(Integer blockNumber) {
		this.blockNumber = blockNumber;
	}

	public void setBStamp(String buid) {

		logger.debug("Parsing buid ... " + buid);

		final String[] pat = buid.split("-");
		blockNumber = Integer.valueOf(pat[0]);
		blockHash.setHash(pat[1]);
	}

	@Override
	public String toString() {
		return getBStamp();
	}
}