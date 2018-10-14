package juniter.model.persistence.tx;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
public class TxOutput implements Serializable, Comparable<TxOutput> {

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

	private static final long serialVersionUID = 2208036347838232516L;
	static final Logger logger = LogManager.getLogger();
	private Integer base;
	private Integer amount;

	private String condition;

	public TxOutput() {
	}

	public TxOutput(String output) {
		setOutput(output);
	}

	@Override
	public int compareTo(TxOutput o) {
		return getOutput().compareTo(o.getOutput());
	}

	public Integer getAmount() {
		return amount;
	}

	public Integer getBase() {
		return base;
	}

	public String getOutput() {
		return amount + ":" + base + ":" + getOutputCondition();
	}

	public String getOutputCondition() {
		return condition.toString();
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public void setBase(Integer base) {
		this.base = base;
	}

	public void setOutput(String output) {
		logger.debug("Parsing TxOutput... " + output);

		final var vals = output.split(":");
		amount = Integer.valueOf(vals[0]);
		base = Integer.valueOf(vals[1]);
		try {
			condition = OutCondition.parse(vals[2]).toString();
		} catch (final Exception e) {
			logger.error("Error parsing " + output, e);
		}
	}

	@Override
	public String toString() {
		return getOutput();
	}

}
