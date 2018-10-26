package juniter.core.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import juniter.core.utils.Constants;

/**
 * ex : [ "BASIC_MERKLED_API metab.ucoin.io 88.174.120.187 9201" ]
 *
 * @author ben
 *
 */
//@Entity
@Embeddable
public class BStamp implements Serializable {

	private static final long serialVersionUID = -165962007943111454L;

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

	@Override
	public boolean equals(Object o) {
		if (o instanceof BStamp)
			return ((BStamp) o).number.equals(number) //
					&& ((BStamp) o).hash.equals(hash);

		return false;
	}

	public String getHash() {
		return hash;
	}

	public Integer getNumber() {
		return number;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public void parse(String string) {
		final String[] pat = string.split("-");
		number = Integer.valueOf(pat[0]);
		hash = pat[1];
	}

	public void setHash(String blockHash) {
		hash = blockHash;
	}

	public void setNumber(Integer blockNumber) {
		number = blockNumber;
	}

	@Override
	public String toString() {
		return number + "-" + hash.toString();
	}
}