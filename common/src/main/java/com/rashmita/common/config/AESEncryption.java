package com.rashmita.common.config;

import com.rashmita.common.constants.ApiConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
@Component
@Slf4j
public class AESEncryption {

    private static String key = "Bar12345Bar12345"; // 128 bit key
    private static String initVector = "RandomInitVector"; // 16 bytes IV
    private static String forwardEncryptionKey = "FoneLoan135!*@&#"; // 128 bit key
    private static String forwardEncryptionInitVector = "IVFoneloan135!&#"; // 16 bytes IV
    private static String backwardEncryptionKey = "FoneLoan245!*@&#"; // 128 bit key
    private static String backwardEncryptionInitVector = "IVFoneLoan246!&#"; // 16 bytes IV

    public static String encryptText(String stringToEncrypt) {
        return encryptTextWithKeyAndInitVector(stringToEncrypt, key, initVector);
    }

    public static String decryptText(String stringToDecrypt) {
        return decryptTextWithKeyAndInitVector(stringToDecrypt, key, initVector);
    }

    public static String encryptTextForBackwardEncryption(String stringToEncrypt) {
        return encryptTextWithKeyAndInitVector(stringToEncrypt, backwardEncryptionKey, backwardEncryptionInitVector);
    }

    public static String decryptTextForForwardEncryption(String stringToDecrypt) {
        return decryptTextWithKeyAndInitVector(stringToDecrypt, forwardEncryptionKey, forwardEncryptionInitVector);
    }

    private static String encryptTextWithKeyAndInitVector(String stringToEncrypt, String key, String initVector) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(stringToEncrypt.getBytes());
            return Base64.encodeBase64String(encrypted);
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }

        return null;
    }

    private static String decryptTextWithKeyAndInitVector(String stringToDecrypt, String key, String initVector) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decodeBase64(stringToDecrypt));

            return new String(original);
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
        return null;
    }

    public static String decrypt(final String stringToDecrypt) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            return new String(cipher.doFinal(Base64.decodeBase64(stringToDecrypt)));
        } catch (Exception ex) {
            log.info(ex.getMessage());
        }
        return ApiConstants.EMPTY;
    }
}

