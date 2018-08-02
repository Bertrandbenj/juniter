package juniter.model.persistence.tx;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Embeddable
public class TxOutput implements Serializable {

	private static final long serialVersionUID = 2208036347838232516L;

	private static final Logger logger = LogManager.getLogger();

	private Integer amount;

	public Integer Amount() {
		return amount;
	}

	public Integer Base() {
		return base;
	}

	public String Function() {
		return outputCondition;
	}

	private Integer base;

	private String outputCondition;

	public TxOutput() {
	}

	public TxOutput(String output) {
		setOutput(output);
	}

	public String getOutput() {
		return amount + ":" + base + ":" + outputCondition;
	}

	public void setOutput(String output) {
		logger.debug("Parsing TxOutput... " + output);

		var vals = output.split(":");
		amount = Integer.valueOf(vals[0]);
		base = Integer.valueOf(vals[1]);
		outputCondition = vals[2];
	}

	public int functionReference() {
		return Integer.parseInt(outputCondition.substring(4, outputCondition.length() - 1));
	}

	public String functionReferenceValue() {
		logger.info(outputCondition + " " + outputCondition.substring(4, outputCondition.length() - 1));
		return outputCondition.substring(4, outputCondition.length() - 1);
	}

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

		OutputFct(String transactionType) {
			this.FCT_TYPE = transactionType;
		}

		public String getEndPointType() {
			return this.FCT_TYPE;
		}

	}
}
