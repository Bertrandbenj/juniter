package juniter.core.model.wso;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MemDoc implements Doc{
	private Integer name = 2 ;
	private String membership;

	@Override
	public void setData(String data) {
		membership =  data;
	}
}
