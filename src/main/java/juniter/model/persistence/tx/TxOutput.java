package juniter.model.persistence.tx;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Embeddable
public class TxOutput implements Serializable {

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
	 * @author ben
	 *
	 */
	public enum OutputFct {
	SIG("SIG"), XHX("XHX"), CLTV("CLTV"), CSV("CSV");

		private final String FCT_TYPE;

		OutputFct(String output) {
			FCT_TYPE = output;
		}

		@Override
		public String toString() {
			return FCT_TYPE;
		}

	}

	private static final long serialVersionUID = 2208036347838232516L;

	private static final Logger logger = LogManager.getLogger();
	private Integer base;
	private Integer amount;
	private OutputFct fct;
	private String fctParam;

	public TxOutput() {
	}

	public TxOutput(String output) {
		setOutput(output);
	}

	public Integer getAmount() {
		return amount;
	}

	public Integer getBase() {
		return base;
	}

	public OutputFct getFct() {
		return fct;
	}

	public String getFctParam() {
		return fctParam;
	}

	public String getOutput() {
		return amount + ":" + base + ":" + getOutputCondition();
	}

	public String getOutputCondition() {
		return fct + "(" + fctParam + ")";
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public void setBase(Integer base) {
		this.base = base;
	}

	public void setFct(OutputFct fct) {
		this.fct = fct;
	}

	public void setFctParam(String fctParam) {
		this.fctParam = fctParam;
	}

	public void setOutput(String output) {
		logger.info("Parsing TxOutput... " + output);

		final var vals = output.split(":");
		amount = Integer.valueOf(vals[0]);
		base = Integer.valueOf(vals[1]);
		final var outputCondition = vals[2];
		fct = OutputFct.valueOf(outputCondition.substring(0, 3));
		fctParam = outputCondition.substring(4, outputCondition.length() - 1);
	}

	@Override
	public String toString() {
		return getOutput();
	}

}
