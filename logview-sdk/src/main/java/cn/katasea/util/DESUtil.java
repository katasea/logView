package cn.katasea.util;

import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class DESUtil {

		
	private static final byte[] IV = {1,2,3,4,5,6,7,8};
	private static final String BM = Global.ENCODING;
	
	/**
	 * 加密 
	 * @param encryptString 加密字符串
	 * @param encryptKey 密钥
	 * @return
	 * @throws Exception
	 */
    public static String encrypt(String encryptString, String encryptKey)
            throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(IV);
        SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes(BM));
		return Base64.encodeBase64String(encryptedData); // 加密
    }
    
    /**
     * 解密
     * @param decryptString 解密字符串
     * @param decryptKey 密钥
     * @return
     * @throws Exception
     */
	public static String decrypt(String decryptString, String decryptKey)
			throws Exception {
		byte[] byteMi = Base64.decodeBase64(decryptString);
		IvParameterSpec zeroIv = new IvParameterSpec(IV);
		SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
		byte decryptedData[] = cipher.doFinal(byteMi);
		return new String(decryptedData,BM);
	}
	
	public static void main(String[] args) throws Exception {
//		String encrypt = encrypt("{\"body\":\"测试描述\",\"channel\":\"WX_QR\",\"chargeAmt\":\"1\",\"description\":\"测试描述\",\"notifyUrl\":\"https://tz.0592pay.com/DZNotify/YLZ\",\"outChargeNo\":\"20180912095031\",\"outChargeTime\":\"20180912095033\",\"subject\":\"测试商品\"}"
//, "+WTlmlWn");
//		System.out.println(encrypt);

        System.out.println(decrypt("RPhlOUV10eUu+lMXmgVYxjBEKjzF/lGCYXWWsMfTdYJVdT4sGQrVcjoXftrwBefaUGjyxCXTAtsBWqiaqd/OEWhjw058UqsDuUFHqsiCBQVv9h36RjixVqignpEFezlTpQNR6z7tr3uu/+UgeS4Z4pBZvrl/hvytoQ1azQGOBgKwDuGXrmuZkZcWjC7NCrotfLpsm/PJ9/jepnCO8ObrgpczfmSVOssQcC+I43Ju9WL+dooSmmaRTw==", "3NEzdpsy"));
	}
}
