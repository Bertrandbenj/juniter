package juniter.core.crypto;

import static org.abstractj.kalium.NaCl.sodium;

import java.nio.charset.Charset;
import java.util.Random;
import java.util.stream.IntStream;

import org.abstractj.kalium.NaCl;
import org.abstractj.kalium.NaCl.Sodium;
import org.abstractj.kalium.crypto.Util;

import com.lambdaworks.codec.Base64;

import jnr.ffi.byref.LongLongByReference;

public class Crypto extends Util {

	public static class Tuple<A, B> {

		public final A a;
		public final B b;

		public Tuple(A a, B b) {
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
	public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");

	public static final Charset CHARSET_ASCII = Charset.forName("US-ASCII");

	private static final int SIGNATURE_BYTES = 64;

	protected final static char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

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

	public static byte[] decodeAscii(String string) {
		return string.getBytes(CHARSET_ASCII);
	}

	private static byte[] decodeBase58(String data) {
		try {
			return Base58.decode(data);
		} catch (final Exception e) {
			throw new juniter.core.exception.TechnicalException("Could decode from base 58: " + e.getMessage());
		}
	}

	public static byte[] decodeBase64(String data) {
		return Base64.decode(data.toCharArray());
	}

	public static byte[] decodeUTF8(String string) {
		return string.getBytes(CHARSET_UTF8);
	}

	public static String encodeBase58(byte[] data) {
		return Base58.encode(data);
	}

	public static String encodeBase64(byte[] data) {
		return new String(Base64.encode(data));
	}

	public static String encodeUTF8(byte[] bytes) {
		return new String(bytes, CHARSET_UTF8);
	}

	//	private static Charset initCharset(String charsetName) {
	//		final Charset result = Charset.forName(charsetName);
	//		if (result == null)
	//			throw new TechnicalException("Could not load charset: " + charsetName);
	//		return result;
	//	};

	public static String hash(String message) {
		final byte[] hash = new byte[Sodium.CRYPTO_HASH_SHA256_BYTES];
		final byte[] messageBinary = Crypto.decodeUTF8(message);
		naCl.crypto_hash_sha256(hash, messageBinary, messageBinary.length);
		return bytesToHex(hash).toUpperCase();
	}

	public static String pow(int difficulty, String innerHash) {
		final IntStream is;

		return new Random()
				.ints()
				.mapToObj(nonce -> "InnerHash: " + innerHash + "\nNonce: " + nonce + "\n")
				.map(signedPartSigned -> hash(signedPartSigned))
				.takeWhile(hash -> hash.startsWith("000"))
				.findAny()
				.get();
	}

	public static Tuple<String, Integer> pow2(int difficulty, String innerHash) {

		final var found = false;

		while (!found) {

			final Integer nonce = new Random().nextInt();
			final String signPartSign = "InnerHash: " + innerHash + "\nNonce: " + nonce + "\n";
			final String hash = hash(signPartSign);

			final boolean unvalid = hash
					.substring(0, difficulty)
					.chars()
					.anyMatch(c -> (char) c != '0');

			if (!unvalid)
				return new Tuple<String, Integer>(hash, nonce);

		}

	}

	private static boolean verify(byte[] message, byte[] signature, byte[] publicKey) {
		final byte[] sigAndMsg = new byte[SIGNATURE_BYTES + message.length];
		for (int i = 0; i < SIGNATURE_BYTES; i++) {
			sigAndMsg[i] = signature[i];
		}
		for (int i = 0; i < message.length; i++) {
			sigAndMsg[i + SIGNATURE_BYTES] = message[i];
		}
		final byte[] buffer = new byte[SIGNATURE_BYTES + message.length];
		final LongLongByReference bufferLength = new LongLongByReference(0);

		final int result = sodium().crypto_sign_ed25519_open(buffer, bufferLength, sigAndMsg, sigAndMsg.length,
				publicKey);
		final boolean validSignature = result == 0;
		return validSignature;
	}

	/**
	 * Verify a signature against data & public key. Return true of false as
	 * callback argument.
	 *
	 * @param rawMsg
	 * @param rawSig
	 * @param rawPub
	 */

	public static boolean verify(String rawMsg, String rawSig, String rawPub) {
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
