/*
 * The MIT License
 *
 * Copyright (c) 2018 Noukkis.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package ch.gwyrin.noukkis.keepqr.model;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Noukkis on 27.05.2018.
 */

public class Crypto {

    private final static String ALGORITHM = "AES/CTR/NoPadding";

    private Cipher cipher;

    public Crypto() {
        try {
            cipher = Cipher.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String data, String key, String iv) throws GeneralSecurityException, UnsupportedEncodingException {
        byte[] raw = data.getBytes("UTF-8");
        byte[] encrypted = encrypt(raw, b64(key), b64(iv));
        encrypted = convertFinal(encrypted);
        return b64(encrypted);
    }

    private byte[] encrypt(byte[] raw, byte[] key, byte[] iv) throws GeneralSecurityException {
        SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        return cipher.doFinal(raw);
    }


    private byte[] convertFinal(byte[] bytes) {
        byte[] res = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            byte b = bytes[i];
            res[i] = b > 0 ? b : (byte) (b + 256);
        }
        return res;
    }

    private byte[] b64(String string) {
        return Base64.decode(string, Base64.DEFAULT);
    }

    private String b64(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.DEFAULT).replace("\n", "");
    }
}
