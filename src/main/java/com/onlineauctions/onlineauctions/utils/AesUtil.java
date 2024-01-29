package com.onlineauctions.onlineauctions.utils;


import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * 用于进行AES加密操作
 */
public class AesUtil {

    private static final String ALGORITHM = "AES";

    private static final String ENCODING = "UTF-8";

    //密钥，长度必须为16、24、32位
    private static final String KEY = "gF1/S8n5kXLYJJWU";

    public static final String DEFAULT_PASSWORD = AesUtil.encrypt("MTIzNDU2");

    /**
     * 使用密钥对明文进行加密，并将密文转换成Base64编码的字符串返回
     *
     * @param plainText 明文字符串
     * @return 加密后的密文字符串，如果加密失败返回null
     */
    public static String encrypt(String plainText) {
        try {
            // 将密钥转换成KeySpec对象
            SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
            // 创建Cipher对象，并初始化为加密模式
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            // 将明文转换成字节数组
            byte[] plainTextBytes = plainText.getBytes(ENCODING);
            // 加密明文
            byte[] encryptedBytes = cipher.doFinal(plainTextBytes);
            // 将密文转换成Base64编码的字符串并返回
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解密加密文本
     *
     * @param encryptedText 加密的文本
     * @return 解密后的明文
     */
    public static String decrypt(String encryptedText) {
        try {
            // 将密钥转换成KeySpec对象
            SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
            // 创建Cipher对象，并初始化为解密模式
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            // 将密文从Base64编码的字符串转换成字节数组
            byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText);
            // 解密密文
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            // 将明文转换成字符串并返回
            return new String(decryptedBytes, ENCODING);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}