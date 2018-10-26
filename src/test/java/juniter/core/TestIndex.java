package juniter.core;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import juniter.core.model.Block;
import juniter.repository.memory.Index;

public class TestIndex {

	private static final Logger LOG = LogManager.getLogger();
	Index idx = new Index();

	List<Block> blockchain;

	@Before
	public void before() throws Exception {
		final ClassLoader cl = this.getClass().getClassLoader();
		final ObjectMapper jsonMapper = new ObjectMapper();

		blockchain = jsonMapper.readValue(cl.getResourceAsStream("blockchain.json"),
				new TypeReference<List<Block>>() {
		});
	}

	@Test
	public void test() {

		assertTrue("blockchain not parsed " + blockchain.size(), blockchain.size() == 12);

		for (final Block b : blockchain) {
			assertTrue("NOT Valid \n" + b.toDUP(), idx.validate(b));
		}


	}


}
