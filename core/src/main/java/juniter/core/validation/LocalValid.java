package juniter.core.validation;

import juniter.core.crypto.Crypto;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 *
 * @author BnimajneB
 *
 */
public interface LocalValid {

	Pattern CCY_PATTERN = Pattern.compile("[A-Za-z0-9]{2,50}");
	Pattern UID_PATTERN = Pattern.compile("^.{2,100}$");
	Pattern INT_PATTERN = Pattern.compile("[0-9]{1,19}");
	Pattern HASH_PATTERN = Pattern.compile("[A-Z0-9]{64}");
	Pattern PBKY_PATTERN = Pattern.compile("[123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]{44,45}");
	Pattern SIGN_PATTERN = Pattern.compile("^.{86}==$");

	default void assertBlockHash(String signPartSigned, String expectedHash) {
		assert Crypto.hash(signPartSigned).equals(expectedHash) : //
			"BR_L01 - Block Hash - \n  signing: " + signPartSigned + "\n  hash  : " + expectedHash;

	}

	default void assertSignature(String rawMsg, String rawSig, String rawPub) {
		assert Crypto.verify(rawMsg, rawSig, rawPub) : //
			"test Valid Block Signature " +
			"\n for : " + rawPub +
			"\n signed : " + rawSig +
			"\n on part :\n" + rawMsg;
	}

	default boolean isG1(String currency) {
		return "g1".equals(currency);
	}

    default boolean isV10(Object x) {
		if (x == null)
			return false;
		if (x instanceof String)
			return Integer.valueOf((String)x) >= 10;
		if (x instanceof Integer)
			return (Integer) x >= 10;
		if (x instanceof Short)
			return (Short) x >= 10;
		return false;
	}

	default boolean matchCurrency(String currency) {
		return CCY_PATTERN.matcher(currency).matches();
	}

	default boolean matchHash(String hash) {
		return true; //HASH_PATTERN.matcher(hash).matches();
	}

	default boolean matchInt(String _int) {
		return INT_PATTERN.matcher(_int).matches();
	}

	default boolean matchPubkey(String pubkey) {
		return true; // PBKY_PATTERN.matcher(pubkey).matches();
	}

	default boolean matchSignature(String sign) {
		return SIGN_PATTERN.matcher(sign).matches();
	}

	default boolean matchUserIdentifier(String hash) {
		return UID_PATTERN.matcher(hash).matches();
	}

	default Stream<String> permutation(String str) {

		final char[] charset = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz+-=/ ".toCharArray();

		final var res = new ArrayList<String>();

		for (int i = 0; i < str.length(); i++) {

			final var before = str.substring(0, i);
			final var after = str.substring(i + 1);

			for (int j = 0; j < charset.length; j++) {

				res.add(before + charset[j] + after);
			}

		}

		return res.stream();
	}

	default boolean verifySignature(String unSignedDoc, String signature, String pk) {
		return Crypto.verify(unSignedDoc, signature, pk);
	}



}
