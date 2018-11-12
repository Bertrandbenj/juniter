package juniter.grammar;

import juniter.core.model.BStamp;

public class PeerDocument implements Document {

	String currency;
	String pubkey;
	BStamp block;

	public PeerDocument() {

	}

	public BStamp getBlock() {
		return block;
	}

	public String getCurrency() {
		return currency;
	}

	public String getPubkey() {
		return pubkey;
	}

	@Override
	public boolean isValid() {
		return false;
	}

	public void setBlock(BStamp block) {
		this.block = block;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void setPubkey(String pubkey) {
		this.pubkey = pubkey;
	}

	public String toDUP() {
		return "Version: 10\n" + //
				"Type: Peer\n" + //
				"Currency: " + currency + "\n" + //
				"PublicKey: " + pubkey + "\n" + //
				"Block: " + block + "\n" + //
				"Endpoints:\n";
	}

	@Override
	public String toString() {
		return toDUP();
	}

}
