package juniter.model.wot;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import juniter.model.base.PubKey;
import juniter.utils.Constants;

@Embeddable
public class Certification implements Serializable {

	private static final long serialVersionUID = -2973877562500906569L;

	private static final Logger logger = LogManager.getLogger();


	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "certifier"))
	private PubKey certifier = new PubKey();
	
	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "certified"))
	private PubKey certified = new PubKey();
	
	
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

}
