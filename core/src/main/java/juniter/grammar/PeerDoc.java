package juniter.grammar;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class PeerDoc implements Document {

	private String currency;
	private String pubkey;
	private String block;

	public PeerDoc(String currency, String pubkey, String block) {
		this.currency = currency;
		this.pubkey = pubkey;
		this.block = block;
	}

	@Override
	public boolean isValid() {
		return false;
	}


	public String toDUP() {
		return "Version: 10\n" + //
				"Type: Peer\n" + //
				"Currency: " + currency + "\n" + //
				"PublicKey: " + pubkey + "\n" + //
				"node: " + block + "\n" + //
				"Endpoints:\n";
	}

	@Override
	public String toString() {
		return toDUP();
	}

}
