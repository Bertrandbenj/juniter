package juniter.core.model.wot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Table(name = "certification", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Certification implements Serializable, Comparable<Certification> {

	private static final long serialVersionUID = -2973877562500906569L;
	private static final Logger LOG = LogManager.getLogger();


    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Size(max = 45)
	@Pattern(regexp = Constants.Regex.PUBKEY)
	private  String certifier ;

	@Size(max = 45)
	@Pattern(regexp = Constants.Regex.PUBKEY)
	private  String certified;

	private Integer blockNumber;

	@Size(max = 88)
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

	public Integer getBlockNumber() { return blockNumber; }

	public String getSignature() {
		return signature;
	}


    public void setSignature(String signature) {
        this.signature = signature;
    }
}
