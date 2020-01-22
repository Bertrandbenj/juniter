package juniter.core.model.dbo.tx;

import juniter.core.model.meta.DUPOutput;
import juniter.core.model.meta.DUPOutCondition;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.Embeddable;

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
@Data
@NoArgsConstructor
public class TxOutput implements DUPOutput {

	static final Logger LOG = LogManager.getLogger(TxOutput.class);

	private Integer base;

	private Integer amount;

	private String condition;

	TxOutput(String output) {
		final var vals = output.split(":");
		amount = Integer.valueOf(vals[0]);
		base = Integer.valueOf(vals[1]);
		try {
			condition = DUPOutCondition.parse(vals[2]).toDUP();
		} catch (final Exception e) {
			LOG.error("Error parsing " + output, e);
		}
	}

	public String getConditionString() {
		return condition;
	}


	@Override
	public String toString() {
		return toDUP();
	}




}
