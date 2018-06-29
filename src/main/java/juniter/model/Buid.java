package juniter.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * ex : [ "BASIC_MERKLED_API metab.ucoin.io 88.174.120.187 9201" ]
 * 
 * @author ben
 *
 */
@Embeddable
public class Buid implements Serializable {

	private static final long serialVersionUID = -165962007943111454L;


	@Column(name="blockNumber")
	private Integer blockNumber;
	
	@Column(name="blockHash")
	private String blockHash;

	public Buid() {
	}

	public Buid(String buid) {
		setBuid(buid);
	}

	public String getBuid() {
		return blockNumber + "-" + blockHash;
	}

	public Integer blockNumber() {
		return blockNumber;
	}

	public String blockHash() {
		return blockHash;
	}
	
	public void setBuid(String buid) {
		String[] pat = buid.split("-");
		blockNumber = Integer.valueOf(pat[0]);
		blockHash = pat[1];
	}
}