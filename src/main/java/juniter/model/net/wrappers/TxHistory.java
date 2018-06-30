package juniter.model.net.wrappers;

import java.io.Serializable;
import java.util.List;

import juniter.model.tx.Transaction;

public class TxHistory  implements Serializable {

	private static final long serialVersionUID = -2101815236308787106L;
	
	private String currency = "g1";
	private String pubkey ; 
	private History history ;

	
	public TxHistory(String pk , List<Transaction> sent,List<Transaction> received) {
		super();
		this.history = new History(sent, received);
		pubkey = pk;
		
	}



	public static  class History implements Serializable{
		
		private static final long serialVersionUID = -5257941146057808644L;
		
		private List<Transaction> sent ;
		private List<Transaction> received ;

		
		public History(List<Transaction> sent, List<Transaction> received) {
			super();
			this.sent = sent;
			this.received = received;
		}

		public List<Transaction> getSent() {
			return sent;
		}
		
		public List<Transaction> getReceived() {
			return received;
		}

		
	}


	/**
	 * @return the result
	 */
	public History getHistory() {
		return history;
	}
	
	public String getCurrency() {
		return currency;
	}

	public String getPubkey() {
		return pubkey;
	}


}
