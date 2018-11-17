package juniter.service.bma.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.net.Peer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PeeringDTO implements Serializable{

	private static final long serialVersionUID = 1618741167475514278L;
	private List<Peer> peers ;

	public transient String url;
	public transient long timeMillis;

	public PeeringDTO() {
		super();
		peers = new ArrayList<Peer>();
	}

	public PeeringDTO(List<Peer> peers) {
		super();
		this.peers = peers;
	}

	public List<Peer> getPeers() {
		return peers;
	}
}