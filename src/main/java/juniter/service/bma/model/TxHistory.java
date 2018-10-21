package juniter.service.bma.model;

import java.io.Serializable;
import java.util.List;

import juniter.core.model.tx.Transaction;

public class TxHistory  implements Serializable {
	
	public interface Summary {}

	private static final long serialVersionUID = -2101815236308787106L;
	
	private String currency = "g1";
	private String pubkey ; 
	private History history ;

	
	public TxHistory(String pk , List<Transaction> sent, List<Transaction> received, List<Transaction> receiving, List<Transaction> sending, List<Transaction> pending) {
		super();
		this.history = new History(sent, received,receiving,sending,pending);
		pubkey = pk;
		
	}



	public static  class History implements Serializable{
		
		private static final long serialVersionUID = -5257941146057808644L;
		
		
		private List<Transaction> sending ;
		private List<Transaction> received ;
		private List<Transaction> receiving ;		
		private List<Transaction> sent ;
		private List<Transaction> pending ;

		
		public History(List<Transaction> sent, List<Transaction> received, List<Transaction> receiving, List<Transaction> sending, List<Transaction> pending) {
			super();
			this.sent = sent;
			this.received = received;
			this.receiving = receiving;
			this.sending = sending;
			this.pending = pending;
		}

		public List<Transaction> getSent() {
			return sent;
		}
		
		public List<Transaction> getReceived() {
			return received;
		}
		public List<Transaction> getSending() {
			return sending;
		}

		public List<Transaction> getReceiving() {
			return receiving;
		}

		public List<Transaction> getPending() {
			return pending;
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
