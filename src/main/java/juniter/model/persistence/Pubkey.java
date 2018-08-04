package juniter.model.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import juniter.utils.Constants;

@Embeddable
public class Pubkey implements Serializable {

	private static final Logger logger = LoggerFactory.getLogger(Pubkey.class);

	private static final long serialVersionUID = -1179432682292981009L;

	@Size(min = 43, max = 45)
	@Column(length = 45)
	@Pattern(regexp = Constants.Regex.PUBKEY)
	private String pubkey;

	public Pubkey() {
	}

	public Pubkey(String pubKey) {
		setPubkey(pubKey);
	}

	/**
	 * include String and PubKey validation
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof String)
			return ((String) o).equals(pubkey);
		if (o instanceof Pubkey)
			return ((Pubkey) o).pubkey.equals(pubkey);
		return false;
	}

	public String getPubkey() {
		return pubkey;
	}

	public void setPubkey(String pubkey) {
		this.pubkey = pubkey;
	}

	@Override
	public String toString() {
		return pubkey;
	}
}
