package juniter.core.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PeersDTO  {

	private List<PeerDTO> peers;


	public PeersDTO(List<PeerDTO> peers) {
		super();
		this.peers = peers;
	}

}