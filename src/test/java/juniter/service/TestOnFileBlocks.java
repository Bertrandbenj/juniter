package juniter.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import juniter.core.model.tx.Transaction;

public class TestOnFileBlocks {

	private static final Logger LOG = LogManager.getLogger();

	/**
	 * joiners/identity/certification & parameters
	 */
	public Block _0;

	public Block _1;

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


	private void assertBlock(Block block ) {
		assertBlockInnerHash(block);
		assertBlockHash(block);
		assertValidBlockSignature(block);

		block.getTransactions().forEach(tx -> {
			assertValidTxSignatures(tx);
		});

	}

	private void assertBlockHash(Block block){
		final var hash = CryptoUtils.hash(block.signedPartSigned());

		assertTrue("test BlockHash " + block.signedPartSigned(), hash.equals(block.getHash()));

	}

	private void assertBlockInnerHash(Block block) {
		final var hash = CryptoUtils.hash(block.toDUP(false, false));

		assertTrue("assert BlockHash #" + block.getNumber() + " - " +
				"\niss      : " + block.getIssuer() +
				"\nsign     : " + block.getSignature() +
				"\nexpected : " + block.getInner_hash() +
				"\n but got : " + hash +
				"\n on " + block.toDUP(false, false), //
				hash.equals(block.getInner_hash()));
	}


	private void assertValidBlockSignature(Block block) {
		assertTrue("test Valid Block Signature for" + block.getIssuer() +
				"\nexpected : " + block.getSignature() +
				"\n on doc  : " + block.toDUP(true, true), //
				CryptoUtils.verify(block.signedPart(), block.getSignature().toString(), block.getIssuer()));
	}


	private void assertValidTxSignatures(Transaction tx) {


		for (int i = 0; i < tx.getSignatures().size(); i++) {
			final var sign = tx.getSignatures().get(i).toString();
			final var iss = tx.getIssuers().get(i).toString();

			assertTrue("Signature isnt verified  " + sign
					+ "\n  for issuer : " + iss
					+ "\n  in transaction : " + tx.toDUPdoc(false),
					CryptoUtils.verify(tx.toDUPdoc(false), sign, iss));

		}

	}

	@Before
	public void init() throws IOException {
		LOG.info("Entering FileBlocksService.init  ");

		final ClassLoader cl = this.getClass().getClassLoader();
		final ObjectMapper jsonMapper = new ObjectMapper();

		try {
			_0 = jsonMapper.readValue(cl.getResourceAsStream("blocks/0.json"), Block.class);
			_1 = jsonMapper.readValue(cl.getResourceAsStream("blocks/1.json"), Block.class);
			_1437 = jsonMapper.readValue(cl.getResourceAsStream("blocks/1437.json"), Block.class);
			_17500 = jsonMapper.readValue(cl.getResourceAsStream("blocks/17500.json"), Block.class);
			_33396 = jsonMapper.readValue(cl.getResourceAsStream("blocks/33396.json"), Block.class);
			_102093 = jsonMapper.readValue(cl.getResourceAsStream("blocks/102093.json"), Block.class);
			_127128 = jsonMapper.readValue(cl.getResourceAsStream("blocks/127128.json"), Block.class);

			blockchain = jsonMapper.readValue(cl.getResourceAsStream("blocks/blockchain.json"),
					new TypeReference<List<Block>>() {
			});
			LOG.info("Sucessfully parsed " + blockchain + "\tfrom " + cl.getResource("blocks/blockchain.json"));
			//			log.info("Sucessfully parsed " + _33396.getHash());


		} catch (final Exception e) {
			LOG.error("Error Initializing " + this.getClass().getName(), e);
		}

		LOG.info("Finished Initializing " + this.getClass().getName());
	}

	@Test
	public void testDuniterTestBlockChain () {
		assertTrue("parsed blocks/blockchain.json size is 11 - " + blockchain.size(), //
				blockchain.size() == 11);

		blockchain.forEach(b -> {
			assertBlock(b);
		});

	}

	@Test
	public void testBlockHash0() {
		assertBlock(_0);
	}

	@Test
	public void testBlockHash1() {
		assertBlock(_1);
	}

	@Test
	public void testBlockHash127128() {
		assertBlock(_127128);
	}

	@Test
	public void testBlockHash33396() {
		assertBlock(_33396);
	}

	@Test
	public void testBlockSign0() {
		assertValidBlockSignature(_0);
	}

	@Test
	public void testBlockSign1() {
		assertValidBlockSignature(_1);
	}

	@Test
	public void testLoad() {
		assertThat(_0).isNotNull();
		assertThat(_0.getJoiners().size()).isEqualTo(59);
		assertThat(_0.getNumber()).isEqualTo(0);

		assertThat(_127128).isNotNull();
		assertThat(_127128.getTransactions().size()).isEqualTo(9);
		assertThat(_127128.getNumber()).isEqualTo(127128);
	}



}