package juniter.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "signature", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true) 
public class Signature implements Serializable {
	private static final long serialVersionUID = -1179432682292981009L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String signature;

	public Signature() {

	}

	public Signature(String signature) {
		this.signature = signature;
	}

	public String getSignature() {
		return signature;
	}

	/**
	 * @return the id
	 */
	protected Long getId() {
		return id;
	}
	

}
