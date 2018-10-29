package juniter.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import juniter.core.crypto.Crypto;
import juniter.core.model.Block;
import juniter.core.validation.LocalValid;
import juniter.repository.memory.Index;

public class TestOnFileBlocks implements LocalValid {

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

	public Block _15144;

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

	public List<Block> blockchaing1;

	final Index idx = new Index();



	@Before
	public void init() {
		LOG.info("Entering FileBlocksService.init  ");

		final ClassLoader cl = this.getClass().getClassLoader();
		final ObjectMapper jsonMapper = new ObjectMapper();

		try {
			_0 = jsonMapper.readValue(cl.getResourceAsStream("blocks/0.json"), Block.class);
			_1 = jsonMapper.readValue(cl.getResourceAsStream("blocks/1.json"), Block.class);
			_1437 = jsonMapper.readValue(cl.getResourceAsStream("blocks/1437.json"), Block.class);
			_15144 = jsonMapper.readValue(cl.getResourceAsStream("blocks/15144.json"), Block.class);
			_17500 = jsonMapper.readValue(cl.getResourceAsStream("blocks/17500.json"), Block.class);
			_33396 = jsonMapper.readValue(cl.getResourceAsStream("blocks/33396.json"), Block.class);
			_102093 = jsonMapper.readValue(cl.getResourceAsStream("blocks/102093.json"), Block.class);
			_127128 = jsonMapper.readValue(cl.getResourceAsStream("blocks/127128.json"), Block.class);

			blockchain = jsonMapper.readValue(cl.getResourceAsStream("blocks/blockchain.json"),
					new TypeReference<List<Block>>() {
			});

			blockchaing1 = jsonMapper.readValue(cl.getResourceAsStream("blocks/g1_0_99.json"),
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
	public void test105622() {
		//hash
	}

	@Test
	public void test15143() {
		final String signedPart = "InnerHash: 13A0C5A47340BB4D16288A94C77DAD6C7CAB7945625675D91F454AB50A7EDC1C\nNonce: 10100000003349\n";
		final String sign = "HWKzk9jPTTr2WhVyZQ51ugrpuAnFKczDhuqvTpi1PJMAyNldYOom06Vsp1F9j9Y+LHAL5ID1WjeR1mG5oNvGBg==";
		final String iss = "5WD4WSHE96ySreSwQFXPqaKaKcwboRNApiPHjPWB6V9C";
		assert Crypto.verify(signedPart, sign, iss);
	}

	@Test
	public void test15144() {
		final String signedPart = "InnerHash: 8B194B5C38CF0A38D16256405AC3E5FA5C2ABD26BE4DCC0C7ED5CC9824E6155B\nNonce: 30400000119992\n";
		final String sign = "fJusVDRJA8akPse/sv4uK8ekUuvTGj1OoKYVdMQQAACs7OawDfpsV6cEMPcXxrQTCTRMrTN/rRrl20hN5zC9DQ==";
		final String iss = "D9D2zaJoWYWveii1JRYLVK3J4Z7ZH3QczoKrnQeiM6mx";
		assert Crypto.verify(signedPart, sign, iss);
	}

	@Test
	public void test31202() {
		final String signedPart = "InnerHash: 931C4F2A922612AC9B541B3F054DDE4E54C9F4F2CA9A2A5D2EDB7396C54B3DC1\nNonce: 10100000254929\n";
		final String sign = "sOfgXH3875Vw137qs3wJ2TfjsfK5PH/ZweWq7sItAIB0D7RKkbfHcfi7OxlqCviy+8r384Ng06p/uVlxFnpLBQ==";
		final String iss = "t5RR5eVeE7jRhKcREvC3kfGtDTdkxmvW6WeJ9q9keHG";
		assertTrue("", Crypto.verify(signedPart, sign, iss));
	}

	@Test
	public void test85448() {
		final String signedPart = "InnerHash: FF0B7558ACC91F77BB6AF3F1DADCD3154E4A683346496C2B564E1BFBD1766EF7\nNonce: 10200000001484\n";
		final String sign = "/pq2tcuTYeHfPGQfkq58Q6M4CLSyVqSEUFv4Q3dPGzAKiJn8cB34jpcVR9Ar03qYfUxzqaJ6ZnDXK2B4LWalDg==";
		final String iss = "4GX5gUFwKg8Y8oL5ZFwFD64U3vEJo6CtY61Y3J8LMCHk";
		assert Crypto.verify(signedPart, sign, iss);
	}

	@Test
	public void testBlock0() {
		assertBlock(_0);

	}

	@Test
	public void testBlock1() {
		assertBlock(_1);
	}

	@Test
	public void testBlock102093() {
		assertBlock(_102093);
	}

	@Test
	public void testBlock127128() {
		assertBlock(_127128);
	}

	@Test
	public void testBlock1437() {
		assertBlock(_1437);
	}

	@Test
	public void testBlock15144() {
		assertBlock(_15144);
	}

	@Test
	public void testBlock17500() {
		assertBlock(_1437);
	}

	@Test
	public void testBlock33396() {
		assertBlock(_33396);
	}

	@Test
	public void testBlockSign1() {
		assertValidBlockSignature(_1);
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
	public void testG1BlockChain() {
		assertTrue("parsed blocks/blockchain.json size is 100 - " + blockchaing1.size(), //
				blockchaing1.size() - 100 == 0);

		blockchaing1.forEach(b -> {
			LOG.info("asserting " + b);
			assertBlock(b);
		});

	}

	@Test
	public void testIndexing() {
		idx.validate(_0);
		idx.validate(_1);
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