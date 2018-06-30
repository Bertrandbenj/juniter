package juniter.model.net.wrappers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WithWrapper implements Serializable {
	public WithWrapper(List<Integer> blocks) {
		super();
		this.result = new Result(blocks);
	}

	Result result ;

	
	private static final long serialVersionUID = -6551775706995959038L;

	@JsonIgnoreProperties(ignoreUnknown = true)
	public class Result implements Serializable{
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

	/**
	 * @return the result
	 */
	public Result getResult() {
		return result;
	}
}
