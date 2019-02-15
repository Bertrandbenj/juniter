package juniter.core.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.business.net.Peer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PeersDTO implements Serializable{
	
	private static final long serialVersionUID = 1618741167475514278L;


	private List<Peer> peers;
	
	public transient String url;
	public transient long timeMillis;

	
	public PeersDTO(List<Peer> peers) {
		super();
		this.peers = peers;
	}

}