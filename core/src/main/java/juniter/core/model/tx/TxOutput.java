package juniter.core.model.tx;

import juniter.core.model.DUPComponent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;

import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * It follows a machine-readable BNF grammar composed of <br>
 *
 * ( and ) characters <br>
 *
 * && and || operators<br>
 *
 * SIG(PUBLIC_KEY), XHX(SHA256_HASH), CLTV(INTEGER), CSV(INTEGER) functions <br>
 *
 * space <br>
 *
 *
 * @author BnimajneB
 *
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TxOutput implements Serializable, Comparable<TxOutput>, DUPComponent {

	private static final long serialVersionUID = 2208036347838232516L;

	static final Logger LOG = LogManager.getLogger();


	private Integer base;
	private Integer amount;

	@Size(max = 255)
	private String condition;

	TxOutput(String output) {
		final var vals = output.split(":");
		amount = Integer.valueOf(vals[0]);
		base = Integer.valueOf(vals[1]);
		try {
			condition = OutCondition.parse(vals[2]).toString();
		} catch (final Exception e) {
			LOG.error("Error parsing " + output, e);
		}
	}

	@Override
	public int compareTo(@NonNull TxOutput o) {
		return toDUP().compareTo(o.toDUP());
	}


	public String getOutputCondition() {
		return condition;
	}


	@Override
	public String toDUP() {
		return amount + ":" + base + ":" + condition;
	}

	@Override
	public String toString() {
		return toDUP();
	}



	public enum OutFunction {
		SIG("SIG"), XHX("XHX"), CLTV("CLTV"), CSV("CSV");

		private final String FCT_TYPE;

		OutFunction(String output) {
			FCT_TYPE = output;
		}

		@Override
		public String toString() {
			return FCT_TYPE;
		}

	}

}
