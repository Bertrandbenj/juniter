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
		return function;
	}

	private Integer base;
	
	private String function; 
	
	public TxOutput() {
	}
	
	public TxOutput(String output) {
		setOutput(output);
	}
	
	public String getOutput(){
		return amount +":"+base+":"+function;
	}
	
	public void setOutput(String output) {
		logger.debug("Parsing TxOutput... "+output);

		var vals = output.split(":");
		amount = Integer.valueOf(vals[0]);
		base = Integer.valueOf(vals[1]);
		function = vals[2];
	}
	
}
