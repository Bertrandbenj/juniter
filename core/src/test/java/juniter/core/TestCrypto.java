package juniter.core;

import juniter.core.crypto.Crypto;
import juniter.core.crypto.SecretBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class TestCrypto {

	private static final Logger LOG = LogManager.getLogger(TestCrypto.class);


	@Test
	public void testHashFunction() {
		// TODO
	}


	@Test
	public void testSignatureFunction() {

		final SecretBox secretBox = new SecretBox("salt", "password");
		final var test = "testSignature";


		final var pubkey = secretBox.getPublicKey();
		assertTrue("3LJRrLQCio4GL7Xd48ydnYuuaeWAgqX4qXYFbXDTJpAa".equals(pubkey));

		final var sign = secretBox.sign(test);
		assertTrue("i1KoUGRU/AxpE4FmdZuFjBuzOv4wD8Nbj7+aeaSjC2R10FaFH15vWBLPcq+ghDHthMg40xJ+OKYzIAPG1l3/BQ=="
				.equals(sign));

		assertTrue(Crypto.verify(test, sign, pubkey));

		assertTrue(Crypto.verify("testSignature",
				"i1KoUGRU/AxpE4FmdZuFjBuzOv4wD8Nbj7+aeaSjC2R10FaFH15vWBLPcq+ghDHthMg40xJ+OKYzIAPG1l3/BQ==",
				"3LJRrLQCio4GL7Xd48ydnYuuaeWAgqX4qXYFbXDTJpAa"));
		LOG.info("pubkey :" + pubkey + "\nSignature: " + sign);

	}

}
