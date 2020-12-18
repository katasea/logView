package cn.katasea.main.service.util;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.net.URLDecoder;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

/**
 * @author katasea
 * 2020/8/11 11:01
 */
public class RSAUtils4Mall {


	private static final String RSA_ALGORITHM = "RSA";
	private static final int MAX_DECRYPT_BLOCK = 128;
	private static final int MAX_ENCRYPT_BLOCK = 117;
	private static RSAPublicKey publicKey;
	private static RSAPrivateKey privateKey;

	public RSAPublicKey getPublicKey() {
		return RSAUtils4Mall.publicKey;
	}
	public RSAPrivateKey getPrivateKey() {
		return RSAUtils4Mall.privateKey;
	}

	public void getKeys() throws Exception {
		// 从 公钥保存的文件 读取 公钥的Base64文本
		String pubKeyBase64 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQClBe8VKs8wtmC3vDbsoYtC4MmzyVWA7J2tB/+kpxii1rBRcrHT7PHhtFyIat5G7nhIFE03fwVx/nloG6B9gIK/uqLd6iT/OqWwwtoEKd8Aom/pIl8CaAc87mmvL6IdCZQK9vSnfi0uX2nk1LYz2c69oo7nn5BWzY1k1PZU0yWrEQIDAQAB";
// 把 公钥的Base64文本 转换为已编码的 公钥bytes
		byte[] encPubKey = new BASE64Decoder().decodeBuffer(pubKeyBase64);

// 创建 已编码的公钥规格
		X509EncodedKeySpec encPubKeySpec = new X509EncodedKeySpec(encPubKey);

// 获取指定算法的密钥工厂, 根据 已编码的公钥规格, 生成公钥对象
		publicKey = (RSAPublicKey)KeyFactory.getInstance("RSA").generatePublic(encPubKeySpec);

		// 从 私钥保存的文件 读取 私钥的base文本
		String priKeyBase64 = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKUF7xUqzzC2YLe8Nuyhi0LgybPJVYDsna0H/6SnGKLWsFFysdPs8eG0XIhq3kbueEgUTTd/BXH+eWgboH2Agr+6ot3qJP86pbDC2gQp3wCib+kiXwJoBzzuaa8voh0JlAr29Kd+LS5faeTUtjPZzr2ijuefkFbNjWTU9lTTJasRAgMBAAECgYAUvHymTm/6QTNBE0EG62Zk8SXXViQiOa63M0tFOjaxy5lx+qAW2cOBPEPpDnNqOrAERVd4nZj7vIwumqjIQcbsCIklcI8T9FPIZ2pN5+jj3hYac23SIaRvFKN9Nf7+mXoRLHu+YtNrTfwvaflcy1JPjkQCaz0Bs6Fwy2NsaUdD8wJBAOELfh1pAr7FQaBPseFpyXBwmRAAB2lXNdKX6DNGVIqG8vv1Ej8VkCLuAX11JuHQe+Z4dh47o3tupnadjdQqdlcCQQC7uOVxevFzhHqpaA9D0RuvS9svySCoLYlaNmMjKx61phxcryLPkke58rkthcy/0TIrJB2V4ualHvlS2omO6PjXAkEAiQq7mONKtatfhGUjmHYYIi2fN/wch0yBTSYOU7MfZmA5da9OljXmWS8An0qJC/guizGOFGkS6+Z83jyhkrLs7wJBAIJt2kV5lY+cqZlQ6uJExQaMHxPZm+4eDYP3q/QAfsrZSnV/XiF40os5QT6G65cPI9tjsYa162ko1zShENiRuGsCQFVj/g1/O38+DHj/ii+vHGlfqVGAtS1AgzxgBrvnYBpkTto5N8onuGFGp1syihKLMJ9E4sAk2FaQGJff/DEc77Y=";

// 把 私钥的Base64文本 转换为已编码的 私钥bytes
		byte[] encPriKey = new BASE64Decoder().decodeBuffer(priKeyBase64);

// 创建 已编码的私钥规格
		PKCS8EncodedKeySpec encPriKeySpec = new PKCS8EncodedKeySpec(encPriKey);

// 获取指定算法的密钥工厂, 根据 已编码的私钥规格, 生成私钥对象
		privateKey = (RSAPrivateKey)KeyFactory.getInstance("RSA").generatePrivate(encPriKeySpec);
	}

	public void geneKeys() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM, new BouncyCastleProvider());
		keyPairGenerator.initialize(1024);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		privateKey = (RSAPrivateKey) keyPair.getPrivate();
		publicKey = (RSAPublicKey) keyPair.getPublic();
	}


	public String encodeByPrivateKey(String body) throws Exception {
		Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		byte[] inputArray = body.getBytes();
		int inputLength = inputArray.length;
//		System.out.println("加密字节数：" + inputLength);
		// 标识
		int offSet = 0;
		byte[] resultBytes = {};
		byte[] cache = {};
		while (inputLength - offSet > 0) {
			if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(inputArray, offSet, MAX_ENCRYPT_BLOCK);
				offSet += MAX_ENCRYPT_BLOCK;
			} else {
				cache = cipher.doFinal(inputArray, offSet, inputLength - offSet);
				offSet = inputLength;
			}
			resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
			System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
		}
		return Base64.getEncoder().encodeToString(resultBytes);
	}

	public String encodeByPublicKey(String body) throws Exception {
		Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] inputArray = body.getBytes();
		int inputLength = inputArray.length;
//		System.out.println("加密字节数：" + inputLength);
		// 标识
		int offSet = 0;
		byte[] resultBytes = {};
		byte[] cache = {};
		while (inputLength - offSet > 0) {
			if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(inputArray, offSet, MAX_ENCRYPT_BLOCK);
				offSet += MAX_ENCRYPT_BLOCK;
			} else {
				cache = cipher.doFinal(inputArray, offSet, inputLength - offSet);
				offSet = inputLength;
			}
			resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
			System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
		}
		return Base64.getEncoder().encodeToString(resultBytes);
	}

	public String decodeByPublicKey(String body) throws Exception {
		Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		return decryptByPublicKey(body);
	}

	public String decodeByPrivateKey(String body) throws Exception {
		Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return decryptByPrivateKey(body);
	}

	public String decryptByPublicKey(String encryptedStr) {
		try {
			// 对公钥解密
			byte[] privateKeyBytes = publicKey.getEncoded();
			// 获得公钥
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(privateKeyBytes);
			// 获得待解密数据
			byte[] data = decryptBase64(encryptedStr);
			KeyFactory factory = KeyFactory.getInstance("RSA");
			PublicKey publicKey = factory.generatePublic(keySpec);
			// 对数据解密
			Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			// 返回UTF-8编码的解密信息
			int inputLen = data.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段解密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
					cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_DECRYPT_BLOCK;
			}
			byte[] decryptedData = out.toByteArray();
			out.close();
			return new String(decryptedData, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}



	/**
	 * 私钥解密
	 *
	 * @param encryptedStr
	 * @return
	 */
	public String decryptByPrivateKey(String encryptedStr) {
		try {
			// 对私钥解密
			byte[] privateKeyBytes = privateKey.getEncoded();
			// 获得私钥
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
			// 获得待解密数据
			byte[] data = decryptBase64(encryptedStr);
			KeyFactory factory = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = factory.generatePrivate(keySpec);
			// 对数据解密
			Cipher cipher = Cipher.getInstance(factory.getAlgorithm());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			// 返回UTF-8编码的解密信息
			int inputLen = data.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段解密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
					cache = cipher.doFinal(data, offSet, MAX_DECRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(data, offSet, inputLen - offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_DECRYPT_BLOCK;
			}
			byte[] decryptedData = out.toByteArray();
			out.close();
			return new String(decryptedData, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * BASE64 解码
	 *
	 * @param key 需要Base64解码的字符串
	 * @return 字节数组
	 */
	public static byte[] decryptBase64(String key) {
		return Base64.getDecoder().decode(key);
	}


	public static void main(String[] args) throws Exception {
		RSAUtils4Mall rsaUtils = new RSAUtils4Mall();
		rsaUtils.getKeys();
//		rsaUtils.geneKeys();
		String plain = "{\"Authorization\":\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1OTY3OTA5NjYsInRlcm1fdHlwZSI6bnVsbCwidXNlcl9uYW1lIjoiMTI4Nzc1Njc1NTIxMTY0OTA2NiIsImp0aSI6IjM5NjM2ZDBiLTcxOGUtNGIxYS04ZTZiLWExNDc4ZGMyYThhZCIsImNsaWVudF9pZCI6ImZyb250ZW5kIiwic2NvcGUiOlsiYWxsIl19.WRD-T4tpXLpAA7VaSNCdmVdh0cjqVlLI-Vq0lJw9QZI\",\"timestamp\":\"20200811 135244\"}";
		String encryptData = rsaUtils.encodeByPublicKey(plain);
		String encryptData2 = rsaUtils.encodeByPrivateKey(plain);
		System.out.println("公钥加密："+encryptData);
		System.out.println("私钥加密："+encryptData2);
		System.out.println("私钥解密后:" + rsaUtils.decodeByPrivateKey(encryptData));
		System.out.println("公钥解密后:" + rsaUtils.decodeByPublicKey(encryptData2));
		String encodes = rsaUtils.encodeByPrivateKey("123456");
		System.out.println("加密前:" + 123456);
		System.out.println("公钥:" + Base64.getEncoder().encodeToString(RSAUtils4Mall.publicKey.getEncoded()));
		System.out.println("私钥:" + Base64.getEncoder().encodeToString(RSAUtils4Mall.privateKey.getEncoded()));
		System.out.println("私钥加密后:" + encodes);
		System.out.println("公钥加密后:" + rsaUtils.encodeByPublicKey("123456"));
		System.out.println("公钥解密后:" + rsaUtils.decodeByPublicKey(encodes));
		String plain2 = "fELmp2H3m%2BhY9DnZHj6QmgxVqVXGTDDeCXxfYNDM2ow6E0hVGQ%2FjT%2FiSMKTxJoXTJS1I2XouybUczzBppF6fDUTwlyNIFViI9wO2ErfEEnikwc9O%2Bgt1SuOScZjLVpkvrw0RrcXzhg1n2rqqJuzYwG0lvrpIAg2haJmyzgiCn4oRiBHexLNQ%2BLcJdYu%2FN9BTndk1ytuPX4osiue1kGBrqKMW3zX97m7%2FRdqeS90OyW29C5tcDq80eWQQXteh0B2L%2B2wgEwiGMLnKw4EOdTYSyJ1k9tQQ90JA4gj%2BlwEgyQ3yB7Gj0ZrqplxoSxJ8NnNbNtHRfGKbB60rvMIHD4Z7SpQZRHsZyihT3CdAdwOzvu91ZSq52EQOLPE0EkMfHDtent1ExOliB950YLJFn%2BHKQTuixDyUusUzABhpOJfp1bxjJDcJPlWoMWoe%2B8%2B2mzMIiVsvKKsgIZivsLm%2FWnNkKr0F9wBJ1RS6yuWr0z7yzg3%2BxfUQyNyPSJXdv2cOq%2B3G";
		plain2 = URLDecoder.decode(plain2,"UTF-8");
		System.out.println(plain2);
		System.out.println(rsaUtils.decodeByPublicKey(plain2));
	}
}
