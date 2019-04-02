package juniter.core.model.dbo.wot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.utils.Constants;
import lombok.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "wot_certification", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Certification implements Comparable<Certification> {

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


}
