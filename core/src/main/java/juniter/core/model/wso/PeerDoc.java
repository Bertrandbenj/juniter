package juniter.core.model.wso;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PeerDoc implements Doc{
	private Integer name = 2 ;
	private String peer;

	@Override
	public void setData(String data) {
		peer =  data;
	}
}
