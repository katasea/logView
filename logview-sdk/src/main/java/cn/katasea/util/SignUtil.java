package cn.katasea.util;


import cn.katasea.myenum.SignType;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * sign工具类
 **/
public class SignUtil {
    public static String sortKey(Map<String, String> map, String key) {
        if (map == null)
            return "";
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (CommonUtil.isNotEmpty(getObjString(entry.getValue()))) {
                list.add(entry.getKey() + "=" + entry.getValue() + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        sb.append(key);
        String result = sb.toString();
        return result;
    }

    public static String getSign(Map<String, String> map, String key, String signType) {
        String result = sortKey(map, key);
        if (CommonUtil.isEmpty(signType) || SignType.MD5.toString().equals(signType)) {
            result = MD5Util.encrypt(result);
        }
//        else if (SignType.RSA.toString().equals(signType)) {
            // TODO RSA 签名
//        }
        return result;
    }

    public static String getSignMD5(Map<String, String> map, String key) {
        return getSign(map, key, SignType.MD5.toString());
    }

    private static String getObjString(Object object) {
        return (object == null ? "" : (String) object);
    }


    /**
     *  利用java原生的摘要实现SHA256加密
     */
    public static String getSHA256Str(String str, String charset){
        MessageDigest messageDigest;
        String encodeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes(charset));
            encodeStr = byte2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodeStr;
    }
    /**
     * 将byte转为16进制
     */
    private static String byte2Hex(byte[] bytes){
        StringBuilder stringBuilder = new StringBuilder();
        String temp = null;
	    for (byte aByte : bytes) {
		    temp = Integer.toHexString(aByte & 0xFF);
		    if (temp.length() == 1) {
			    //1得到一位的进行补0操作
			    stringBuilder.append("0");
		    }
		    stringBuilder.append(temp);
	    }
        return stringBuilder.toString();
    }
}
