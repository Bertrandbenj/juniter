package juniter.core.model.wot;

import juniter.core.model.business.DUPComponent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.Embeddable;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Excluded implements DUPComponent, Comparable<Excluded> {

	private String pubkey;

	public Excluded(String pubkey) {
		this.pubkey = pubkey;
	}

	@Override
	public int compareTo(@NonNull Excluded o) {
		return pubkey.compareTo(o.pubkey);
	}

	public String toDUP() {
		return pubkey;
	}

	@Override
	public String toString() {
		return toDUP();
	}
}
