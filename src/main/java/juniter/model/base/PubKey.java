package juniter.model.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import juniter.utils.Constants;


@Embeddable public class PubKey implements Serializable {
	
	private static final long serialVersionUID = -1179432682292981009L;

	@Pattern(regexp=Constants.Regex.PUBKEY) @Size(min=43, max=45)
	@Column(length=45)
	private String pubkey;

	public PubKey() {
	}

	public PubKey(String pubKey) {
		this.pubkey = pubKey;
	}

	public String getPubkey() {
		return pubkey;
	}

	
	public void setPubkey(String pubkey) {
		this.pubkey = pubkey;
	}

	@Override
	public String toString() {
		return this.getPubkey();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PubKey) {
			((PubKey)o).pubkey.equals(pubkey);
		}
		return false;
	}
}
