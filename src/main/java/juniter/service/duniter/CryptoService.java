package juniter.service.utils;

import static org.abstractj.kalium.NaCl.sodium;
import static org.abstractj.kalium.NaCl.Sodium.CRYPTO_BOX_CURVE25519XSALSA20POLY1305_BOXZEROBYTES;
import static org.abstractj.kalium.NaCl.Sodium.CRYPTO_BOX_CURVE25519XSALSA20POLY1305_NONCEBYTES;
import static org.abstractj.kalium.NaCl.Sodium.CRYPTO_BOX_CURVE25519XSALSA20POLY1305_ZEROBYTES;
import static org.abstractj.kalium.crypto.Util.checkLength;
import static org.abstractj.kalium.crypto.Util.isValid;
import static org.abstractj.kalium.crypto.Util.prependZeros;
import static org.abstractj.kalium.crypto.Util.removeZeros;

import java.security.GeneralSecurityException;

import org.abstractj.kalium.NaCl;
import org.abstractj.kalium.NaCl.Sodium;
import org.abstractj.kalium.crypto.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

/*
 * #%L
 * Duniter4j :: Core API
 * %%
 * Copyright (C) 2014 - 2015 EIS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import com.lambdaworks.crypto.SCrypt;

import jnr.ffi.byref.LongLongByReference;
import juniter.crypto.CryptoUtils;
import juniter.crypto.KeyPair;
import juniter.exception.TechnicalException;

/**
 * Crypto services (sign...) Created by eis on 10/01/15.
 */
@Service
public class CryptoService {

	private static final Logger log = LogManager.getLogger();

	// Length of the seed key (generated deterministically, use to generate the 64
	// key pair).
	private static final int SEED_BYTES = 32;
	// Length of a signature return by crypto_sign
	private static final int SIGNATURE_BYTES = 64;
	// Length of a public key
	private static final int PUBLICKEY_BYTES = 32;
	// Length of a secret key
	private static final int SECRETKEY_BYTES = 64;

	// Scrypt default parameters
	public static final int SCRYPT_PARAMS_N = 4096;
	public static final int SCRYPT_PARAMS_r = 16;
	public static final int SCRYPT_PARAMS_p = 1;
	protected final static char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

	// Hash
	private static int HASH_BYTES = 256;

	protected static String bytesToHex(byte[] bytes) {
		final char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			final int v = bytes[j] & 0xFF;
			hexChars[j * 2] = HEX_CHARS[v >>> 4];
			hexChars[j * 2 + 1] = HEX_CHARS[v & 0x0F];
		}
		return new String(hexChars);
	}

	private final Sodium naCl;

	public CryptoService() {
		naCl = NaCl.sodium();
	}

	public String box(String message, byte[] nonce, byte[] senderSignSk, byte[] receiverSignPk) {
		checkLength(nonce, CRYPTO_BOX_CURVE25519XSALSA20POLY1305_NONCEBYTES);

		final byte[] messageBinary = prependZeros(CRYPTO_BOX_CURVE25519XSALSA20POLY1305_ZEROBYTES,
				CryptoUtils.decodeBase64(message));

		final byte[] senderBoxSk = new byte[Sodium.CRYPTO_BOX_CURVE25519XSALSA20POLY1305_SECRETKEYBYTES];
		naCl.crypto_sign_ed25519_sk_to_curve25519(senderBoxSk, senderSignSk);

		final byte[] receiverBoxPk = new byte[Sodium.CRYPTO_BOX_CURVE25519XSALSA20POLY1305_PUBLICKEYBYTES];
		naCl.crypto_sign_ed25519_pk_to_curve25519(receiverBoxPk, receiverSignPk);

		final byte[] cypherTextBinary = new byte[messageBinary.length];
		isValid(sodium().crypto_box_curve25519xsalsa20poly1305(cypherTextBinary, messageBinary, cypherTextBinary.length,
				nonce, senderBoxSk, receiverBoxPk), "Encryption failed");
		return CryptoUtils
				.encodeBase64(removeZeros(CRYPTO_BOX_CURVE25519XSALSA20POLY1305_BOXZEROBYTES, cypherTextBinary));
	}

	public String box(String message, byte[] nonce, String senderSignSk, String receiverSignPk) {
		final byte[] senderSignSkBinary = CryptoUtils.decodeBase58(senderSignSk);
		final byte[] receiverSignPkBinary = CryptoUtils.decodeBase58(receiverSignPk);
		return box(message, nonce, senderSignSkBinary, receiverSignPkBinary);
	}

	public byte[] getBoxRandomNonce() {
		final byte[] nonce = new byte[Sodium.CRYPTO_BOX_CURVE25519XSALSA20POLY1305_NONCEBYTES];
		naCl.randombytes(nonce, nonce.length);

		return nonce;
	}

	public KeyPair getKeyPair(String salt, String password) {
		return getKeyPairFromSeed(getSeed(salt, password));
	}

	public KeyPair getKeyPairFromSeed(byte[] seed) {
		final byte[] secretKey = CryptoUtils.zeros(SECRETKEY_BYTES);
		final byte[] publicKey = CryptoUtils.zeros(PUBLICKEY_BYTES);
		Util.isValid(naCl.crypto_sign_ed25519_seed_keypair(publicKey, secretKey, seed),
				"Failed to generate a key pair");

		return new KeyPair(publicKey, secretKey);
	}

	public KeyPair getRandomKeypair() {
		return getKeyPairFromSeed(String.valueOf(System.currentTimeMillis()).getBytes());
	}

	public byte[] getSeed(String salt, String password) {
		return getSeed(salt, password, SCRYPT_PARAMS_N, SCRYPT_PARAMS_r, SCRYPT_PARAMS_p);
	}

	public byte[] getSeed(String salt, String password, int N, int r, int p) {
		try {
			final byte[] seed = SCrypt.scrypt(CryptoUtils.decodeAscii(password), CryptoUtils.decodeAscii(salt), N, r, p,
					SEED_BYTES);
			return seed;
		} catch (final GeneralSecurityException e) {
			throw new TechnicalException("Unable to salt password, using Scrypt library", e);
		}
	}

	public String hash(String message) {
		final byte[] hash = new byte[Sodium.CRYPTO_HASH_SHA256_BYTES];
		final byte[] messageBinary = CryptoUtils.decodeUTF8(message);
		naCl.crypto_hash_sha256(hash, messageBinary, messageBinary.length);
		return bytesToHex(hash).toUpperCase();
	}

	public String openBox(String cypherText, byte[] nonce, byte[] senderSignPk, byte[] receiverSignSk) {
		checkLength(nonce, CRYPTO_BOX_CURVE25519XSALSA20POLY1305_NONCEBYTES);
		final byte[] cypherTextBinary = prependZeros(CRYPTO_BOX_CURVE25519XSALSA20POLY1305_BOXZEROBYTES,
				CryptoUtils.decodeBase64(cypherText));

		final byte[] receiverBoxSk = new byte[Sodium.CRYPTO_BOX_CURVE25519XSALSA20POLY1305_SECRETKEYBYTES];
		naCl.crypto_sign_ed25519_sk_to_curve25519(receiverBoxSk, receiverSignSk);

		final byte[] senderBoxPk = new byte[Sodium.CRYPTO_BOX_CURVE25519XSALSA20POLY1305_PUBLICKEYBYTES];
		naCl.crypto_sign_ed25519_pk_to_curve25519(senderBoxPk, senderSignPk);

		final byte[] messageBinary = new byte[cypherTextBinary.length];
		isValid(sodium().crypto_box_curve25519xsalsa20poly1305_open(messageBinary, cypherTextBinary,
				cypherTextBinary.length, nonce, senderBoxPk, receiverBoxSk),
				"Decryption failed. Ciphertext failed verification.");
		return CryptoUtils.encodeUTF8(removeZeros(CRYPTO_BOX_CURVE25519XSALSA20POLY1305_ZEROBYTES, messageBinary));
	}

	public String openBox(String cypherText, String nonce, String senderSignPk, String receiverSignSk) {
		return openBox(cypherText, CryptoUtils.decodeBase58(nonce), CryptoUtils.decodeBase58(senderSignPk),
				CryptoUtils.decodeBase58(receiverSignSk));
	}

	protected byte[] sign(byte[] message, byte[] secretKey) {
		byte[] signature = Util.prependZeros(SIGNATURE_BYTES, message);
		final LongLongByReference smLen = new LongLongByReference(0);
		naCl.crypto_sign_ed25519(signature, smLen, message, message.length, secretKey);
		signature = Util.slice(signature, 0, SIGNATURE_BYTES);

		Util.checkLength(signature, SIGNATURE_BYTES);
		return signature;
	}

	public String sign(String message, byte[] secretKey) {
		final byte[] messageBinary = CryptoUtils.decodeUTF8(message);
		return CryptoUtils.encodeBase64(sign(messageBinary, secretKey));
	}

	/* -- Internal methods -- */

	public String sign(String message, String secretKey) {
		final byte[] messageBinary = CryptoUtils.decodeUTF8(message);
		final byte[] secretKeyBinary = CryptoUtils.decodeBase58(secretKey);
		return CryptoUtils.encodeBase64(sign(messageBinary, secretKeyBinary));
	}

	protected boolean verify(byte[] message, byte[] signature, byte[] publicKey) {
		final byte[] sigAndMsg = new byte[SIGNATURE_BYTES + message.length];
		for (int i = 0; i < SIGNATURE_BYTES; i++) {
			sigAndMsg[i] = signature[i];
		}
		for (int i = 0; i < message.length; i++) {
			sigAndMsg[i + SIGNATURE_BYTES] = message[i];
		}
		log.info("verify " + sigAndMsg + " ");
		final byte[] buffer = new byte[SIGNATURE_BYTES + message.length];
		final LongLongByReference bufferLength = new LongLongByReference(0);

		final int result = naCl.crypto_sign_ed25519_open(buffer, bufferLength, sigAndMsg, sigAndMsg.length, publicKey);
		final boolean validSignature = result == 0;

		return validSignature;
	}

	public boolean verify(String message, String signature, String publicKey) {
		final byte[] messageBinary = CryptoUtils.decodeUTF8(message);
		final byte[] signatureBinary = CryptoUtils.decodeBase64(signature);
		final byte[] publicKeyBinary = CryptoUtils.decodeBase58(publicKey);
		return verify(messageBinary, signatureBinary, publicKeyBinary);
	}

}
