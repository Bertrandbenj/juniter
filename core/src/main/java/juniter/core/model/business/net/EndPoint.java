package juniter.core.model.business.net;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.utils.Constants;
import juniter.core.validation.NetValid;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * ex : [ "BASIC_MERKLED_API metab.ucoin.io 88.174.120.187 9201" ]
 * 
 * @author ben
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "endpoints", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EndPoint   {


	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.REMOVE, fetch=FetchType.EAGER)
	@JoinColumn(name = "peer", referencedColumnName = "pubkey")
	private Peer peer;

	@Enumerated(EnumType.STRING)
	@Column(length=32)
	private EndPointType api;

	@Size(min=7, max=15)
	@Pattern(regexp=Constants.Regex.IP4)
	private String ip4;
	
	@Size(max=64)
	//@Pattern(regexp=Constants.Regex.IP6)
	private String ip6;

	@Size(max=6)
	@Pattern(regexp=Constants.Regex.PORT)
	private String port;

	private String domain;
	private String other;
	private String tree;
	private String endpoint;

	public EndPoint(String endPT) {
		this.endpoint = endPT;
		var it = endPT.split(" ");
		setApi(EndPointType.valueOf(it[0]));
		for (int i = 1; i < it.length; i++) {

			String item = it[i];
			if (NetValid.validatePortNumber(item)) {
				port = item; continue;
			}
			if (NetValid.validateIP4Address(item)) {
				ip4 = item; continue;
			}
			if (NetValid.validateIP6Address(item)) {
				ip6 = item; continue;
			}
			if (NetValid.validateDomain(item) || NetValid.validateDomain2(item)) {
				domain = item; continue;
			}
			if (NetValid.validateWS2PSTH(item)) {
				tree = item; continue;
			}
			other = item;
		}
	}

	public EndPointType api() {
		return api;
	}

	public String url() {
		
		String res = "";
		switch(api){
			case BMAS:
				res+="https://";
				break;
			case BMA:
				res+="http://";
				break;
			case WS2P:
				res+="wss://";
				break;

		}
		
		if(domain != null) {
			res += domain ;
		}else if(ip6 != null) {
			res += "["+ ip6 +"]";
		}else if(ip4 != null) {
			res += ip4;
		}

		res+= ":"+port;
		
		if(!res.endsWith("/"))
			res+="/";
		//LOG.debug("url: " +res);
		return res;
	}
	


	public EndPoint linkPeer(Peer peer) {
		this.peer = peer;
		return this;
	}





}
