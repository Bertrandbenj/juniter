package juniter.model.tx;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Embeddable
public class TxUnlock implements Serializable {

	private static final long serialVersionUID = -2759081749575814229L;

	private static final Logger logger = LogManager.getLogger();
	
	private Integer id;

	private String function; 
	
	public TxUnlock() {
	}
	
	public TxUnlock(String unlock) {
		setUnlock(unlock);
	}
	
	public String getUnlock(){
		return id +":"+function;
	}
	
	public void setUnlock(String unlock) {
		
		logger.debug("Parsing TxUnlock... "+unlock);
		
		var vals = unlock.split(":");
		id = Integer.valueOf(vals[0]);
		function = vals[1];
	}
	
}
