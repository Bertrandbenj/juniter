package juniter.core.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TransactionDTO  {


	private Integer version;

	private String currency;

	private Integer locktime;

	@JsonProperty("hash")
	private String thash;

	private String blockstamp;

	private String blockstampTime;


	private List<String> issuers = new ArrayList<>(); //

	private List<String> inputs = new ArrayList<>();

	private List<String> outputs = new ArrayList<>();

	private List<String> unlocks = new ArrayList<>();

	private List<String> signatures = new ArrayList<>();

	private String comment;


}
