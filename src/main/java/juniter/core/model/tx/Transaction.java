package juniter.core.model.tx;

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
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import juniter.core.model.BStamp;
import juniter.core.model.DUPComponent;
import juniter.core.model.Pubkey;
import juniter.core.model.Signature;
import juniter.core.utils.Constants;

@Entity
@Table(name = "transaction", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction implements Serializable, DUPComponent {

	private static final Logger LOG = LogManager.getLogger();

	private static final long serialVersionUID = -1472028218837984061L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	protected Long id;

	//	@JsonIgnoreProperties()
	protected Integer version;

	@Pattern(regexp = Constants.Regex.G1)
	protected String currency;

	//	@JsonView(TxHistory.Summary.class)
	protected Integer locktime;

	@Valid
	@JsonProperty("hash")
	protected String thash;// = new Hash();

	@Valid
	@AttributeOverride(name = "buid", column = @Column(name = "blockstamp"))
	protected BStamp blockstamp = new BStamp();

	protected Integer blockstampTime;

	@Valid
	@LazyCollection(LazyCollectionOption.FALSE)
	@ElementCollection
	@CollectionTable(name = "tx_issuers", joinColumns = @JoinColumn(name = "tx_id"))
	protected List<Pubkey> issuers = new ArrayList<>(); //

	@Valid
	@LazyCollection(LazyCollectionOption.FALSE)
	@ElementCollection
	@CollectionTable(name = "tx_inputs", joinColumns = @JoinColumn(name = "tx_id"))
	protected List<TxInput> inputs = new ArrayList<>();

	@Valid
	@LazyCollection(LazyCollectionOption.FALSE)
	@ElementCollection
	@CollectionTable(name = "tx_outputs", joinColumns = @JoinColumn(name = "tx_id"))
	protected List<TxOutput> outputs = new ArrayList<>();

	@Valid
	@LazyCollection(LazyCollectionOption.FALSE)
	@ElementCollection
	@CollectionTable(name = "tx_unlocks", joinColumns = @JoinColumn(name = "tx_id"))
	protected List<TxUnlock> unlocks = new ArrayList<>();

	@Valid
	@LazyCollection(LazyCollectionOption.FALSE)
	@ElementCollection
	@CollectionTable(name = "tx_signatures", joinColumns = @JoinColumn(name = "tx_id"))
	protected List<Signature> signatures = new ArrayList<>();

	@Size(max = 255)
	protected String comment;

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

	/**
	 * @return the hash
	 */
	public String getThash() {
		return thash;// .toString();
	}

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

	@Override
	public String toDUP() {
		return toDUPdoc(true);
	}

	/**
	 * <pre>
	 * ex :
	Version: 10
	Type: Transaction
	Currency: g1
	Blockstamp: 12345
	Locktime: 98765
	Issuers:
	HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd
	GgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd
	Inputs:
	25:2:T:6991C993631BED4733972ED7538E41CCC33660F554E3C51963E2A0AC4D6453D3:0
	25:2:T:6991C993631BED4733972ED7538E41CCC33660F554E3C51963E2A0AC4D6453D3:65
	Unlocks:
	0:SIG(1)
	0:XHX(1)
	Outputs:
	50:2:(SIG(HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd) || (SIG(DNann1Lh55eZMEDXeYt59bzHbA3NJR46DeQYCS2qQdLV) && XHX(309BC5E644F797F53E5A2065EAF38A173437F2E6)))
	50:2:XHX(8AFC8DF633FC158F9DB4864ABED696C1AA0FE5D617A7B5F7AB8DE7CA2EFCD4CB)
	Signatures:
	DpVMlf6vaW0q+WVtcZlEs/XnDz6WtJfA448qypOqRbpi7voRqDaS9R/dG4COctxPg6sqXRbfQDieeDKU7IZWBA==
	DpVMlf6vaW0q+WVtcZlEs/XnDz6WtJfA448qypOqRbpi7voRqDaS9R/dG4COctxPg6sqXRbfQDieeDKU7IZWBA==
	Comment: huhuhaha
	 * </pre>
	 */
	public String toDUPdoc(boolean signed) {
		return "Version: " + version +
				"\nType: Transaction" +
				"\nCurrency: g1" +
				"\nBlockstamp: " + blockstamp +
				"\nLocktime: " + locktime +
				"\nIssuers:\n" + issuers.stream().map(i -> i.getPubkey()).collect(Collectors.joining("\n")) +

				"\nInputs:\n" + inputs.stream().map(in -> in.getInput()).collect(Collectors.joining("\n")) +
				"\nUnlocks:\n" + unlocks.stream().map(in -> in.toDUP()).collect(Collectors.joining("\n")) +
				"\nOutputs:\n" + outputs.stream().map(in -> in.getOutput()).collect(Collectors.joining("\n")) +

				(signed ? "\nSignatures:\n" +
						signatures.stream().map(in -> in.getSignature()).collect(Collectors.joining("\n"))
						: "")
				+

				"\nComment: " + comment + "\n"
				;
	}

	/**
	 * <pre>
	 * TX:VERSION:NB_ISSUERS:NB_INPUTS:NB_UNLOCKS:NB_OUTPUTS:HAS_COMMENT:LOCKTIME
	 *
	 * ex : Transactions:\nTX:10:1:1:1:2:0:0
	 * 127129-00000232C91EF53648DA67D5DA32DA54C766238B48C512F66C7CC769585DFCBE
	 * 8ysx7yQe47ffx379Evv3R6Qys86ekmVxwYTiVTqWq73e
	 * 9506:0:T:97A239CA02FA2F97B859C2EA093FE68FEADF90A1FDE8EE69711C2048BD328128:1
	 * 0:SIG(0)
	 * 1000:0:SIG(CCdjH7Pd8GPe74ZbiD1DdZ1CXQ2ggYVehk2c7iVV6NwJ)
	 * 8506:0:SIG(8ysx7yQe47ffx379Evv3R6Qys86ekmVxwYTiVTqWq73e)
	 * EP9BhAMIbDSy9nfplSmmvp7yI6t79kO0/7/bdecGjayH+hrZxT2R4xkpEVyV3qo6Ztc1TwK+F2Hf2big5pVrCA==
	 * </pre>
	 */
	public String toDUPshort(boolean signed) {

		final var hasComment = comment != null && !comment.equals("") ;

		return "TX:" + version + ":" + issuers.size() + ":" + inputs.size() + ":" + unlocks.size() + ":"
		+ outputs.size() + ":" + (hasComment ? 1 : 0) + ":" + locktime + "\n" + blockstamp + "\n"
		+ issuers.stream().map(i -> i.getPubkey()).collect(Collectors.joining("\n")) + "\n"
		+ inputs.stream().map(in -> in.getInput()).collect(Collectors.joining("\n")) + "\n" //
		+ unlocks.stream().map(in -> in.toString()).collect(Collectors.joining("\n")) + "\n"
		+ outputs.stream().map(in -> in.getOutput()).collect(Collectors.joining("\n")) + "\n"
		+ (hasComment ? comment + "\n" : "")
		+ (signed ? //
				signatures.stream().map(in -> in.getSignature()).collect(Collectors.joining("\n"))
				: "");
	}

	public boolean txReceivedBy(Object pubkey) {
		return issuers.stream().anyMatch(pk -> pk.equals(pubkey));
	}

	public boolean txSentBy(Object pubkey) {
		return issuers.stream().anyMatch(pk -> pk.equals(pubkey));
	}

}
