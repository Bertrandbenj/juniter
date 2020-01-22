package juniter.core.crypto;

import com.lambdaworks.crypto.SCrypt;
import jnr.ffi.byref.LongLongByReference;
import juniter.core.exception.TechnicalException;
import org.abstractj.kalium.crypto.Util;

import java.security.GeneralSecurityException;

import static org.abstractj.kalium.NaCl.Sodium.CRYPTO_BOX_CURVE25519XSALSA20POLY1305_PUBLICKEYBYTES;
import static org.abstractj.kalium.NaCl.Sodium.CRYPTO_BOX_CURVE25519XSALSA20POLY1305_SECRETKEYBYTES;
import static org.abstractj.kalium.NaCl.sodium;
import static org.abstractj.kalium.crypto.Util.checkLength;
import static org.abstractj.kalium.crypto.Util.isValid;

/*
 * #%L
 * Duniter4j :: Core API
 * %%
 * Copyright (C) 2014 - 2015 EIS
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either getVersion 3 of the
 * License, or (at your option) any later getVersion.
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

public class SecretBox {

    // Length of the key
    private static int SEED_LENGTH = 32;

    private final String pubKey;
    private final byte[] secretKey;

    public SecretBox(String pub, byte[] sec) {
        secretKey = sec;
        pubKey = pub;
    }

    public SecretBox(byte[] seed) {
        checkLength(seed, SEED_LENGTH);
        secretKey = Crypto.zeros(CRYPTO_BOX_CURVE25519XSALSA20POLY1305_SECRETKEYBYTES * 2);
        final byte[] publicKey = Crypto.zeros(CRYPTO_BOX_CURVE25519XSALSA20POLY1305_PUBLICKEYBYTES);
        isValid(sodium().crypto_sign_ed25519_seed_keypair(publicKey, secretKey, seed), "Failed to generate a key pair");
        pubKey = Base58.encode(publicKey);
    }

    public SecretBox(String salt, String password) {
        this(computeSeedFromSaltAndPassword(salt, password));
    }

    private static byte[] computeSeedFromSaltAndPassword(String salt, String password) {
        try {
            int SCRYPT_PARAMS_N = 4096;
            int SCRYPT_PARAMS_r = 16;
            int SCRYPT_PARAMS_p = 1;

            return SCrypt.scrypt(Crypto.decodeAscii(password), Crypto.decodeAscii(salt),
                    SCRYPT_PARAMS_N, SCRYPT_PARAMS_r, SCRYPT_PARAMS_p, SEED_LENGTH);
        } catch (final GeneralSecurityException e) {
            throw new TechnicalException("Unable to salt password, using Scrypt library", e);
        }
    }



//    public byte[] decrypt(byte[] nonce, byte[] ciphertext) {
//        checkLength(nonce, CRYPTO_SECRETBOX_XSALSA20POLY1305_NONCEBYTES);
//        final byte[] ct = Util.prependZeros(CRYPTO_BOX_CURVE25519XSALSA20POLY1305_BOXZEROBYTES, ciphertext);
//        final byte[] message = Util.zeros(ct.length);
//        //isValid(sodium().crypto_secretbox_xsalsa20poly1305_open(message, ct, ct.length, nonce, seed),"Decryption failed. Ciphertext failed verification");
//        return removeZeros(CRYPTO_BOX_CURVE25519XSALSA20POLY1305_ZEROBYTES, message);
//    }

//    public byte[] encrypt(byte[] nonce, byte[] message) {
//        checkLength(nonce, CRYPTO_SECRETBOX_XSALSA20POLY1305_NONCEBYTES);
//        final byte[] msg = Util.prependZeros(CRYPTO_BOX_CURVE25519XSALSA20POLY1305_ZEROBYTES, message);
//        final byte[] ct = Util.zeros(msg.length);
//        //isValid(sodium().crypto_secretbox_xsalsa20poly1305(ct, msg, msg.length, nonce, seed), "Encryption failed");
//        return removeZeros(CRYPTO_BOX_CURVE25519XSALSA20POLY1305_BOXZEROBYTES, ct);
//    }

    /**
     * Retrun the public key, encode in Base58
     *
     * @return the public Key
     */
    public String getPublicKey() {
        return pubKey;
    }

    /**
     * Return the secret key, encode in Base58
     *
     * @return the secretKey
     */
    public String getSecretKey() {
        return Base58.encode(secretKey);
    }

    private byte[] sign(byte[] message) {
        int SIGNATURE_BYTES = 64;
        byte[] signature = Util.prependZeros(SIGNATURE_BYTES, message);
        final LongLongByReference bufferLen = new LongLongByReference(0);
        sodium().crypto_sign_ed25519(signature, bufferLen, message, message.length, secretKey);
        signature = Util.slice(signature, 0, SIGNATURE_BYTES);

        checkLength(signature, SIGNATURE_BYTES);
        return signature;
    }

    /* -- Internal methods -- */

    public String sign(String message) {
        final byte[] messageBinary = Crypto.decodeUTF8(message);
        return Crypto.encodeBase64(sign(messageBinary));
    }

}