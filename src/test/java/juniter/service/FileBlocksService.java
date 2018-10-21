package juniter.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import juniter.core.crypto.CryptoUtils;
import juniter.core.model.Block;

//@RunWith(SpringRunner.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FileBlocksService {

	private static final Logger LOG = LogManager.getLogger();

	/**
	 * joiners/identity/certification & parameters
	 */
	public Block _0;

	/**
	 *
	 */
	public Block _1437;

	/**
	 * transactions
	 */
	public Block _127128;

	/**
	 * leavers
	 */
	public Block _102093;

	/**
	 * actives
	 */
	public Block _17500;

	/**
	 * revoked/excluded
	 */
	public Block _33396;

	public List<Block> blockchain;


	@Before
	public void init() throws IOException {
		LOG.info("Entering FileBlocksService.init  ");

		final ClassLoader cl = this.getClass().getClassLoader();
		final ObjectMapper jsonMapper = new ObjectMapper();

		try {
			_0 = jsonMapper.readValue(cl.getResourceAsStream("blocks/0.json"), Block.class);
			LOG.info("Sucessfully parsed " + _0 + "\tfrom" + cl.getResource("blocks/0.json"));
			assertThat("Parsing _0", _0.getNumber(), equalTo(0));

			_1437 = jsonMapper.readValue(cl.getResourceAsStream("blocks/1437.json"), Block.class);
			LOG.info("Sucessfully parsed " + _1437 + "\tfrom " + cl.getResource("blocks/1437.json"));

			_127128 = jsonMapper.readValue(cl.getResourceAsStream("blocks/127128.json"), Block.class);
			LOG.info("Sucessfully parsed " + _127128 + "\tfrom " + cl.getResource("blocks/127128.json"));

			_102093 = jsonMapper.readValue(cl.getResourceAsStream("blocks/102093.json"), Block.class);
			LOG.info("Sucessfully parsed " + _102093 + "\tfrom " + cl.getResource("blocks/102093.json"));

			_17500 = jsonMapper.readValue(cl.getResourceAsStream("blocks/17500.json"), Block.class);
			LOG.info("Sucessfully parsed " + _17500 + "\tfrom " + cl.getResource("blocks/17500.json"));

			_33396 = jsonMapper.readValue(cl.getResourceAsStream("blocks/33396.json"), Block.class);
			LOG.info("Sucessfully parsed " + _33396 + "\tfrom " + cl.getResource("blocks/33396.json"));

			blockchain = jsonMapper.readValue(cl.getResourceAsStream("blocks/blockchain.json"),
					new TypeReference<List<Block>>() {
			});
			LOG.info("Sucessfully parsed " + blockchain + "\tfrom " + cl.getResource("blocks/blockchain.json"));
			//			log.info("Sucessfully parsed " + _33396.getHash());


		} catch (final Exception e) {
			LOG.error("Starting FileBlocksService ... " + e);
		}

		LOG.info("Finished Initializing " + this.getClass().getName());
	}

	@Test
	public void testBlockChain () {
		assertThat("parsed blocks/blockchain.json size is 12 - " + blockchain.size(), //
				blockchain.size(), equalTo(12 + 0));

	}

	@Test
	public void testBlockHash() {
		final var unHashedBlock = _1437.toDUP(false);
		LOG.info("testBlockHash " + _1437.getIssuer() + " " + _1437.getSignature() + "\n" + unHashedBlock);
		final var hash = CryptoUtils.hash(unHashedBlock);
		assertThat("testBlockHash " + hash + " " + _1437.getInner_hash(), hash, equalTo(_1437.getInner_hash()));

	}

	@Test
	public void testTxHash() {

		LOG.info("testTxHash " + _0.getIssuer() + " " + _0.getSignature());

		assertTrue(CryptoUtils.verify(_0.toDUP(), _0.getSignature().toString(), _0.getIssuer()));
	}

	@Test
	public void testValidBlockSignature() {

		LOG.info("testValidBlockSignature " + _0.getIssuer() + " " + _0.getSignature());

		assertTrue(CryptoUtils.verify(_0.toDUP(), _0.getSignature().toString(), _0.getIssuer()));
	}

	@Test
	public void testValidTxSignature() {

		assertTrue("Block _127128 has 9 transactions ", _127128.getTransactions().size() == 9);

		for (int i = 0; i < _127128.getTransactions().size(); i++) {
			final var tx = _127128.getTransactions().get(i);

			assertTrue("Signature isnt verified  " + tx.getSignatures().get(i).toString()
					+ "\n  for issuer : " + tx.getIssuers().get(i).toString()
					+ "\n  in transaction : " + tx.toDUP(),
					CryptoUtils.verify(
							tx.toDUP(),
							tx.getSignatures().get(i).toString(),
							tx.getIssuers().get(i).toString()
							));

		}




	}

}