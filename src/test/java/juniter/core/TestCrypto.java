package juniter.core;

import static org.junit.Assert.assertTrue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import juniter.core.crypto.CryptoUtils;
import juniter.core.crypto.SecretBox;

public class TestCrypto {

	private static final Logger LOG = LogManager.getLogger();



	@Test
	public void testDoc() {

		final String signature = "L4AiZBYrmriQT3+r/WIpxobEnX2pdPgDg7Cf50vJ74c0GsL1CaEfJ3JmbqwARvzNkiewB3GR77gxFjZDIM0XAg==";
		final String unsignedDoc = "Version: 10\n" //
				+ "Type: Identity\n" //
				+ "Currency: g1\n" //
				+ "Issuer: 4tsFXtKofD6jK8zwLymgXWAoMBzhFx7KNANhtK86mKzA\n" //
				+ "UniqueID: AlainLebrun\n" //
				+ "Timestamp: 1184-00000C17FA48A4681377DFA9BF1CE747CE82B869E694EC9E41F9F530C45E8F19\n"
				// + signature
				;

		//		final String unsignedInlined = "4tsFXtKofD6jK8zwLymgXWAoMBzhFx7KNANhtK86mKzA:L4AiZBYrmriQT3+r/WIpxobEnX2pdPgDg7Cf50vJ74c0GsL1CaEfJ3JmbqwARvzNkiewB3GR77gxFjZDIM0XAg==:1184-00000C17FA48A4681377DFA9BF1CE747CE82B869E694EC9E41F9F530C45E8F19:AlainLebrun";
		//		final var sign = secretBox.sign(unsignedDoc);

		LOG.info("en theorie : " + signature + "\ngot ");

		assertTrue(CryptoUtils.verify(unsignedDoc, signature, "4tsFXtKofD6jK8zwLymgXWAoMBzhFx7KNANhtK86mKzA"));

	}

	@Test
	public void testSignature() {

		final SecretBox secretBox = new SecretBox("salt", "password");
		final var test = "testSignature";


		final var pubkey = secretBox.getPublicKey();
		assertTrue("3LJRrLQCio4GL7Xd48ydnYuuaeWAgqX4qXYFbXDTJpAa".equals(pubkey));

		final var sign = secretBox.sign(test);
		assertTrue("i1KoUGRU/AxpE4FmdZuFjBuzOv4wD8Nbj7+aeaSjC2R10FaFH15vWBLPcq+ghDHthMg40xJ+OKYzIAPG1l3/BQ=="
				.equals(sign));

		assertTrue(CryptoUtils.verify(test, sign, pubkey));

		assertTrue(CryptoUtils.verify("testSignature",
				"i1KoUGRU/AxpE4FmdZuFjBuzOv4wD8Nbj7+aeaSjC2R10FaFH15vWBLPcq+ghDHthMg40xJ+OKYzIAPG1l3/BQ==",
				"3LJRrLQCio4GL7Xd48ydnYuuaeWAgqX4qXYFbXDTJpAa"));
		LOG.info("pubkey :" + pubkey + "\nSignature: " + sign);

	}

}
