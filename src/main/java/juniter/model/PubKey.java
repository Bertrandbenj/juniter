package juniter.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import utils.Constants;

@Entity
@Table(name = "pubkey", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true) 
public class PubKey implements Serializable {
	private static final long serialVersionUID = -1179432682292981009L;
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Long id;

	@Id 
	@Pattern(regexp=Constants.Regex.PUBKEY) @Size(min=43, max=44)
	private String pubKey;

	public PubKey() {

	}

	public PubKey(String pubKey) {
		this.pubKey = pubKey;
	}

	public String getPubKey() {
		return pubKey;
	}

//	/**
//	 * @return the id
//	 */
//	protected Long getId() {
//		return id;
//	}
	
	@Override
	public String toString() {
		return this.getPubKey();
	}

}
