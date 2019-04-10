package juniter.service.ws2p;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import juniter.core.model.wso.Body;
import lombok.Data;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Request {

	private static final Logger LOG = LogManager.getLogger(Request.class);

	private String reqId;

	private Body body;

	/**
	 * https://git.duniter.org/nodes/common/doc/blob/master/rfc/0004_ws2p_v1.md#getblock
	 *
	 * @param number
	 * @return
	 */
	Request getBlock(Integer number) {

		final var res = new Request();
		res.reqId = randomReqId();
		res.body = new Body("BLOCK_BY_NUMBER");
		res.body.getParams().put("number", number);

		return res;
	}

	/**
	 * https://git.duniter.org/nodes/common/doc/blob/master/rfc/0004_ws2p_v1.md#getblocks
	 *
	 * @return
	 */
	Request getBlocks(Integer count, Integer from) {

		final var res = new Request();
		res.reqId = randomReqId();
		res.body = new Body("BLOCKS_CHUNK");
		res.body.getParams().put("count", count);
		res.body.getParams().put("fromNumber", from);
		return res;
	}

	/**
	 * https://git.duniter.org/nodes/common/doc/blob/master/rfc/0004_ws2p_v1.md#getcurrent
	 *
	 * @return
	 */
	Request getCurrent() {

		final var res = new Request();
		res.reqId = randomReqId();
		res.body = new Body("CURRENT");

		return res;
	}

	/**
	 * https://git.duniter.org/nodes/common/doc/blob/master/rfc/0004_ws2p_v1.md#getrequirementspending
	 *
	 ** @return
	 */
	Request getRequirementsPending(Integer minCert) {

		final var res = new Request();
		res.reqId = randomReqId();
		res.body = new Body("WOT_REQUIREMENTS_OF_PENDING");
		res.body.getParams().put("minCert", minCert);
		return res;
	}

	String randomReqId() {
		final var rand = new Random();
		final char[] charset = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
				'f' };
		var res = "";

		for (int i = 0; i < 8; i++) {
			final int myRandomNumber = rand.nextInt(16); // Generates a random number between 0x10 and 0x20
			res += charset[myRandomNumber]; // Random hex number in result
		}
		LOG.info("randomReqId " + res);

		return res;
	}

}
