package cn.katasea.util;

import cn.katasea.util.SMUtil.ByteUtil;
import cn.katasea.util.SMUtil.SM4;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SM4Utils {
    public SM4Utils() {
    }

    public static String encryptData_ECB(String plainText, String secretKey, boolean isHex) {
        try {
            byte[] keyBytes;
            if (isHex) {
                keyBytes = ByteUtil.hexStringToBytes(secretKey);
            } else {
                keyBytes = secretKey.getBytes();
            }

            SM4 sm4 = new SM4();
            long[] sk = new long[32];
            sm4.sm4_setkey_enc(sk, keyBytes);
            byte[] encrypted = sm4.sm4_crypt_ecb(true, SM4.SM4_ENCRYPT, sk, plainText.getBytes("GBK"));
            String cipherText = new BASE64Encoder().encode(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptData_ECB(String cipherText, String secretKey, boolean isHex) {
        try {
            byte[] keyBytes;
            if (isHex) {
                keyBytes = ByteUtil.hexStringToBytes(secretKey);
            } else {
                keyBytes = secretKey.getBytes();
            }

            SM4 sm4 = new SM4();
            long[] sk = new long[32];
            sm4.sm4_setkey_dec(sk, keyBytes);
            byte[] decrypted = sm4.sm4_crypt_ecb(true, SM4.SM4_DECRYPT, sk, new BASE64Decoder().decodeBuffer(cipherText));
            return new String(decrypted, "GBK");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String encryptData_CBC(String plainText, String secretKey, String iv, boolean isHex) {
        try {
            byte[] keyBytes;
            byte[] ivBytes;
            if (isHex) {
                keyBytes = ByteUtil.hexStringToBytes(secretKey);
                ivBytes = ByteUtil.hexStringToBytes(iv);
            } else {
                keyBytes = secretKey.getBytes();
                ivBytes = iv.getBytes();
            }

            SM4 sm4 = new SM4();
            long[] sk = new long[32];
            sm4.sm4_setkey_enc(sk, keyBytes);
            byte[] encrypted = sm4.sm4_crypt_cbc(true, SM4.SM4_ENCRYPT, sk, ivBytes, plainText.getBytes("GBK"));
            String cipherText = new BASE64Encoder().encode(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptData_CBC(String cipherText, String secretKey, String iv, boolean isHex) {
        try {
            byte[] keyBytes;
            byte[] ivBytes;
            if (isHex) {
                keyBytes = ByteUtil.hexStringToBytes(secretKey);
                ivBytes = ByteUtil.hexStringToBytes(iv);
            } else {
                keyBytes = secretKey.getBytes();
                ivBytes = iv.getBytes();
            }

            SM4 sm4 = new SM4();
            long[] sk = new long[32];
            sm4.sm4_setkey_dec(sk, keyBytes);
            byte[] decrypted = sm4.sm4_crypt_cbc(true, SM4.SM4_DECRYPT, sk, ivBytes, new BASE64Decoder().decodeBuffer(cipherText));
            return new String(decrypted, "GBK");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) throws IOException {
        String plainText = "你是哪块小饼干你是哪块小饼干";

        System.out.println("ECB模式");
        String cipherText = SM4Utils.encryptData_ECB(plainText, "JeF8U9wHFOMfs2Y8", false);
        System.out.println("密文: " + cipherText);
        System.out.println("");

        plainText = SM4Utils.decryptData_ECB(cipherText, "JeF8U9wHFOMfs2Y8", false);
        System.out.println("明文: " + plainText);
        System.out.println("");

        System.out.println("CBC模式");
        String iv = "UISwD9fW6cFh9SbS";
        cipherText = SM4Utils.encryptData_CBC(plainText, "JeF8U9wHFOMfs2Y8", iv, false);
        System.out.println("密文: " + cipherText);
        System.out.println("");

        plainText = SM4Utils.decryptData_CBC(cipherText, "JeF8U9wHFOMfs2Y8", iv, false);
        System.out.println("明文: " + plainText);
    }
}

