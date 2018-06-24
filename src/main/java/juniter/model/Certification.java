package juniter.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "certification", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Certification implements Serializable {

	private static final long serialVersionUID = -2973877562500906569L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String  certif;
	
//	@Column(name="from")
//	private String from;
//	
//	@Column(name="to")
//	private String to;
//	
//	@Column(name="blockNumber")
//	private String blockNumber;
//	
//	@Column(name="signature")
//	private String signature;

	public Certification() {
	}

	public Certification(String certif) {
		this.certif = certif;
//		from = it[0];
//		to = it[1];
//		blockNumber = it[2];
//		signature = it[3];
	}

	public String getCertif() {
		return from() + ":" + to() + ":" + blockNumber() + ":" + signature(); 
	}

	public String toRaw() {
		return getCertif();
	}

	private String from() {
		return certif.split(":")[0];
	}
	
	private String to() {
		return certif.split(":")[1];
	}
	
	private String blockNumber() {
		return certif.split(":")[2];
	}
	
	private String signature() {
		return certif.split(":")[3];
	}
}
