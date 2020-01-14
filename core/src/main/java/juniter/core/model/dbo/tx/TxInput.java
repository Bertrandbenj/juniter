package juniter.core.model.dbo.tx;

import juniter.core.model.meta.DUPComponent;
import juniter.core.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class TxInput implements  Comparable<TxInput>, DUPComponent {

	@Min(1)
	private Integer amount;

	@Min(0)
	@Max(0)
	private Integer base;

	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private TxType type;

	@Size(max = 45)
	@Pattern(regexp = Constants.Regex.PUBKEY)
	private String dsource ;

	private Integer dBlockID;

	@Size(max = 64)
	@Pattern(regexp = Constants.Regex.HASH)
	private String tHash;

	private Integer tIndex;


	/**
	 * Build input from String
     *
	 * @param input as a DUPComponent String
	 */
	public TxInput(String input) {
		final var it = input.split(":");
		amount = Integer.valueOf(it[0]);
		base = Integer.valueOf(it[1]);
		type = TxType.valueOf(it[2]);

		if (type.equals(TxType.T)) {
			tHash = it[3];
			tIndex = Integer.valueOf(it[4]);
		}

		if (type.equals(TxType.D)) {
			dsource = it[3];
			dBlockID = Integer.valueOf(it[4]);
		}
	}


	@Override
	public String toDUP() {
		return amount + ":" + base + ":" + type + ":"
				+ (TxType.D.equals(type) ? dsource + ":" + dBlockID : tHash + ":" + tIndex);
	}


	@Override
	public String toString() {
		return toDUP();
	}

	@Override
	public int compareTo(@NonNull TxInput o) {
		return toDUP().compareTo(o.toDUP());
	}
}
