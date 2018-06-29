package juniter.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import juniter.utils.Constants;

@Entity
@Table(name = "certification", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Certification implements Serializable {

	private static final long serialVersionUID = -2973877562500906569L;

	private static final Logger logger = LogManager.getLogger();

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	
	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "from"))
	@Embedded protected PubKey from = new PubKey();
	
	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "to"))
	@Embedded protected PubKey to = new PubKey();
	
	
	private Integer blockNumber;
	
	@Pattern(regexp=Constants.Regex.SIGNATURE)
	private String signature;

	public Certification() {
	}

	public Certification(String certif) {
		setCertif(certif);
	}
	
	public void setCertif(String certif) {
		
		logger.debug("Parsing certif ... "+certif);

		var it = certif.split(":");
		from.setPubkey(it[0]);
		to.setPubkey(it[1]);
		blockNumber = Integer.valueOf(it[2]);
		signature = it[3];
	}

	public String getCertif() {
		return from + ":" + to + ":" + blockNumber + ":" + signature; 
	}

	public String toRaw() {
		return getCertif();
	}

}
