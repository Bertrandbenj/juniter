package juniter.service.bma.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WithDTO implements Serializable {

	private static final long serialVersionUID = -6551775706995959038L;
	Result result;

	public WithDTO() {

	}

	public WithDTO(List<Integer> blocks) {
		super();
		this.result = new Result(blocks);
	}

	/**
	 * @return the result
	 */
	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}


	@JsonIgnoreProperties(ignoreUnknown = true)
	public class Result implements Serializable {
		

		public Result() {
		}

		public Result(List<Integer> blocks2) {
			super();
			this.blocks = blocks2;
		}

		private static final long serialVersionUID = 8301420797082933436L;
		private List<Integer> blocks = new ArrayList<Integer>();

		public List<Integer> getBlocks() {
			return blocks;
		}

	}

}
