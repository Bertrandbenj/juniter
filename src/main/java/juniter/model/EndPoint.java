package juniter.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import juniter.model.base.EndPointType;
import juniter.utils.Constants;
import juniter.utils.Validator;

/**
 * ex : [ "BASIC_MERKLED_API metab.ucoin.io 88.174.120.187 9201" ]
 * 
 * @author ben
 *
 */
@Entity
@Table(name = "endpoints", schema = "public")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EndPoint implements Serializable {

	private static final long serialVersionUID = -165962007943111454L;
	private static final Logger logger = LogManager.getLogger();

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@ManyToOne(cascade = CascadeType.REMOVE, fetch=FetchType.EAGER)
	@JoinColumn(name = "peer", referencedColumnName = "pubkey")
	private Peer peer;
	
	EndPointType getApi() {
		return api;
	}

	@Enumerated(EnumType.STRING)
	@Column(length=32)
	private EndPointType api;


	public EndPointType api() {
		return api;
	}

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
	

	@Transient private List<String> values = new ArrayList<String>();

	private String endpoint;

	
	
	public String getIp4() {
		return ip4;
	}

	public void setIp4(String ip4) {
		this.ip4 = ip4;
	}

	public String getIp6() {
		return ip6;
	}

	public void setIp6(String ip6) {
		this.ip6 = ip6;
	}

	public String url() {
		
		String res = "";
		if("443".equals(port) && !port.startsWith("https://"))
			res+="https://";
		
		if(domain != null) {
			res += domain ;
		}else if(ip6 != null) {
			res += "["+ ip6 +"]";
		}else if(ip4 != null) {
			res += ip4;
		}
		
		if(!res.endsWith("/"))
			res+="/";
		logger.debug("url: " +res);
		return res;
	}
	
	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public EndPoint linkPeer(Peer peer) {
		this.peer = peer;
		return this;
	}

	public EndPoint() {

	}

	public EndPoint(String endPT) {
		this.setEndpoint(endPT);
		//logger.info("Endpoint parsed " + reconstitute());
	}

	public String reconstitute() {
		return  endpoint + " <===> " + api + " " + domain + " " + ip4 + " " + ip6 + " " + port;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endPT) {
		this.endpoint = endPT;
		var it = endPT.split(" ");
		setApi(EndPointType.valueOf(it[0]));
		for (int i = 1; i < it.length; i++) {
			
			String item = it[i];
			values.add(item);
			if (Validator.validatePortNumber(item)) {
				port = item; continue;
			}
			if (Validator.validateIP4Address(item)) {
				ip4 = item; continue;
			}
			if (Validator.validateIP6Address(item)) {
				ip6 = item; continue;
			}
			if (Validator.validateDomain(item) || Validator.validateDomain2(item)) {
				domain = item; continue;
			}
			if (Validator.validateWS2PSTH(item)) {
				tree = item; continue;
			}
			other = item;
		}

	}

	public void setApi(EndPointType api) {
		this.api = api;
	}

	public Peer getPeer() {
		return peer;
	}

	public void setPeer(Peer peer) {
		this.peer = peer;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getOther() {
		return other;
	}

	void setOther(String other) {
		this.other = other;
	}

	public String getTree() {
		return tree;
	}

	public void setTree(String tree) {
		this.tree = tree;
	}

//	switch (api) {
//	case BASIC_MERKLED_API:
//	case BMAS:
//	case BMA:
//	case WS2P:
//	case WS2PTOR:
//	case MONIT_API:
//	case BMATOR:
//	case WS2PS:
//	case ES_CORE_API:
//	case DEFAULT_ENDPOINT:
//	case ES_USER_API:
//	case ES_SUBSCRIPTION_API:
//	default:
//		
//		break;
//
//	}

}
