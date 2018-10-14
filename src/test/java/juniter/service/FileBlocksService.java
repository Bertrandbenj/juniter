package juniter.service;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

import juniter.model.persistence.Block;
import juniter.repository.BlockRepository;
import juniter.service.duniter.CryptoService;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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

	@Autowired
	BlockRepository blockRepo;

	@Autowired
	CryptoService cryptoService;

	@Before
	public void init() throws IOException {
		LOG.info("Entering FileBlocksService.init  ");

		final ClassLoader cl = this.getClass().getClassLoader();
		final ObjectMapper jsonMapper = new ObjectMapper();

		try {
			_0 = jsonMapper.readValue(cl.getResourceAsStream("blocks/0.json"), Block.class);
			LOG.info("Sucessfully parsed " + _0 + "\tfrom" + cl.getResource("blocks/0.json"));
			assertThat(_0.getNumber(), equalTo(0));

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
//			log.info("Sucessfully parsed " + _33396.getHash());
			try {
				blockRepo.findTop1ByNumber(17500).orElseGet(() -> blockRepo.save(_17500));
				blockRepo.findTop1ByNumber(33396).orElseGet(() -> blockRepo.save(_33396));
				blockRepo.findTop1ByNumber(127128).orElseGet(() -> blockRepo.save(_127128));
				blockRepo.findTop1ByNumber(102093).orElseGet(() -> blockRepo.save(_102093));
				blockRepo.findTop1ByNumber(0).orElseGet(() -> blockRepo.save(_0));
			} catch (final Exception e) {
				LOG.error("saving ", e);
			}

		} catch (final Exception e) {
			LOG.error("Starting FileBlocksService ... " + e);
		}

		LOG.info("Finished Initializing " + this.getClass().getName());
	}

	@Test
	public void testBlockHash() {
		final var unHashedBlock = _1437.toRaw(false);
		LOG.info("testBlockHash " + _1437.getIssuer() + " " + _1437.getSignature() + "\n" + unHashedBlock);
		final var hash = cryptoService.hash(unHashedBlock);
		assertThat(hash, equalTo(_1437.getInner_hash()));

	}

	@Test
	public void testTxHash() {

		LOG.info("testTxHash " + _0.getIssuer() + " " + _0.getSignature());

		assertTrue(cryptoService.verify(_0.toRaw(), _0.getSignature().toString(), _0.getIssuer()));
	}

	@Test
	public void testValidBlockSignature() {

		LOG.info("testValidBlockSignature " + _0.getIssuer() + " " + _0.getSignature());

		assertTrue(cryptoService.verify(_0.toRaw(), _0.getSignature().toString(), _0.getIssuer()));
	}

	@Test
	public void testValidTxSignature() {

		assertTrue(_127128.getTransactions().size() > 0);
		final var tx = _127128.getTransactions().get(0);

		LOG.info("testValidTxSignature " + tx.toRaw());

		assertTrue(cryptoService.verify( //
				tx.toRaw(), //
				tx.getSignatures().get(0).toString(), //
				tx.getIssuers().iterator().next().toString()));
	}

}