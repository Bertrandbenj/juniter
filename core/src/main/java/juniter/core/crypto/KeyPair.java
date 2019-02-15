package juniter.core.crypto;

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


/**
 * Created by eis on 28/01/15.
 */
public class KeyPair {

    public byte[] publicKey;
    public byte[] secretKey;

    public KeyPair(byte[] publicKey, byte[] secretKey) {
        this.publicKey = publicKey;
        this.secretKey = secretKey;
    }

    public byte[] getSecKey() {
        return secretKey;
    }

    public void setSecKey(byte[] secKey) {
        this.secretKey = secKey;
    }

    public byte[] getPubKey() {
        return publicKey;
    }

    public void setPubKey(byte[] pubKey) {
        this.publicKey = pubKey;
    }
}