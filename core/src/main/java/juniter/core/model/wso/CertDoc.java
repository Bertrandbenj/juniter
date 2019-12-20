package juniter.core.model.wso;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CertDoc implements Doc{
	private Integer name = 2 ;
	private String certification;

	@Override
	public void setData(String data) {
		certification =  data;
	}
}
