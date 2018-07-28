package juniter.model.persistence.wot;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
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

import juniter.model.persistence.PubKey;
import juniter.utils.Constants;

@Entity
@Table(name = "Certification", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Certification implements Serializable {

	private static final long serialVersionUID = -2973877562500906569L;

	private static final Logger logger = LogManager.getLogger();
	
	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "certifier"))
	private PubKey certifier = new PubKey();
	
	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "certified"))
	private PubKey certified = new PubKey();
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private Integer blockNumber;
	
	@Pattern(regexp=Constants.Regex.SIGNATURE)
	private String signature;

	public PubKey getCertifier() {
		return certifier;
	}

	public PubKey getCertified() {
		return certified;
	}

	public Integer getBlockNumber() {
		return blockNumber;
	}

	public String getSignature() {
		return signature;
	}

	public Certification() {
	}

	public Certification(String certif) {
		setCertif(certif);
	}
	
	public void setCertif(String certif) {
		
		logger.debug("Parsing certif ... "+certif);

		var it = certif.split(":");
		certifier.setPubkey(it[0]);
		certified.setPubkey(it[1]);
		blockNumber = Integer.valueOf(it[2]);
		signature = it[3];
	}

	public String getCertif() {
		return certifier.getPubkey() + ":" + certified + ":" + blockNumber + ":" + signature; 
	}

	public String toRaw() {
		return getCertif();
	}

	public boolean isCertifiedBy(String cert){
		return certified.getPubkey().equals(cert);
	}
	
	public boolean isCertifierOf(String cert){
		return certifier.getPubkey().equals(cert);
	}
}
