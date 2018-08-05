package juniter.crypto;

import java.nio.charset.Charset;

import org.abstractj.kalium.crypto.Util;

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

import com.lambdaworks.codec.Base64;

import juniter.exception.TechnicalException;

public class CryptoUtils extends Util {

	public static final Charset CHARSET_UTF8 = Charset.forName("UTF-8");
	public static final Charset CHARSET_ASCII = Charset.forName("US-ASCII");

	public static byte[] copyEnsureLength(byte[] source, int length) {
		final byte[] result = zeros(length);
		if (source.length > length) {
			System.arraycopy(source, 0, result, 0, length);
		} else {
			System.arraycopy(source, 0, result, 0, source.length);
		}
		return result;
	}

	public static byte[] decodeAscii(String string) {
		return string.getBytes(CHARSET_ASCII);
	}

	public static byte[] decodeBase58(String data) {
		try {
			return Base58.decode(data);
		} catch (final Exception e) {
			throw new juniter.exception.TechnicalException("Could decode from base 58: " + e.getMessage());
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

	protected static Charset initCharset(String charsetName) {
		final Charset result = Charset.forName(charsetName);
		if (result == null)
			throw new TechnicalException("Could not load charset: " + charsetName);
		return result;
	}

	/**
	 * /** Verify a signature against data & public key. Return true of false as
	 * callback argument.
	 *
	 * @param rawMsg
	 * @param rawSig
	 * @param rawPub
	 */

	public static void verify(String rawMsg, String rawSig, String rawPub) {
		final var msg = decodeUTF8(rawMsg);
		final var sig = decodeBase64(rawSig);
		final var pub = decodeBase58(rawPub);
	}
	// export function verify(rawMsg:string, rawSig:string, rawPub:string) {
//  const msg = decodeUTF8(rawMsg);
//  const sig = decodeBase64(rawSig);
//  const pub = Base58decode(rawPub);
//  const m = new Uint8Array(crypto_sign_BYTES + msg.length);
//  const sm = new Uint8Array(crypto_sign_BYTES + msg.length);
//  let i;
//  for (i = 0; i < crypto_sign_BYTES; i++) sm[i] = sig[i];
//  for (i = 0; i < msg.length; i++) sm[i+crypto_sign_BYTES] = msg[i];
//
//  // Call to verification lib...
//  return naclBinding.verify(m, sm, pub);
//}

	public static byte[] zeros(int n) {
		return new byte[n];
	}
}
