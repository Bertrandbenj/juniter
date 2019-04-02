package juniter.core;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import juniter.core.model.dbo.DBBlock;
import juniter.service.Index;
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

	private List<DBBlock> blockchain;
	private List<DBBlock> blockchaing1;

	@Before
	public void init() {

		final ClassLoader cl = this.getClass().getClassLoader();

		final ObjectMapper jsonReader = new ObjectMapper();

		//		jsonReader.configure(Feature.AUTO_CLOSE_SOURCE, true);

		try {
			blockchain = jsonReader.readValue(cl.getResourceAsStream("blocks/blockchain.json"),
					new TypeReference<List<DBBlock>>() {
			});

			blockchaing1 = jsonReader.copy().readValue(cl.getResourceAsStream("blocks/g1_0_99.json"),
					new TypeReference<List<DBBlock>>() {
			});


		} catch (final Exception e) {
			LOG.error("Error parsing " + this.getClass().getName(), e);
		}
	}

	@Test
	public void testIndexingDuniterTest() {

		assertTrue("blockchain not parsed " + blockchain.size(), blockchain.size() == 12);

		for (final DBBlock b : blockchain) {
			assertTrue("NOT Valid \n" + b.toDUP(), idx_duniter.completeGlobalScope(b, true));
		}

	}

	@Test
	public void testIndexingG1() {

		assertTrue("blockchain not parsed " + blockchaing1.size(), blockchaing1.size() == 100);

		for (final DBBlock b : blockchaing1) {
			assertTrue("NOT Valid \n" + b.toDUP(), idx_g1.completeGlobalScope(b, true));
		}


	}


}
