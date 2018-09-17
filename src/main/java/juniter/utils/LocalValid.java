package juniter.utils;

import java.util.regex.Pattern;

import juniter.crypto.CryptoUtils;

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

	default boolean isG1(String currency) {
		return "g1".equals(currency);
	};

	default boolean isV10(Object x) {
		if (x == null)
			return false;
		if (x instanceof String)
			return "10".equals(x);
		if (x instanceof Integer || x instanceof Short)
			return (Integer) x == 10;
		return false;
	}

	default boolean matchCurrency(String currency) {
		return CCY_PATTERN.matcher(currency).matches();
	}

	default boolean matchHash(String hash) {
		return HASH_PATTERN.matcher(hash).matches();
	};

	default boolean matchInt(String _int) {
		return INT_PATTERN.matcher(_int).matches();
	}

	default boolean matchPubkey(String pubkey) {
		return PBKY_PATTERN.matcher(pubkey).matches();
	}

	default boolean matchSignature(String sign) {
		return SIGN_PATTERN.matcher(sign).matches();
	}

	default boolean matchUserIdentifier(String hash) {
		return UID_PATTERN.matcher(hash).matches();
	}

	default boolean verifySignature(String unSignedDoc, String signature, String pk) {
		return CryptoUtils.verify(unSignedDoc, signature, pk);
	}
}
