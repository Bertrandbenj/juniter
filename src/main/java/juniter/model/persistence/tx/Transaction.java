package juniter.model.persistence.tx;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.AttributeOverride;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import juniter.model.persistence.BStamp;
import juniter.model.persistence.Hash;
import juniter.model.persistence.Pubkey;
import juniter.model.persistence.Signature;
import juniter.utils.Constants;

@Entity
@Table(name = "transaction", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction implements Serializable {

	private static final Logger logger = LogManager.getLogger();

	private static final long serialVersionUID = -1472028218837984061L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

//	@JsonIgnoreProperties()
	private Integer version;

	@Pattern(regexp = Constants.Regex.G1)
	private String currency;

//	@JsonView(TxHistory.Summary.class)
	private Integer locktime;

	@Valid
	@AttributeOverride(name = "hash", column = @Column(name = "tx_hash"))
	private Hash hash = new Hash();

	@Valid
	@AttributeOverride(name = "buid", column = @Column(name = "blockstamp"))
	private BStamp blockstamp = new BStamp();

	private Integer blockstampTime;

	@Valid
	@ElementCollection
	@CollectionTable(name = "tx_issuers", joinColumns = @JoinColumn(name = "tx_id"))
	private List<Pubkey> issuers = new ArrayList<>(); //

	@Valid
	@ElementCollection
	@CollectionTable(name = "tx_inputs", joinColumns = @JoinColumn(name = "tx_id"))
	private List<TxInput> inputs = new ArrayList<>();

	@Valid
	@ElementCollection
	@CollectionTable(name = "tx_outputs", joinColumns = @JoinColumn(name = "tx_id"))
	private List<TxOutput> outputs = new ArrayList<>();

	@Valid
	@ElementCollection
	@CollectionTable(name = "tx_unlocks", joinColumns = @JoinColumn(name = "tx_id"))
	private List<TxUnlock> unlocks = new ArrayList<>();

	@Valid
	@ElementCollection
	@CollectionTable(name = "tx_signatures", joinColumns = @JoinColumn(name = "tx_id"))
	private List<Signature> signatures = new ArrayList<>();

	@Size(max = 255)
	private String comment;

	/**
	 * @return the blockstamp
	 */
	public @Valid BStamp getBlockstamp() {
		return blockstamp; // .getBuid();
	}

	/**
	 * @return the blockstampTime
	 */
	public Integer getBlockstampTime() {
		return blockstampTime;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @return the hash
	 */
	public Hash getHash() {
		return hash;// .toString();
	}

	/**
	 * @return the inputs
	 */
	public List<TxInput> getInputs() {
		return inputs;// .stream().map(TxInput::getInput).collect(Collectors.toList());
	}

	/**
	 * @return the issuers
	 */
	public List<Pubkey> getIssuers() {
		return issuers;// .stream().map(Pubkey::getPubkey).collect(Collectors.toList());
	}

	/**
	 * @return the locktime
	 */
	public Integer getLocktime() {
		return locktime;
	}

	public List<TxOutput> getOutputs() {
		return outputs;
	}

	// /**
//	 * @return the outputs
//	 */
//	public List<String> getOutputs() {
//		return outputs.stream().map(TxOutput::getOutput).collect(Collectors.toList());
//	}
//
	/**
	 * @return the signatures
	 */
	public List<String> getSignatures() {
		return signatures.stream().map(Signature::toString).collect(Collectors.toList());
	}
//
//	/**
//	 * @return the unlocks
//	 */
//	public List<String> getUnlocks() {
//		return unlocks.stream().map(TxUnlock::getUnlock).collect(Collectors.toList());
//	}

	public List<TxUnlock> getUnlocks() {
		return unlocks;
	}

	/**
	 * @return the version
	 */
	public Integer getVersion() {
		return version;
	}

	public boolean isValid() {
		return true;
	}

	/**
	 * ex :
	 * Transactions:\nTX:10:1:1:1:2:0:0\n127129-00000232C91EF53648DA67D5DA32DA54C766238B48C512F66C7CC769585DFCBE\n8ysx7yQe47ffx379Evv3R6Qys86ekmVxwYTiVTqWq73e\n9506:0:T:97A239CA02FA2F97B859C2EA093FE68FEADF90A1FDE8EE69711C2048BD328128:1\n0:SIG(0)\n1000:0:SIG(CCdjH7Pd8GPe74ZbiD1DdZ1CXQ2ggYVehk2c7iVV6NwJ)\n8506:0:SIG(8ysx7yQe47ffx379Evv3R6Qys86ekmVxwYTiVTqWq73e)\nEP9BhAMIbDSy9nfplSmmvp7yI6t79kO0/7/bdecGjayH+hrZxT2R4xkpEVyV3qo6Ztc1TwK+F2Hf2big5pVrCA==
	 */
	public String toRaw() {
		return "TX:" + version + ":" + issuers.size() + ":" + inputs.size() + ":" + unlocks.size() + ":"
				+ outputs.size() + ":" + locktime + ":" + blockstampTime + "\n" + blockstamp + "\n"
				+ issuers.stream().map(i -> i.getPubkey()).collect(Collectors.joining("\n")) + "\n"
				+ inputs.stream().map(in -> in.getInput()).collect(Collectors.joining("\n")) + "\n" //
				+ unlocks.stream().map(in -> in.toString()).collect(Collectors.joining("\n")) + "\n"
				+ outputs.stream().map(in -> in.getOutput()).collect(Collectors.joining("\n")) + "\n"
				+ signatures.stream().map(in -> in.getSignature()).collect(Collectors.joining("\n"));
	}

	public boolean txReceivedBy(Object pubkey) {
		return issuers.stream().anyMatch(pk -> pk.equals(pubkey));
	}

	public boolean txSentBy(Object pubkey) {
		return issuers.stream().anyMatch(pk -> pk.equals(pubkey));
	}

}
