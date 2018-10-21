package juniter.core.model.tx;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import juniter.core.model.Hash;
import juniter.core.model.Pubkey;

//@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class TxInput implements Serializable, Comparable<TxInput> {

	private static final long serialVersionUID = 860920319125591515L;

	private static final Logger logger = LogManager.getLogger();

	@Min(1)
	private Integer amount;

	@Min(0)
	@Max(0)
	private Integer base;

	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private TxType type;

	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "dsource"))
	private Pubkey dsource = new Pubkey();

	private Integer dBlockID;

	@Valid
	@AttributeOverride(name = "hash", column = @Column(name = "thash"))
	private Hash tHash = new Hash();

	private Integer tIndex;

	public TxInput() {
	}

	public TxInput(String input) {
		setInput(input);
	}

	@Override
	public int compareTo(TxInput o) {

		return getInput().compareTo(o.getInput());
	}

	public Integer getAmount() {
		return amount;
	}

	public Integer getBase() {
		return base;
	}

	public Integer getDBlockID() {
		return dBlockID;
	}

	public Pubkey getDsource() {
		return dsource;
	}

	public String getInput() {
		return amount + ":" + base + ":" + type + ":"
				+ (TxType.D.equals(type) ? dsource + ":" + dBlockID : tHash + ":" + tIndex);
	}

	public Hash getTHash() {
		return tHash;
	}

	public Integer getTIndex() {
		return tIndex;
	}

	public TxType getType() {
		return type;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public void setBase(Integer base) {
		this.base = base;
	}

	public void setdBlockID(Integer dBlockID) {
		this.dBlockID = dBlockID;
	}

	public void setdSource(Pubkey dSource) {
		dsource = dSource;
	}

	public void setInput(String input) {
		logger.debug("parse TxInput ... " + input);
		// this.input = input;
		final var it = input.split(":");
		amount = Integer.valueOf(it[0]);
		base = Integer.valueOf(it[1]);
		setType(TxType.valueOf(it[2]));

		if (type.equals(TxType.T)) {
			tHash.setHash(it[3]);
			tIndex = Integer.valueOf(it[4]);
		}

		if (type.equals(TxType.D)) {
			dsource.setPubkey(it[3]);
			dBlockID = Integer.valueOf(it[4]);
		}
	}

	public void settHash(String tHash) {
		this.tHash.setHash(tHash);
	}

	public void settIndex(Integer tIndex) {
		this.tIndex = tIndex;
	}

	public void setType(TxType txType) {
		type = txType;
	}

	@Override
	public String toString() {
		return getInput();
	}
}
