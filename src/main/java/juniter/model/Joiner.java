package juniter.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "joiner", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Joiner implements Serializable {

	private static final long serialVersionUID = 4413010134970991059L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name="joiner_", nullable=true, length = 350 )
	private String joiner;

	public Joiner() {

	}

	public Joiner(String joiner) {
		this.joiner = joiner;
	}

	public String getJoiner() {
		return joiner;
	}

	public void setJoiner(String joiner) {
		this.joiner = joiner;
	}

	

	public String toRaw() {
		return joiner;
	}
}
