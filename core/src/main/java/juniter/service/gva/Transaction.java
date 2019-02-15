package juniter.service.gva;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@GraphQLType(description = "GVA Transaction Object - careful with class name conflict though ")
public class Transaction implements Serializable {
	private static final long serialVersionUID = 7842838617118486285L;


	private String hash;
	private Integer block_number;
	private Integer locktime;
	private Integer version;
	private String currency;
	private String comment;
	private String blockstamp;
	private Integer blockstampTime;
	private Integer time;
	private List<String> inputs;
	private List<String> unlocks;
	private List<String> outputs;
	private List<String> issuers;
	private List<String> signatures;
	//private Boolean written;
	private Boolean removed;
	private Integer received;
	private Integer output_base;
	private Integer output_amount;
	private String written_on;
	private Integer writtenOn;

	@GraphQLNonNull
	public String getWritten_on(){
		return written_on;
	}

	@GraphQLNonNull
	public Integer getWrittenOn(){
		return writtenOn;
	}


	@GraphQLNonNull
	public String getHash() {
		return hash;
	}

	@GraphQLNonNull
	public Integer getLocktime() {
		return locktime;
	}

	@GraphQLNonNull
	public Integer getVersion() {
		return version;
	}

	@GraphQLNonNull
	public String getCurrency() {
		return currency;
	}

	@GraphQLNonNull
	public String getComment() {
		return comment;
	}

	@GraphQLNonNull
	public String getBlockstamp() {
		return blockstamp;
	}

	@GraphQLNonNull
	public List<@GraphQLNonNull String> getInputs() {
		return inputs;
	}

	@GraphQLNonNull
	public List<@GraphQLNonNull String> getUnlocks() {
		return unlocks;
	}

	@GraphQLNonNull
	public List<@GraphQLNonNull String> getOutputs() {
		return outputs;
	}

	@GraphQLNonNull
	public List<@GraphQLNonNull String> getIssuers() {
		return issuers;
	}

	@GraphQLNonNull
	public List<@GraphQLNonNull String> getSignatures() {
		return signatures;
	}


}
