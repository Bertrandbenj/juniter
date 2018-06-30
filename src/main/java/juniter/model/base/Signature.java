package juniter.model.base;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import juniter.utils.Constants;

@Embeddable
public class Signature implements Serializable {

	private static final long serialVersionUID = -2140293433532805888L;
	
	@Pattern(regexp = Constants.Regex.SIGNATURE)
	@Size(max = 88) 
	private String signature;

	public Signature() { }

	public Signature(String signature) {
		setSignature(signature);
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	@Override
	public String toString() {
		return getSignature();
	}
	
	public String toRaw() {
		return getSignature();
	}

}
