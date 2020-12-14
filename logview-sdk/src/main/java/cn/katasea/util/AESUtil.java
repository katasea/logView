package cn.katasea.util;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 类功能说明
 * 
 * @author sun
 * @version 1.0.0
 */

public class AESUtil {
	public static final String VIPARA = "0102030405060708";
	public static final String BM = "UTF-8";

	public static String encrypt(String content, String password) throws Exception {
		IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
		SecretKeySpec key = getKey(password);
		//new SecretKeySpec(password.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
		byte[] encryptedData = cipher.doFinal(content.getBytes(BM));
		String encryptResultStr = parseByte2HexStr(encryptedData);
//		String encryptResultStr = Base64.encode(encryptedData);
		return encryptResultStr; // 加密
	}

	//新增招行1.0.8加密算法 add by fuqiang chen
	//注意比较坑，需要替换jdk的security的两个文件才能运行。local_policy us_export_policy
	public static String encryptByECB(String content, String password) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(password.getBytes("ASCII"), "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encryptedData = cipher.doFinal(content.getBytes(BM));
		return parseByte2HexStr(encryptedData); // 加密
	}
	//新增招行1.0.8解密算法 add by fuqiang chen
	public static String decryptByECB(String content, String password) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(password.getBytes("ASCII"), "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] encrypted1 = parseHexStr2Byte(content);
		byte[] original = cipher.doFinal(encrypted1);
		return new String(original, BM);
	}

	public static String decrypt(String content, String password) throws Exception {
		content = new String(content.getBytes("UTF-8"));
		byte[] decryptFrom = parseHexStr2Byte(content);
//		byte[] decryptFrom = Base64.decode(content);
	    IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
	    SecretKeySpec key = getKey(password);
	    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
	    cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
	    byte[] decryptedData = cipher.doFinal(decryptFrom);  
	  
	    return new String(decryptedData,BM);
	}

	
	private static SecretKeySpec getKey(String strKey) throws Exception {
        byte[] arrBTmp = strKey.getBytes();
        byte[] arrB = new byte[16]; // 创建一个空的16位字节数组（默认值为0）

        for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
            arrB[i] = arrBTmp[i];
        }

        SecretKeySpec skeySpec = new SecretKeySpec(arrB, "AES");

        return skeySpec;
    }

	/**
	 * 将二进制转换成16进制
	 * 
	 * @param buf
	 * @return
	 */
	public static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex.toUpperCase());
		}
		return sb.toString();
	}

	/**
	 * 将16进制转换为二进制
	 * 
	 * @param hexStr
	 * @return
	 */
	public static byte[] parseHexStr2Byte(String hexStr) {
		if (hexStr.length() < 1)
			return null;
		byte[] result = new byte[hexStr.length() / 2];
		for (int i = 0; i < hexStr.length() / 2; i++) {
			int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
			int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
			result[i] = (byte) (high * 16 + low);
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		String password = "q!w@e3r$";

//		 String str = encrypt("q!w@e3r$", password);
//		 System.out.println("--" + str);
		
		 String str1 = encrypt("1234567890123456", password);
		 System.out.println("--" + str1);
//		String oldStr = decrypt("CFA6E071093E16A948D171A71678B233", password);
//		System.out.println("====" + oldStr);
			String encryptData = "B1FE11C116A11DD42EB0C621465D4C8D8F6A1FA376D22FC1A7F5347ABBA7E3FFEC1EE25ED42AE2EE45E504DC5C8FD013A17F809AA6C135A6355FF8D3F05CABB2F932F465AB564080D3AEFECE8C360815D1537690A7B82A7562F879E13E91A8F42028E120653D8E6F4231390F39E6DD71B1645FAB850ED3907B58E0A87476F5D968B0D5BCA7862C4CABF0AA780AB1EF1F25FA9382D47731D027C6193587E868B261DC4FDDEBE5383F8A7A6CA930AEA7B9D902AA7FC774E2B26511BEC3486C65457F30992596F88C56C7C3EC21F8F9451641A5DA8B8D42A18FDC4127947AE391338DBE9045E5165D96C8CC908067DEFB3DDC1C1075C3EC68647B689521CE3A5DB53C68B9ACEE7D7B30D12AC96E8FEE8F70E53AC0EA78C31EB457EE8A2EAC2E5F494C4F61ECE3CE5BD032DB39497AE3C72AADC68820F66A07E27B7569853CB4C6459E3272C8F1990D2EC777131D0211058D9464828F81B974F4976524421C2A02629AD3D8530D1B0F50312B8C55AC03DD3C2ABD351CB8219BBB812E54060A38039C407EAA18F94CCDE69AF724EE1F4AF3935D2313BE2C46A6C66D2CCEC99953F0C92C4C37EB9DBC4F27FD1D78DD55C7878B82C525EAA5B4E7ACB3CE2CE5BF9978383D9311637B32A006A9B937B52D0B5D057381397B8C915DD866C24775A89CA8D7";
			String appSecret = "C1CEIR5CGN004030AEIR5CGN00174006";

		 System.out.println(decrypt(encryptData,appSecret));
	}

}
