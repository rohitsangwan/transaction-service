package com.transaction.transac.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;

public class AESGCMEncryptor {

    public static final int GCM_NONCE_LENGTH = 12; // 96 bits IV
    public static final int GCM_TAG_LENGTH = 128; // 128 bits authentication tag

    public static String encrypt(String plainText, String secret) throws Exception {
        SecureRandom secureRandom = new SecureRandom();
        byte[] iv = new byte[GCM_NONCE_LENGTH];
        secureRandom.nextBytes(iv);

        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, gcmParameterSpec);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        byte[] encryptedWithIV = new byte[iv.length + encryptedBytes.length];
        System.arraycopy(iv, 0, encryptedWithIV, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, encryptedWithIV, iv.length, encryptedBytes.length);
        return Base64.getEncoder().encodeToString(encryptedWithIV);
    }

    public static String decrypt(String cipherText,String secret) throws Exception {
        byte[] encryptedWithIV = Base64.getDecoder().decode(cipherText);
        byte[] iv = new byte[GCM_NONCE_LENGTH];
        byte[] encryptedBytes = new byte[encryptedWithIV.length - GCM_NONCE_LENGTH];
        System.arraycopy(encryptedWithIV, 0, iv, 0, iv.length);
        System.arraycopy(encryptedWithIV, iv.length, encryptedBytes, 0, encryptedBytes.length);

        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }
}
