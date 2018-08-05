package juniter.model.bma;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TransactionDTO implements Serializable {

	private static final long serialVersionUID = 7842838617478486285L;

	private Integer version;

	private String currency;

	private Integer locktime;

	@JsonProperty("hash")
	private String thash;

	private String blockstamp;

	private String blockstampTime;

	private List<String> issuers = new ArrayList<>(); //

	private List<String> inputs = new ArrayList<>();

	private List<String> outputs = new ArrayList<>();

	private List<String> unlocks = new ArrayList<>();

	private List<String> signatures = new ArrayList<>();

	private String comment;

	public String getBlockstamp() {
		return blockstamp;
	}

	public String getBlockstampTime() {
		return blockstampTime;
	}

	public String getComment() {
		return comment;
	}

	public String getCurrency() {
		return currency;
	}

	public List<String> getInputs() {
		return inputs;
	}

	public List<String> getIssuers() {
		return issuers;
	}

	public Integer getLocktime() {
		return locktime;
	}

	public List<String> getOutputs() {
		return outputs;
	}

	public List<String> getSignatures() {
		return signatures;
	}

	public String getThash() {
		return thash;
	}

	public List<String> getUnlocks() {
		return unlocks;
	}

	public Integer getVersion() {
		return version;
	}

	public void setBlockstamp(String blockstamp) {
		this.blockstamp = blockstamp;
	}

	public void setBlockstampTime(String blockstampTime) {
		this.blockstampTime = blockstampTime;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setInputs(List<String> inputs) {
		this.inputs = inputs;
	}

	public void setIssuers(List<String> issuers) {
		this.issuers = issuers;
	}

	public void setLocktime(Integer locktime) {
		this.locktime = locktime;
	}

	public void setOutputs(List<String> outputs) {
		this.outputs = outputs;
	}

	public void setSignatures(List<String> signatures) {
		this.signatures = signatures;
	}

	public void setThash(String hash) {
		thash = hash;
	}

	public void setUnlocks(List<String> unlocks) {
		this.unlocks = unlocks;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
