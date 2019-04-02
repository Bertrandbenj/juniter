package juniter.core.model.wso;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.dbo.DBBlock;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseBlock  {

	private String resId;

	private DBBlock body;

}
