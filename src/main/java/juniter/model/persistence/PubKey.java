package juniter.model.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import juniter.utils.Constants;


@Embeddable public class PubKey implements Serializable 
{
	private static final Logger logger = LogManager.getLogger();

	private static final long serialVersionUID = -1179432682292981009L;
	
	@Size(min=43, max=45)
	@Column(length=45)
	@Pattern(regexp=Constants.Regex.PUBKEY)
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

	/**
	 * include String and PubKey validation 
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof String) {
			return ((String)o).equals(pubkey);
		}
		if (o instanceof PubKey) {
			return ((PubKey)o).pubkey.equals(pubkey);
		}
		return false;
	}
}
