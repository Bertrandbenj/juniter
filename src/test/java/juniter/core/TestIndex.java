package juniter.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import juniter.core.model.Block;
import juniter.repository.jpa.index.Index;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class TestIndex {
	private static final Logger LOG = LogManager.getLogger();


	private Index idx_duniter = new Index();
	private Index idx_g1 = new Index();

	private List<Block> blockchain;
	private List<Block> blockchaing1;

	@Before
	public void init() {

		final ClassLoader cl = this.getClass().getClassLoader();

		final ObjectMapper jsonReader = new ObjectMapper();

		//		jsonReader.configure(Feature.AUTO_CLOSE_SOURCE, true);

		try {
			blockchain = jsonReader.readValue(cl.getResourceAsStream("blocks/blockchain.json"),
					new TypeReference<List<Block>>() {
			});

			blockchaing1 = jsonReader.copy().readValue(cl.getResourceAsStream("blocks/g1_0_99.json"),
					new TypeReference<List<Block>>() {
			});


		} catch (final Exception e) {
			LOG.error("Error parsing " + this.getClass().getName(), e);
		}
	}

	@Test
	public void testIndexingDuniterTest() {

		assertTrue("blockchain not parsed " + blockchain.size(), blockchain.size() == 12);

		for (final Block b : blockchain) {
			assertTrue("NOT Valid \n" + b.toDUP(), idx_duniter.validate(b, false));
		}

	}

	@Test
	public void testIndexingG1() {

		assertTrue("blockchain not parsed " + blockchaing1.size(), blockchaing1.size() == 100);

		for (final Block b : blockchaing1) {
			assertTrue("NOT Valid \n" + b.toDUP(), idx_g1.validate(b, false));
		}


	}


}
