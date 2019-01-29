package juniter.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import juniter.core.crypto.Crypto;
import juniter.core.model.DBBlock;
import juniter.core.validation.BlockLocalValid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

public class TestOnFileBlocks implements BlockLocalValid {

	private static final Logger LOG = LogManager.getLogger();

	/**
	 * joiners/identity/certification & parameters
	 */
	public DBBlock _0;

	public DBBlock _1;

	/**
	 *
	 */
	public DBBlock _1437;

	public DBBlock _15144;

	/**
	 * transactions
	 */
	public DBBlock _127128;

	/**
	 * leavers
	 */
	public DBBlock _102093;

	/**
	 * actives
	 */
	public DBBlock _17500;

	/**
	 * revoked/excluded
	 */
	public DBBlock _33396;

	public List<DBBlock> blockchain;

	public List<DBBlock> blockchaing1;

	final Index idx = new Index();

	private DBBlock _92636;


	@Before
	public void init() {
		LOG.info("Entering FileBlocksService.reset  ");

		final ClassLoader cl = this.getClass().getClassLoader();
		final ObjectMapper jsonMapper = new ObjectMapper();

		try {
			_0 = jsonMapper.readValue(cl.getResourceAsStream("blocks/0.json"), DBBlock.class);
			_1 = jsonMapper.readValue(cl.getResourceAsStream("blocks/1.json"), DBBlock.class);
			_1437 = jsonMapper.readValue(cl.getResourceAsStream("blocks/1437.json"), DBBlock.class);
			_15144 = jsonMapper.readValue(cl.getResourceAsStream("blocks/15144.json"), DBBlock.class);
			_17500 = jsonMapper.readValue(cl.getResourceAsStream("blocks/17500.json"), DBBlock.class);
			_33396 = jsonMapper.readValue(cl.getResourceAsStream("blocks/33396.json"), DBBlock.class);
			_92636 = jsonMapper.readValue(cl.getResourceAsStream("blocks/92636.json"), DBBlock.class);
			_102093 = jsonMapper.readValue(cl.getResourceAsStream("blocks/102093.json"), DBBlock.class);
			_127128 = jsonMapper.readValue(cl.getResourceAsStream("blocks/127128.json"), DBBlock.class);

			blockchain = jsonMapper.readValue(cl.getResourceAsStream("blocks/blockchain.json"),
					new TypeReference<List<DBBlock>>() {
			});

			blockchaing1 = jsonMapper.readValue(cl.getResourceAsStream("blocks/g1_0_99.json"),
					new TypeReference<List<DBBlock>>() {
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
		assert Crypto.verify(signedPart, sign, iss): "Signature Block# 85448";
	}

	@Test
	public void test109327() {
		final String signedPart = "InnerHash: 845302B10649E0C9916AB6957CED582917AD9D46573033DF5C482E2F4950FF6E\nNonce: 770400000049856";
		final String sign = "77oCaC+v8fY45PjK8pMHf0u3/hsP/7I4RKvPFg8Ku1HEwwUl95531SDQmh2Q5KLaZjU7uBmS7qdiGVqYINSoAw==";
		final String iss = "GfKERHnJTYzKhKUma5h1uWhetbA8yHKymhVH2raf2aCP";
		assert Crypto.verify(signedPart, sign, iss) : "Signature Block# 109327";
	}

	@Test
	public void test87566() {
		final String signedPart = "InnerHash: 2C9AAC6290DC5CE5FCFA11B0A626179747E52F54512FFB75986BAC7423C1C4C8\nNonce: 10300000125264\n";
		final String sign = "1bn9PaiuWEhYRwEtdCthPtvxipNXuvUsh2c3iuv2PkZXIFyohx3U5+Fx+wUl60TjihPSGvAH3X/jw2NzHyUFDw==";
		final String iss = "2sZF6j2PkxBDNAqUde7Dgo5x3crkerZpQ4rBqqJGn8QT";
		assert Crypto.verify(signedPart, sign, iss): "Signature Block# 87566";
	}
	@Test
	public void test0() {
		final String signedPart = "InnerHash: 55FBB918864D56922100211DAEF9CB71AC412BE10CC16B3A7AE56E10FB3A7569\nNonce: 400000020525\n";
		final String sign = "49OD/8pj0bU0Lg6HB4p+5TOcRbgtj8Ubxmhen4IbOXM+g33V/I56GfF+QbD9U138Ek04E9o0lSjaDIVI/BrkCw==";
		final String iss = "2ny7YAdmzReQxAayyJZsyVYwYhVyax2thKcGknmQy5nQ";
		assert Crypto.verify(signedPart, sign, iss): "Signature Block# 0";
	}



	@Test
	public void test92636() {
		final var hash = Crypto.hash(_92636.toDUP(false, false));

		assertTrue("test 92636 " + hash + " \nexpected: " + _92636.getInner_hash(),
				hash.equals(_92636.getInner_hash()));
	}

	@Test
	public void testBlock0() {
		assertBlockLocalValid(_0, true);

	}

	@Test
	public void testBlock1() {
		assertBlockLocalValid(_1, true);
	}

	@Test
	public void testBlock102093() {
		assertBlockLocalValid(_102093, true);
	}

	@Test
	public void testBlock127128() {
		assertBlockLocalValid(_127128, true);
	}

	@Test
	public void testBlock1437() {
		assertBlockLocalValid(_1437, true);
	}

	@Test
	public void testBlock15144() {
		assertBlockLocalValid(_15144, true);
	}

	@Test
	public void testBlock17500() {
		assertBlockLocalValid(_1437, true);
	}

	@Test
	public void testBlock33396() {
		assertBlockLocalValid(_33396, true);
	}


	@Test
	public void testDuniterTestBlockChain () {
		assertTrue("parsed blocks/blockchain.json size is 11 - " + blockchain.size(), //
				blockchain.size() == 11);

		blockchain.forEach(b -> {
			assertBlockLocalValid(b, true);
		});

	}

	@Test
	public void testG1BlockChain() {
		assertTrue("parsed blocks/blockchain.json size is 100 - " + blockchaing1.size(), //
				blockchaing1.size() - 100 == 0);

		blockchaing1.forEach(b -> {
			LOG.info("asserting " + b);
			assertBlockLocalValid(b, true);
		});

	}

	@Test
	public void testIndexing() {
		idx.completeGlobalScope(_0, true);
		idx.completeGlobalScope(_1, true);
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