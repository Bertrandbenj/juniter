package juniter.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "transaction", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction implements Serializable {

	private static final long serialVersionUID = -1472028218837984061L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Integer version;
	private String currency;
	private Integer locktime;
	private String hash;
	private String blockstamp;
	private Integer blockstampTime;

	@ElementCollection
	
	private List<String> issuers = new ArrayList<String>();

	@ElementCollection
	private List<String> inputs = new ArrayList<String>();

	@ElementCollection
	private List<String> outputs = new ArrayList<String>();

	@ElementCollection
	private List<String> unlocks = new ArrayList<String>();

	@ElementCollection
	private List<String> signatures = new ArrayList<String>();

	private String comment;

	/**
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @return the locktime
	 */
	public Integer getLocktime() {
		return locktime;
	}

	/**
	 * @return the hash
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * @return the blockstamp
	 */
	public String getBlockstamp() {
		return blockstamp;
	}

	/**
	 * @return the blockstampTime
	 */
	public Integer getBlockstampTime() {
		return blockstampTime;
	}

	/**
	 * @return the issuers
	 */
	public List<String> getIssuers() {
		return issuers;
	}

	/**
	 * @return the inputs
	 */
	public List<String> getInputs() {
		return inputs;
	}

	/**
	 * @return the outputs
	 */
	public List<String> getOutputs() {
		return outputs;
	}

	/**
	 * @return the unlocks
	 */
	public List<String> getUnlocks() {
		return unlocks;
	}

	/**
	 * @return the signatures
	 */
	public List<String> getSignatures() {
		return signatures;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * ex :
	 * Transactions:\nTX:10:1:1:1:2:0:0\n127129-00000232C91EF53648DA67D5DA32DA54C766238B48C512F66C7CC769585DFCBE\n8ysx7yQe47ffx379Evv3R6Qys86ekmVxwYTiVTqWq73e\n9506:0:T:97A239CA02FA2F97B859C2EA093FE68FEADF90A1FDE8EE69711C2048BD328128:1\n0:SIG(0)\n1000:0:SIG(CCdjH7Pd8GPe74ZbiD1DdZ1CXQ2ggYVehk2c7iVV6NwJ)\n8506:0:SIG(8ysx7yQe47ffx379Evv3R6Qys86ekmVxwYTiVTqWq73e)\nEP9BhAMIbDSy9nfplSmmvp7yI6t79kO0/7/bdecGjayH+hrZxT2R4xkpEVyV3qo6Ztc1TwK+F2Hf2big5pVrCA==
	 */
	public String toRaw() {
		return "TX:"+version + ":" + issuers.size() + ":" + inputs.size() + ":" + unlocks.size() + ":" + outputs.size() + ":"
				+ locktime + ":" + blockstampTime + "\n" + blockstamp + "\n"
				+ issuers.stream().collect(Collectors.joining("\n")) + "\n"
				+ inputs.stream().collect(Collectors.joining("\n")) + "\n"
				+ unlocks.stream().collect(Collectors.joining("\n")) + "\n"
				+ outputs.stream().collect(Collectors.joining("\n")) + "\n"
				+ signatures.stream().collect(Collectors.joining("\n"));
	}
}
