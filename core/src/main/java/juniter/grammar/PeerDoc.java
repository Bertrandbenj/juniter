package juniter.grammar;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeerDoc implements Document {

	private String currency;
	private String pubkey;
	private String block;

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
