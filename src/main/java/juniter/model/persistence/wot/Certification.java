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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import juniter.model.persistence.Pubkey;
import juniter.utils.Constants;

@Entity
@Table(name = "Certification", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Certification implements Serializable, Comparable<Certification> {

	private static final long serialVersionUID = -2973877562500906569L;
	private static final Logger logger = LoggerFactory.getLogger(Certification.class);

	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "certifier"))
	private final Pubkey certifier = new Pubkey();

	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "certified"))
	private final Pubkey certified = new Pubkey();

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Integer blockNumber;

	@Pattern(regexp = Constants.Regex.SIGNATURE)
	private String signature;

	public Certification() {
	}

	public Certification(String certif) {
		setCertif(certif);
	}

	@Override
	public int compareTo(Certification o) {
		return (certifier + " " + certified).compareTo(o.certifier + " " + o.certified);
	}

	public Integer getBlockNumber() {
		return blockNumber;
	}

	public String getCertif() {
		return certifier.getPubkey() + ":" + certified + ":" + blockNumber + ":" + signature;
	}

	public Pubkey getCertified() {
		return certified;
	}

	public Pubkey getCertifier() {
		return certifier;
	}

	public String getSignature() {
		return signature;
	}

	public boolean isCertifiedBy(String cert) {
		return certified.getPubkey().equals(cert);
	}

	public boolean isCertifierOf(String cert) {
		return certifier.getPubkey().equals(cert);
	}

	public void setCertif(String certif) {

		logger.debug("Parsing certif ... " + certif);

		final var it = certif.split(":");
		certifier.setPubkey(it[0]);
		certified.setPubkey(it[1]);
		blockNumber = Integer.valueOf(it[2]);
		signature = it[3];
	}

	public String toRaw() {
		return getCertif();
	}

	@Override
	public String toString() {
		return toRaw();
	}
}
