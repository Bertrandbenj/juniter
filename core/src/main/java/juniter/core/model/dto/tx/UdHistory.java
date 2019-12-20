package juniter.core.model.dto.tx;

import lombok.Data;

import java.util.List;

@Data
public class UdHistory {


	private String currency = "g1";
	private String pubkey ;
	private List<UdDTO> history ;


	public UdHistory(String pk ,  List<UdDTO> history) {
		super();
		this.history = history;
		pubkey = pk;

	}


}
