package juniter.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import utils.Constants;

@Entity
@Table(name = "peer", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Peer  implements Serializable{

	private static final long serialVersionUID = -4439148624925326592L;

//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Long id;

	private Short version; 
	
	private String currency; 
	
	private String status; 
	
	private String first_down; 
	
	private String last_try; 
	
	/**
	 * ex : HnFcSms8jzwngtVomTTnzudZx7SHUQY8sVE1y8yBmULk
	 */
//	@OneToOne(cascade = { CascadeType.ALL })
//	@JoinColumn(name = "pubkey", referencedColumnName= "pubkey")
	@Id
	@Pattern(regexp = Constants.Regex.PUBKEY) @Size(max=45)
	private String pubkey; 
	
	/**
	 * ex : 45180-00000E577DD4B308B98D0ED3E43926CE4D22E9A8
	 */
	//@OneToOne(cascade = CascadeType.DETACH)
	//@Pattern(regexp = Constants.Regex.BUID) 
	private String block;
	
	/**
	 * ex : GKTrlUc4um9lQuj9UI8fyA/n/JKieYqBYcl9keIWfAVOnvHamLHaqGzijsdX1kNt64cadcle/zkd7xOgMTdQAQ==
	 */
	@Pattern(regexp = Constants.Regex.SIGNATURE) @Size(max=88)
	private String signature; 
	
	/**
	 * ex : BASIC_MERKLED_API metab.ucoin.io 88.174.120.187 9201
	 */
	@OneToMany(mappedBy="peer")
	private List<EndPoint> endpoints = new ArrayList<EndPoint>(); 
	
	
	public Short getVersion() {
		return version;
	}
	public String getCurrency() {
		return currency;
	}
	public String getStatus() {
		return status;
	}
	public String getFirst_down() {
		return first_down;
	}
	public String getLast_try() {
		return last_try;
	}
	public String getPubkey() {
		return pubkey;
	}
	public String getBlock() {
		return block;//.getBuid();
	}
	public String getSignature() {
		return signature;
	}
	
	public List<EndPoint> endpoints() {
		return endpoints;
	}

	public List<String> getEndpoints() {
		return endpoints.stream().map(ep->ep.getEndpoint()).collect(Collectors.toList());
	}


	public static Predicate<Peer> blockHigherThan (int x){
		return p-> Integer.parseInt(p.block.split("-")[0]) > x;
	}
	public void setVersion(Short version) {
		this.version = version;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public void setFirst_down(String first_down) {
		this.first_down = first_down;
	}
	public void setLast_try(String last_try) {
		this.last_try = last_try;
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
	public Peer putEndpoints(List<EndPoint> endpoints) {
		this.endpoints.removeIf(x->true);
		this.endpoints.addAll(endpoints);
		return this;
	}
	

}
