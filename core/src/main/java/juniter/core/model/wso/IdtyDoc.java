package juniter.core.model.wso;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdtyDoc implements Doc{
	private Integer name = 4 ;
	private String identity;

	@Override
	public void setData(String data) {
		identity = data;
	}
}
