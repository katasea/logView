package cn.katasea.util;


import cn.katasea.myenum.EncryptType;
import org.apache.commons.codec.binary.Base64;
import javax.crypto.Cipher;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 安全工具类： 主要功能有加解密、签名、验签等
 *
 * @author sun
 * @version 1.0.0
 */

public class SecurityUtil {

    /**
     * 通过公钥完成加密，再通过base64加密
     *
     * @param data          待加密的数据
     * @param publicKeyFile 公钥路径
     * @return 公钥加密后的base64字符串
     * @throws Exception
     * @author sun
     * @date 2014年6月20日
     */
    public static String encryptByPublicKey(String data, String publicKeyFile) throws Exception {
        PublicKey publicKey = FileUtils.getPublicKey(publicKeyFile);
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] byteContext = cipher.doFinal(data.getBytes("UTF-8"));
        String encrypt = new String(Base64.encodeBase64String(byteContext));
        return encrypt;
    }

    /**
     * 通过私钥完成密文解密
     *
     * @param data           待解密的base64密文
     * @param privateKeyFile 私钥文件路径
     * @return
     * @throws Exception
     * @author sun
     * @date 2014年6月20日
     */
    public static String decryptByPrivateKey(String data, String privateKeyFile) throws Exception {
        PrivateKey privateKey = FileUtils.getPrivateKey(privateKeyFile);
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] byteData = cipher.doFinal(Base64.decodeBase64(data));
        return new String(byteData);
    }

    /**
     * 通过AES加密方式完成文本加密
     *
     * @param data 待加密文本
     * @return 加密后文本
     * @throws Exception
     * @author sun
     * @date 2014年6月20日
     */
    public static String encryptByAES(String data, String appSecret, String appId) throws Exception {
        // String appSecret = CommonConstant.propertiesMap.get("appSecret");
        String newPassword = AESUtil.encrypt(appSecret, appId);
        return AESUtil.encrypt(data, newPassword);
    }

    /**
     * 通过DES加密方式完成文本加密
     *
     * @param data 待加密文本
     * @return 加密后文本
     * @throws Exception
     */
    public static String encryptByDES(String data, String appSecret, String appId) throws Exception {
        // String appSecret = CommonConstant.propertiesMap.get("appSecret");
        String newPassword = DESUtil.encrypt(appSecret, appId.substring(0, 8));
        return DESUtil.encrypt(data, newPassword.substring(0, 8));
    }

    /**
     * 通过DES解密方式完成密文解密
     *
     * @param data 待加密文本
     * @return 加密后文本
     * @throws Exception
     */
    public static String decryptByDES(String data, String appSecret, String appId) throws Exception {
        // String appSecret = CommonConstant.propertiesMap.get("appSecret");
        String newPassword = DESUtil.encrypt(appSecret, appId.substring(0, 8));
        // 传输过程加号丢失问题
        data = data.replace(" ", "+");
        return DESUtil.decrypt(data, newPassword.substring(0, 8));
    }

    /**
     * 通过AES解密方式完成密文解密
     *
     * @param data 待加密文本
     * @return 加密后文本
     * @throws Exception
     */
    public static String decryptByAES(String data, String appSecret, String appId) throws Exception {
        // imei = getPassword(imei);
        // String appSecret = CommonConstant.propertiesMap.get("appSecret");
        String newPassword = AESUtil.encrypt(appSecret, appId);
        return AESUtil.decrypt(data, newPassword);
    }

    /**
     * 通过SM4加密方式完成文本加密
     *
     * @param data 待加密文本
     * @return 加密后文本
     * @throws Exception
     */
    public static String encryptBySM4(String data, String appSecret, String appId) throws Exception {
        String newPassword = SM4Utils.encryptData_ECB(appSecret, appId.substring(0, 16), false);
        return SM4Utils.encryptData_ECB(data, newPassword.substring(0, 16), false);
    }

    /**
     * 通过SM4解密方式完成密文解密
     *
     * @param data 待解密文本
     * @return
     * @throws Exception
     */
    public static String decryptBySM4(String data, String appSecret, String appId) throws Exception {
        String newPassword = SM4Utils.encryptData_ECB(appSecret, appId.substring(0, 16), false);
        return SM4Utils.decryptData_ECB(data, newPassword.substring(0, 16), false);
    }



    /*
     * 字符串加密
     *
     * @author sun
     *
     * @date 2016年10月13日
     *
     * @param text 待加密的字符串
     *
     * @param encryptType 加密类型
     *
     * @return
     *
     * @throws Exception
     */
    public static String encrypt(String text, String encryptType, String appSecret, String appId) throws Exception {
        if (encryptType == null || "".equals(encryptType) || EncryptType.Plain.toString().equals(encryptType))
            return text;
        else if (EncryptType.AES.toString().equals(encryptType))
            return SecurityUtil.encryptByAES(text, appSecret, appId);
        else if (EncryptType.DES.toString().equals(encryptType))
            return SecurityUtil.encryptByDES(text, appSecret, appId);
        else if (EncryptType.SM4.toString().equals(encryptType))
            return SecurityUtil.encryptBySM4(text, appSecret, appId);
        else
            return text;
    }

    /*
     * 字符串加密
     *
     * @author sun
     *
     * @date 2016年10月13日
     *
     * @param text 待加密的字符串
     *
     * @param encryptType 加密类型
     *
     * @return
     *
     * @throws Exception
     */
    public static String decrypt(String text, String encryptType, String appSecret, String appId) throws Exception {
        if (encryptType == null || "".equals(encryptType) || EncryptType.Plain.toString().equals(encryptType))
            return text;
        else if (EncryptType.AES.toString().equals(encryptType))
            return SecurityUtil.decryptByAES(text, appSecret, appId);
        else if (EncryptType.DES.toString().equals(encryptType))
            return SecurityUtil.decryptByDES(text, appSecret, appId);
        else if (EncryptType.SM4.toString().equals(encryptType))
            return SecurityUtil.decryptBySM4(text, appSecret, appId);
        else
            return text;
    }

    public static void main(String[] args) throws Exception {
        String APP_ID = "1A3VL0KVK0000B020A0A0000CC3F48AD";
        String APP_SECRET = "1A3VL0KVE0010B010A0A0000277BDC91";
        String s = "{\"body\":\"测试下单接口body\",\"channel\":\"WX_WAP\",\"chargeAmt\":\"1.11\",\"clientIp\":\"120.36.144.106\",\"description\":\"测试下单接口描述\",\"notifyUrl\":\"http://test.xmzhyc.cn:8080/notify_url.php\",\"optional\":\"订单附加信息\",\"outChargeNo\":\"OC02220016\",\"outChargeTime\":\"20161031160405133\",\"returnUrl\":\"http://test.xmzhyc.cn:8080/call_back_url.php\",\"showUrl\":\"http://test.xmzhyc.cn:8080/\",\"subject\":\"测试下单接口\"}";
        String decryptData = encryptByAES(s, APP_SECRET, APP_ID);


//        RequestParams requestParams = new RequestParams();
//        requestParams.setSignType("MD5");
//        requestParams.setEncryptType("AES");
//        requestParams.setTransType("app.merch.login");
//        requestParams.setTimestamp("201612021010100");
//        requestParams.setVersion("1.0");
//        Map param = new HashMap();
//        param.put("userName", "test");
//        param.put("loginPwd", "123");
//        requestParams.setParam(param);
//        System.out.println("原始请求报文内容：" + JSON.toJSONString(requestParams));
//        String sign = Signature.createSign(requestParams, APP_SECRET);
//        requestParams.setSign(sign);
//        System.out.println("内部参数param加密之前：" + JSON.toJSONString(requestParams.getParam()));
//        String encryptData = SecurityUtil.encrypt(JSONObject.toJSONString(requestParams.getParam()), requestParams.getEncryptType(), APP_SECRET, APP_ID);
//        requestParams.setEncryptData(encryptData);
//        System.out.println("内部参数param加密之后：" + requestParams.getEncryptData());
//        // 清空明文
//        requestParams.setParam(null);
//        System.out.println("最终发送的请求报文内容：" + JSON.toJSONString(requestParams));
//
//        //解密
//        String decryptData = SecurityUtil.decrypt(encryptData, requestParams.getEncryptType(), APP_SECRET, APP_ID);
        System.out.println("解密结果：" + decryptData);
//		HttpUtil.request("http://localhost:8088/onepay-web/appweb/merchapi", JSON.toJSONString(requestParams), URLType.HTTP);
    }
}
