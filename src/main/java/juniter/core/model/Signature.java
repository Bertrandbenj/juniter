package juniter.core.model;

import juniter.core.utils.Constants;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class Signature implements Serializable, Comparable<Signature> {

	private static final long serialVersionUID = -2140293433532805888L;

	@Pattern(regexp = Constants.Regex.SIGNATURE)
	@Size(max = 88)
	private String signature;

	public Signature() {
	}

	public Signature(String signature) {
		setSignature(signature);
	}

	@Override
	public int compareTo(Signature o) {
		return signature.compareTo(o.signature);
	}

	@Override
	public boolean equals(Object o) {
		return o.toString().equals(signature);
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String toRaw() {
		return getSignature();
	}

	@Override
	public String toString() {
		return getSignature();
	}
}
