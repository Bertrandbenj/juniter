package juniter.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * ex : [ "BASIC_MERKLED_API metab.ucoin.io 88.174.120.187 9201" ]
 * 
 * @author ben
 *
 */
@Entity
@Table(name = "buid", schema = "public"
	//,uniqueConstraints = { @UniqueConstraint(columnNames = { "blockNumber", "blockHash" })  }
)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Buid implements Serializable {

	private static final long serialVersionUID = -165962007943111454L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name="blockNumber")
	private String blockNumber;
	
	@Column(name="blockHash")
	private String blockHash;

	public Buid() {
	}

	public Buid(String buid) {
		String[] pat = buid.split("-");
		blockNumber = pat[0];
		blockHash = pat[1];
	}

	public String getBuid() {
		return blockNumber + "-" + blockHash;
	}

	public String blockNumber() {
		return blockNumber;
	}

	public String blockHash() {
		return blockHash;
	}
}