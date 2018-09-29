package juniter;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import juniter.crypto.CryptoUtils;
import juniter.crypto.SecretBox;

public class CryptoTest {

	private static final Logger LOG = LogManager.getLogger();

	final SecretBox secretBox = new SecretBox("salt", "password");

	@Test
	public void test() {
		fail("Not yet implemented");
	}

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
	public void testDoc2() {

		final String signature = "J3G9oM5AKYZNLAB5Wx499w61NuUoS57JVccTShUbGpCMjCqj9yXXqNq7dyZpDWA6BxipsiaMZhujMeBfCznzyci";
		final String unsignedDoc = "Version: 10\n" //
				+ "Type: Identity\n" //
				+ "Currency: beta_brousouf\n" //
				+ "Issuer: HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd\n" //
				+ "UniqueID: lolcat\n" //
				+ "Timestamp: 32-DB30D958EE5CB75186972286ED3F4686B8A1C2CD"
		// + signature
		;

//		final String unsignedInlined = "4tsFXtKofD6jK8zwLymgXWAoMBzhFx7KNANhtK86mKzA:L4AiZBYrmriQT3+r/WIpxobEnX2pdPgDg7Cf50vJ74c0GsL1CaEfJ3JmbqwARvzNkiewB3GR77gxFjZDIM0XAg==:1184-00000C17FA48A4681377DFA9BF1CE747CE82B869E694EC9E41F9F530C45E8F19:AlainLebrun";
//		final var sign = secretBox.sign(unsignedDoc);

		LOG.info("en theorie : " + signature + "\ngot ");

		assertTrue(CryptoUtils.verify(unsignedDoc, signature, "HgTTJLAQ5sqfknMq7yLPZbehtuLSsKj9CxWN7k8QvYJd"));

	}

	@Test
	public void testSignature() {

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
