package juniter.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "identity", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Identity implements Serializable {

	private static final long serialVersionUID = -9160916061297193207L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name="identity_", nullable=true, length = 350 )
	private String identitiy;

	public Identity() {

	}

	public Identity(String identity) {
		this.identitiy = identity;
	}

	public String getIdentity() {
		return identitiy;
	}

	public void setIdentity(String identity) {
		this.identitiy = identity;
	}

	public String toRaw() {
		return identitiy;
	}
}
