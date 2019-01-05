package juniter.core.model.tx;

import juniter.core.model.DUPComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class TxUnlock implements Serializable, Comparable<TxUnlock>, DUPComponent {

	public enum UnlockFct {
		SIG("SIG"), XHX("XHX");

		private final String FCT_UNLOCK;

		UnlockFct(String unlock) {
			FCT_UNLOCK = unlock;
		}

		@Override
		public String toString() {
			return FCT_UNLOCK;
		}

	}

	private static final long serialVersionUID = -2759081749575814229L;

	private static final Logger LOG = LogManager.getLogger();

	private Integer inputRef;

	private UnlockFct fct;

	private String fctParam;

	public TxUnlock() {
	}

	public TxUnlock(String unlock) {
		final var vals = unlock.split(":");
		inputRef = Integer.valueOf(vals[0]);
		final var function = vals[1];
		setFct(UnlockFct.valueOf(function.substring(0, 3)));
		setFctParam(function.substring(4, function.length() - 1));
	}

	@Override
	public int compareTo(TxUnlock o) {
		// TODO Auto-generated method stub
		return (getInputRef() + getFunction()).compareTo(o.getInputRef() + o.getFunction());
	}

	public UnlockFct getFct() {
		return fct;
	}

	public String getFctParam() {
		return fctParam;
	}

	public String getFunction() {
		return fct.toString() + "(" + fctParam + ")";
	}

	public Integer getInputRef() {
		return inputRef;
	}

	public void setFct(UnlockFct fct) {
		this.fct = fct;
	}

	public void setFctParam(String fctParam) {
		this.fctParam = fctParam;
	}

	public void setInputRef(Integer id) {
		inputRef = id;
	}



	@Override
	public String toDUP() {
		return inputRef + ":" + getFunction();

	}

	@Override
	public String toString() {
		return toDUP();
	}

}
