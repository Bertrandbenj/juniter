package juniter.core.model.tx;

import juniter.core.model.DUPComponent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;

import javax.persistence.Embeddable;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class TxUnlock implements Serializable, Comparable<TxUnlock>, DUPComponent {



	private static final long serialVersionUID = -2759081749575814229L;

	private static final Logger LOG = LogManager.getLogger();

	private Integer inputRef;

	private UnlockFct fct;

	@Size(max = 255)
	private String fctParam;


	public TxUnlock(String unlock) {
		try{
			final var vals = unlock.split(":");
			inputRef = Integer.valueOf(vals[0]);
			final var function = vals[1];
			fct = UnlockFct.valueOf(function.substring(0, 3));
			fctParam = function.substring(4, function.length() - 1);
		}catch(Exception e){
			LOG.error("parsing TxUnlock "+ unlock, e);
		}

	}

	@Override
	public int compareTo(@NonNull TxUnlock o) {
		return toDUP().compareTo(o.toDUP());
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

	@Override
	public String toDUP() {
		return inputRef + ":" + getFunction();
	}

	@Override
	public String toString() {
		return toDUP();
	}


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
}
