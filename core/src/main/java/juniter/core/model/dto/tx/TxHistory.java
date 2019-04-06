package juniter.core.model.dto.tx;

import lombok.*;

import java.util.List;

@Data
public class TxHistory   {
	
	public interface Summary {}


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


	@Data
	@AllArgsConstructor
	public class History implements Historizable {


		private List<TransactionDTO> sending;
		private List<TransactionDTO> received;
		private List<TransactionDTO> receiving;
		private List<TransactionDTO> sent;
		private List<TransactionDTO> pending;

	}


	public interface Historizable { }


	@Data
	@NoArgsConstructor
	public class UdDTO implements Historizable{

		Long time ;
		Integer block_number;
		Boolean consumed ;
		Integer amount;
		Integer base;
	}

}
