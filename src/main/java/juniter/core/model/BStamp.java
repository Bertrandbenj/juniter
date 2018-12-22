package juniter.core.model;

import juniter.core.utils.Constants;
import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * ex : [ "BASIC_MERKLED_API metab.ucoin.io 88.174.120.187 9201" ]
 *
 * @author ben
 *
 */
//@Entity
@EqualsAndHashCode
@Embeddable
public class BStamp implements Serializable {

	private static final long serialVersionUID = -165962007943111454L;
	private static final Logger LOG = LogManager.getLogger();

	@Min(0)
	@Column(name = "number")
	private Integer number;

	@Pattern(regexp = Constants.Regex.HASH)
	@Column(length = 64)
	@Size(max = 64)
	private String hash;// = new Hash();

	public BStamp() {
	}

	public BStamp(Integer number, String hash) {
		this.number = number;
		this.hash = hash;
	}

	public BStamp(String string) {
		parse(string);
	}

	public String getHash() {
		return hash;
	}

	public Integer getNumber() {
		return number;
	}


	public void parse(String string) {
		try{
			final String[] pat = string.split("-");
			number = Integer.valueOf(pat[0]);
			hash = pat[1];
		}catch(Exception e){
			LOG.error( "Error parsing " + string, e );
		}
	}

	public void setHash(String blockHash) {
		hash = blockHash;
	}

	public void setNumber(Integer blockNumber) {
		number = blockNumber;
	}

	@Override
	public String toString() {
		return number + "-" + hash;
	}
}