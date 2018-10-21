package juniter.core.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import juniter.core.utils.Constants;

@Embeddable
public class Hash implements Serializable {

	private static final long serialVersionUID = -1179432322312981009L;

	@Pattern(regexp = Constants.Regex.HASH)
	@Column(length = 64)
	@Size(max = 64)
	protected String hash;

	public Hash() {
	}

	public Hash(String Hash) {
		setHash(Hash);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		return o.toString().equals(hash);
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String Hash) {
		hash = Hash;
	}

	@Override
	public String toString() {
		return getHash();
	}
}
