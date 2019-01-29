package juniter.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TxHistory  implements Serializable {
	
	public interface Summary {}

	private static final long serialVersionUID = -2101815236308787106L;
	
	private String currency = "g1";
	private String pubkey ; 
	private Historizable history ;

	
	public TxHistory(String pk , List<TransactionDTO> sent, List<TransactionDTO> received, List<TransactionDTO> receiving, List<TransactionDTO> sending, List<TransactionDTO> pending) {
		super();
		this.history = new History(sent, received,receiving,sending,pending);
		pubkey = pk;
		
	}


	public TxHistory(String pk , String udlist ) {
		super();
		this.history = new UdDTO();
		pubkey = pk;

	}


	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public class History implements Serializable,  Historizable {

		private static final long serialVersionUID = -5257941146057808644L;


		private List<TransactionDTO> sending;
		private List<TransactionDTO> received;
		private List<TransactionDTO> receiving;
		private List<TransactionDTO> sent;
		private List<TransactionDTO> pending;

	}


	public interface Historizable { }


	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	public class UdDTO implements Serializable,  Historizable{


		private static final long serialVersionUID = -5257941146057812345L;

		Long time ;
		Integer block_number;
		Boolean consumed ;
		Integer amount;
		Integer base;
	}

}
