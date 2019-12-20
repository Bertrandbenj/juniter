package juniter.core.model.wso;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.dbo.DBBlock;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlockDoc implements Doc{
	private Integer name = 5 ;
	private String block;

	@Override
	public void setData(String data) {
		block=data;
	}
}
