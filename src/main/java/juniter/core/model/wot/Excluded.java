package juniter.core.model.wot;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Excluded implements Serializable, Comparable<Excluded> {

	private static final long serialVersionUID = -8542771529353910205L;

	private String pubkey;

	public Excluded(String pubkey) {
		this.pubkey = pubkey;
	}

	@Override
	public int compareTo(@NonNull Excluded o) {
		return pubkey.compareTo(o.pubkey);
	}

	public String getExcluded() {
		return toDUP();
	}


	public String toDUP() {
		return pubkey;
	}

	@Override
	public String toString() {
		return toDUP();
	}
}
