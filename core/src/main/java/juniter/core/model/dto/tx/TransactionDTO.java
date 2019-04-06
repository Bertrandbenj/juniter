package juniter.core.model.dto.tx;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TransactionDTO  {


	private Integer version;

	private String currency;

	private Integer locktime;

	private String hash;

	private String blockstamp;

	private String blockstampTime;


	private List<String> issuers = new ArrayList<>(); //

	private List<String> inputs = new ArrayList<>();

	private List<String> outputs = new ArrayList<>();

	private List<String> unlocks = new ArrayList<>();

	private List<String> signatures = new ArrayList<>();

	private String comment;


}
