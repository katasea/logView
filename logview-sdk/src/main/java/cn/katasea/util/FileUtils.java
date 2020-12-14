package cn.katasea.util;

import org.apache.commons.codec.binary.Base64;
import java.io.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


public class FileUtils {
	/**
	 * 将指定的对象写入指定的文件中
	 * 
	 * @param file
	 *            指定写入的文件
	 * @param objs
	 *            要写入的对象
	 */
	public static void doObjToFile(String file, Object[] objs) {
		ObjectOutputStream oos = null;
		try {
			FileOutputStream fos = new FileOutputStream(file);
			oos = new ObjectOutputStream(fos);
			for (int i = 0; i < objs.length; i++) {
				oos.writeObject(objs[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				oos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void doObjToFile(String privateFile, String publicFile, Object[] objs) {
		ObjectOutputStream oosPrivate = null;
		ObjectOutputStream oosPublic = null;

		try {
			FileOutputStream fosPrivate = new FileOutputStream(privateFile);
			FileOutputStream fosPublic = new FileOutputStream(publicFile);
			oosPrivate = new ObjectOutputStream(fosPrivate);
			oosPublic = new ObjectOutputStream(fosPublic);
			for (int i = 0; i < objs.length; i++) {
				if (objs[i] instanceof PublicKey) {
					oosPublic.writeObject(objs[i]);
				} else if (objs[i] instanceof PrivateKey) {
					oosPrivate.writeObject(objs[i]);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				oosPrivate.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public static PublicKey getPublicKey(String publicFile) throws Exception {
		FileInputStream fis = new FileInputStream(publicFile);
		InputStreamReader inputReader = new InputStreamReader(fis);
		BufferedReader bufferReader = new BufferedReader(inputReader);
        // 读取一行
        String line = null;
        StringBuffer strBuffer = new StringBuffer();
        while ((line = bufferReader.readLine()) != null)
        {
        	if(line.charAt(0)=='-'){  
                continue;  
            }else{
                strBuffer.append(line);
                strBuffer.append('\r');
            }
        }
        fis.close();
        inputReader.close();
        bufferReader.close();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(strBuffer.toString()));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);
        return publicKey;
	}
	
	public static PrivateKey getPrivateKey(String privateFile) throws Exception {
		FileInputStream fis = new FileInputStream(privateFile);
		InputStreamReader inputReader = new InputStreamReader(fis);
		BufferedReader bufferReader = new BufferedReader(inputReader);
        // 读取一行
        String line = null;
        StringBuffer strBuffer = new StringBuffer();
        while ((line = bufferReader.readLine()) != null)
        {
            if(line.charAt(0)=='-'){  
                continue;  
            }else{
                strBuffer.append(line);
                strBuffer.append('\r');
            }
        }
        fis.close();
        inputReader.close();
        bufferReader.close();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(strBuffer.toString()));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return privateKey;
	}
	
	public static void wiriteKeyToFile(String privateFile, String publicFile, String[] strings)
			throws IOException {
		FileOutputStream fosPrivate = new FileOutputStream(privateFile);
		FileOutputStream fosPublic = new FileOutputStream(publicFile);
		fosPrivate.write(strings[0].getBytes());
		fosPublic.write(strings[1].getBytes());
		fosPrivate.close();
		fosPublic.close();
	}
}
