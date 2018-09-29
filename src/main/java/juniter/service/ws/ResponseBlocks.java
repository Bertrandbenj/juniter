package juniter.service.ws;

import java.io.Serializable;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import juniter.model.persistence.Block;

@JsonIgnoreProperties(ignoreUnknown = true)

public class ResponseBlocks implements Serializable {

	private static final long serialVersionUID = 2497514270739293189L;
	private static final Logger LOG = LogManager.getLogger();

	String resId;

	List<Block> body;

	public List<Block> getBody() {
		return body;
	}

	public String getResId() {
		return resId;
	}

}
