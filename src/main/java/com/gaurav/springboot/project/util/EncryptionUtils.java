package com.gaurav.springboot.project.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class EncryptionUtils {

    private static final String ALGORITHM = "AES";
    private static final byte[] keyValue = "1234567890123456".getBytes(); // Use a secure key

    public static String encrypt(String message) {
        try {
            SecretKeySpec key = new SecretKeySpec(keyValue, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedVal = cipher.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(encryptedVal);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encrypt data", e);
        }
    }

    public static String decrypt(String encryptedData) {
        try {
            SecretKeySpec key = new SecretKeySpec(keyValue, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedVal = cipher.doFinal(decodedValue);
            return new String(decryptedVal);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decrypt data", e);
        }
    }
}
