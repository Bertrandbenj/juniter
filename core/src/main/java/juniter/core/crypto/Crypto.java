package juniter.core.crypto;

import com.lambdaworks.codec.Base64;
import jnr.ffi.byref.LongLongByReference;
import org.abstractj.kalium.NaCl;
import org.abstractj.kalium.NaCl.Sodium;
import org.abstractj.kalium.crypto.Util;

import java.nio.charset.Charset;

import static org.abstractj.kalium.NaCl.sodium;

public class Crypto extends Util {

	public static class Tuple<A, B> {

		public final A a;
		public final B b;

		Tuple(A a, B b) {
			this.a = a;
			this.b = b;
		}

		public A getA() {
			return a;
		}

		public B getB() {
			return b;
		}

	}
	private static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

	private static final Charset CHARSET_ASCII = Charset.forName("US-ASCII");

	private static final int SIGNATURE_BYTES = 64;

	private final static char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

	private static final Sodium naCl = NaCl.sodium();

	//	private static byte[] copyEnsureLength(byte[] source, int length) {
	//		final byte[] result = zeros(length);
	//		if (source.length > length) {
	//			System.arraycopy(source, 0, result, 0, length);
	//		} else {
	//			System.arraycopy(source, 0, result, 0, source.length);
	//		}
	//		return result;
	//	}

	private static String bytesToHex(byte[] bytes) {
		final char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			final int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_CHARS[v >>> 4];
			hexChars[j * 2 + 1] = HEX_CHARS[v & 0x0F];
		}
		return new String(hexChars);
	}

	static byte[] decodeAscii(String string) {
		return string.getBytes(CHARSET_ASCII);
	}

	public static byte[] decodeBase58(String data) {
		try {
			return Base58.decode(data);
		} catch (final Exception e) {
			throw new juniter.core.exception.TechnicalException("Could decode from base 58: " + e.getMessage());
		}
	}

	private static byte[] decodeBase64(String data) {
		return Base64.decode(data.toCharArray());
	}

	static byte[] decodeUTF8(String string) {
		return string.getBytes(CHARSET_UTF8);
	}

//	public static String encodeBase58(byte[] data) {
//		return Base58.encode(data);
//	}

	static String encodeBase64(byte[] data) {
		return new String(Base64.encode(data));
	}

//	public static String encodeUTF8(byte[] bytes) {
//		return new String(bytes, CHARSET_UTF8);
//	}

	//	private static Charset initCharset(String charsetName) {
	//		final Charset result = Charset.forName(charsetName);
	//		if (result == null)
	//			throw new TechnicalException("Could not reload charset: " + charsetName);
	//		return result;
	//	};

	public static String hash(String message) {
		final byte[] hash = new byte[Sodium.CRYPTO_HASH_SHA256_BYTES];
		final byte[] messageBinary = Crypto.decodeUTF8(message);
		naCl.crypto_hash_sha256(hash, messageBinary, messageBinary.length);
		return bytesToHex(hash).toUpperCase();
	}


	private static boolean verify(byte[] message, byte[] signature, byte[] publicKey) {
		final byte[] sigAndMsg = new byte[SIGNATURE_BYTES + message.length];
		System.arraycopy(signature, 0, sigAndMsg, 0, SIGNATURE_BYTES);
		System.arraycopy(message, 0, sigAndMsg, 64, message.length);
		final byte[] buffer = new byte[SIGNATURE_BYTES + message.length];
		final LongLongByReference bufferLength = new LongLongByReference(0);

		final int result = sodium().crypto_sign_ed25519_open(buffer, bufferLength, sigAndMsg, sigAndMsg.length,
				publicKey);
		return result == 0;
	}

	/**
	 * Verify a signature against data & public key. Return true of false as
	 * callback argument.
	 *
	 * @param rawMsg the message to verify
	 * @param rawSig the signature of the message
	 * @param rawPub the author of the message
	 */

	public static boolean verify(String rawMsg, String rawSig, String rawPub) {

		if(rawMsg == null || rawSig ==null || rawPub==null)
			return false;

		final var msg = decodeUTF8(rawMsg);
		final var sig = decodeBase64(rawSig);
		final var pub = decodeBase58(rawPub);

		// checkLength(sig, SIGNATURE_BYTES);
		return verify(msg, sig, pub);
	}

	public static byte[] zeros(int n) {
		return new byte[n];
	}
}
