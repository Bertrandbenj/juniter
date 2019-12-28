package juniter.core.crypto;

/*
 * #%L
 * UCoin Java :: Core Shared
 * %%
 * Copyright (C) 2014 - 2016 EIS
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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Neither org.apache.commons.codec.digest.DigestUtils nor org.apache.commons.codec.binary.Hex
 * take the ENCODING into account, they both use the system's default encoding which is wrong
 * in a web environment.
 * <p/>
 * see: https://github.com/MyMalcom/malcom-lib-android/blob/master/src/main/java/com/malcom/library/android/utils/DigestUtils.java
 * @author Malcom Ventures, S.L.
 * @since 2012
 */
public class DigestUtils {
    private static final char[] HEXITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final String SHA1_ALGORITHM = "SHA1";
    private static final String UTF_8 = "UTF-8";

    /**
     * Converts an array of bytes into an array of characters representing the hexidecimal values of each byte in order.
     * The returned array will be double the length of the passed array, as it takes two characters to represent any
     * given byte.
     *
     * see http://stackoverflow.com/questions/332079/in-java-how-do-i-convert-a-byte-array-to-a-string-of-hex-digits-while-keeping-le
     *
     * @param data a byte[] to convert to Hex characters
     * @return A char[] containing hexidecimal character
     */
    private static String encodeHex(byte[] data) {
        char[] out = new char[data.length << 1]; // == new char[data.length * 2];
        for (int i = 0, j = 0; i < data.length; i++) {
            out[j++] = HEXITS[(0xF0 & data[i]) >>> 4]; // HEXITS[(data[i] & 0xFF) / 16];
            out[j++] = HEXITS[0x0F & data[i]]; // HEXITS[(data[i] & 0xFF) % 16];
        }
        return new String(out);
    }

    /**
     * Genera a SHA1 fingerprint from the given message
     *
     * @param message a message to encodeinto SHA-1
     * @return a SHA1 fingerprint
     */
    public static String sha1Hex(String message) {
        return sha1Hex(message, UTF_8);
    }

    private static String sha1Hex(String message, String encoding) {
        try {
            MessageDigest md = getSHA1Instance();
            return encodeHex(md.digest(message.getBytes(encoding)));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private static MessageDigest getSHA1Instance() {
        try {
            return MessageDigest.getInstance(SHA1_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}