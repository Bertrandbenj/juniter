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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "joiner", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Joiner implements Serializable {

	private static final Logger logger = LogManager.getLogger();

	private static final long serialVersionUID = 4413010134970991059L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "joiner", nullable = true, length = 350)
	private String joiner;

	@Valid
	@AttributeOverride(name = "pubkey", column = @Column(name = "joinerKey"))
	private PubKey joinerKey = new PubKey();

	private String signature;

	@AttributeOverride(name = "buid.buid", column = @Column(name = "buid1"))
	@Valid private Buid buid1 = new Buid();
	
	@AttributeOverride(name = "buid2", column = @Column(name = "buid2"))
	@Valid private Buid buid2 = new Buid();

	private String pseudo;

	public Joiner() {

	}

	public Joiner(String joiner) {
		setJoiner(joiner);
	}

	public String getJoiner() {
		return joinerKey.getPubkey() + ":" + signature + ":" + buid1.getBuid() + ":" + buid2.getBuid()+":"+pseudo;
	}

	public void setJoiner(String joiner) {
		
		logger.info("Parsing Joiner... "+joiner);
		var vals = joiner.split(":");
		joinerKey.setPubkey(vals[0]);
		signature = vals[1];
		buid1.setBuid(vals[2]);
		buid2.setBuid(vals[3]);
		pseudo = vals[4];
		this.joiner = joiner;
	}

	public String toRaw() {
		return joiner;
	}
}
