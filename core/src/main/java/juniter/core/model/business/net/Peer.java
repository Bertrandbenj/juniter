package juniter.core.model.business.net;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.utils.Constants;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = "peer", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Peer  implements Serializable{

	private static final long serialVersionUID = -4439148624925326592L;

//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Long id;

	private Integer version;

	@Size(max = 42)
	private String currency;

	@Size(max = 10)
	private String status; 

	@Size(max = 42)
	private String first_down;

	@Size(max = 42)
	private String last_try; 
	
	/**
	 * ex : HnFcSms8jzwngtVomTTnzudZx7SHUQY8sVE1y8yBmULk
	 */
//	@OneToOne(cascade = { CascadeType.ALL })
//	@JoinColumn(name = "pubkey", referencedColumnName= "pubkey")
	@Id
	@Pattern(regexp = Constants.Regex.PUBKEY)
	@Size(max=45)
	private String pubkey; 
	
	/**
	 * ex : 45180-00000E577DD4B308B98D0ED3E43926CE4D22E9A8
	 */
	//@OneToOne(cascade = CascadeType.DETACH)
	//@Pattern(regexp = Constants.Regex.BUID) 
	private String block;
	

	@Size(max=88)
	@Pattern(regexp = Constants.Regex.SIGNATURE)
	private String signature; 
	

	@OneToMany(mappedBy="peer")
	private List<EndPoint> endpoints = new ArrayList<>();
	
	
	public Integer getVersion() {
		return version;
	}
	public String getCurrency() {
		return currency;
	}
	public String getPubkey() {
		return pubkey;
	}
	public String getBlock() {
		return block;
	}
	public String getRaw(){
		return toDUP(true);
	}

	public List<EndPoint> endpoints() {
		return endpoints;
	}


	public void setVersion(Integer version) {
		this.version = version;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setPubkey(String pubkey) {
		this.pubkey = pubkey;
	}
	public void setBlock(String block) {
		this.block = block;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String toDUP(boolean signed ){
		return "Version: "+version+
				"\nType: Peer" +
				"\nCurrency: "+currency+"" +
				"\nPublicKey: "+pubkey+"" +
				"\nBlock: "+block+"" +
				"\nEndpoints:\n"
				+endpoints.stream().map(EndPoint::getEndpoint).collect(Collectors.joining("\n"))
				+"\n" +
				(signed? signature + "\n" : "");
	}
	

}
