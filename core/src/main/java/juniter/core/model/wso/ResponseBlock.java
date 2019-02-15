package juniter.core.model.wso;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.DBBlock;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseBlock implements Serializable {

	private static final long serialVersionUID = 2497514270739293189L;

	private String resId;

	private DBBlock body;

}
