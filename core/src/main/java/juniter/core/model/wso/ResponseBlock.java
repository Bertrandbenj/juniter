package juniter.core.model.wso;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.DBBlock;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseBlock  {

	private String resId;

	private DBBlock body;

}
