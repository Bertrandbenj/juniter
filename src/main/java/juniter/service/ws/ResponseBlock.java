package juniter.service.ws;

import java.io.Serializable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import juniter.model.persistence.Block;

@JsonIgnoreProperties(ignoreUnknown = true)

public class ResponseBlock implements Serializable {

	private static final long serialVersionUID = 2497514270739293189L;
	private static final Logger LOG = LogManager.getLogger();

	String resId;

	Block body;

	public Object getBody() {
		return body;
	}

	public String getResId() {
		return resId;
	}

}
