package juniter.core.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import juniter.core.utils.Constants;

@Embeddable
public class Pubkey implements Serializable, Comparable<Pubkey> {

	private static final Logger logger = LoggerFactory.getLogger(Pubkey.class);

	private static final long serialVersionUID = -1179432682292981009L;

	@Size(max = 45)
	@Column(length = 45)
	@Pattern(regexp = Constants.Regex.PUBKEY)
	private String pubkey;

	public Pubkey() {
	}

	public Pubkey(String pubKey) {
		setPubkey(pubKey);
	}

	@Override
	public int compareTo(Pubkey o) {
		// TODO Auto-generated method stub
		return pubkey.compareTo(o.pubkey);
	}

	/**
	 * include String and PubKey validation
	 */
	@Override
	public boolean equals(Object o) {
		return o.toString().equals(pubkey);
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
