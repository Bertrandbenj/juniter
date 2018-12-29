package juniter.core.model.wot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "Certification", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Certification implements Serializable, Comparable<Certification> {

	private static final long serialVersionUID = -2973877562500906569L;
	private static final Logger LOG = LoggerFactory.getLogger(Certification.class);




    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private  String certifier ;

	private  String certified;

	private Integer blockNumber;

	private String signature;



	public Certification(){}

	public Certification(String certif) {
		LOG.debug("Parsing certif ... " + certif);

		final var it = certif.split(":");
		certifier = it[0];
		certified = it[1];
		blockNumber = Integer.valueOf(it[2]);
		signature = it[3];
	}

	@Override
	public int compareTo(Certification o) {
		return (certifier + " " + certified).compareTo(o.certifier + " " + o.certified);
	}

	public String toDUP() {
		return certifier + ":" + certified + ":" + blockNumber + ":" + signature;
	}

	@Override
	public String toString() {
		return toDUP();
	}

	public String certifier() {
		return certifier;
	}

	public String certified() {
		return certified;
	}

	public String signature() {
		return signature;
	}

	public Integer getBlockNumber() { return blockNumber;	}


	public Long getId() {
		return id;
	}

	public String getCertifier() {
		return certifier;
	}

	public String getCertified() {
		return certified;
	}

	public String getSignature() {
		return signature;
	}

    public void setId(Long id) {
        this.id = id;
    }

    public void setCertifier(String certifier) {
        this.certifier = certifier;
    }

    public void setCertified(String certified) {
        this.certified = certified;
    }

    public void setBlockNumber(Integer blockNumber) {
        this.blockNumber = blockNumber;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
