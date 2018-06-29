package juniter.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;

@Entity
@Table(name = "identity", schema = "public")
public class Identity implements Serializable {

	private static final long serialVersionUID = -9160916061297193207L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name="identity_", nullable=true, length = 350 )
	private String identitiy;
	
	@AttributeOverride(name = "pubkey", column = @Column(name = "newidentity"))
	@Valid private PubKey newidentity= new PubKey();
	
	
	private String signature; 
	
	@Valid private Buid buid = new Buid();
	
	private String pseudo;
	
	

	public Identity() {

	}

	public Identity(String identity) {
		setIdentity(identity);
	}

	public String getIdentity() {
		return newidentity+":"+signature+":"+buid.getBuid()+":"+pseudo;
	}

	public void setIdentity(String identity) {
		var vals = identity.split(":");
		newidentity.setPubkey(vals[0]);
		setSignature(vals[1]);
		buid.setBuid(vals[2]);
		setPseudo(vals[3]);
		this.identitiy = identity;
	}

	public String toRaw() {
		return identitiy;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public void setPseudo(String pseudo) {
		this.pseudo = pseudo;
	}
}
