package juniter.service.gva.tx;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.types.GraphQLType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter // we overload some to force non null
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@GraphQLType(description = "GVA Transaction Object - careful with class name conflict though ")
public class Transaction implements Serializable {
	private static final long serialVersionUID = 7842838617118486285L;


	String hash;
	Integer block_number;
	Integer locktime;
	Integer version;
	String currency;
	String comment;
	String blockstamp;
	Integer blockstampTime;
	Integer time;
	List<String> inputs;
	List<String> unlocks;
	List<String> outputs;
	List<String> issuers;
	List<String> signatures;
	Boolean written;
	Boolean removed;
	Integer received;
	Integer output_base;
	Integer output_amount; 
	String written_on;
	Integer writtenOn;

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

	@GraphQLNonNull
	public Integer getOutput_amount() {
		return output_amount;
	}
}
