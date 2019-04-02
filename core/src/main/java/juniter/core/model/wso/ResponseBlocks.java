package juniter.core.model.wso;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.dbo.DBBlock;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseBlocks  {


	private String resId;

	private List<DBBlock> body;

}
