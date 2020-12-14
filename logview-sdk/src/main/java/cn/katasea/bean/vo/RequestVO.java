package cn.katasea.bean.vo;

import java.io.Serializable;

/**
 * View Object of Request Param
 *
 * @author katasea
 * @date 2019/5/16 15:05
 */
public class RequestVO<T> implements Serializable{
	private static final long serialVersionUID = -7602478003003767252L;

	private String appId;
	private String appSecret;
	private String method;
	private String signType;
	private String sign;
	private String encryptType;
	private String timestamp;
	private String version;

	private String bizContent;
	private T bizObj;


	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getAppId() {
		return appId;
	}

	
	public void setAppId(String appId) {
		this.appId = appId;
	}

	
	public String getMethod() {
		return method;
	}

	
	public void setMethod(String method) {
		this.method = method;
	}

	
	public String getSignType() {
		return signType;
	}

	
	public void setSignType(String signType) {
		this.signType = signType;
	}

	
	public String getSign() {
		return sign;
	}

	
	public void setSign(String sign) {
		this.sign = sign;
	}

	
	public String getEncryptType() {
		return encryptType;
	}

	
	public void setEncryptType(String encryptType) {
		this.encryptType = encryptType;
	}

	
	public String getTimestamp() {
		return timestamp;
	}

	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	
	public String getVersion() {
		return version;
	}

	
	public void setVersion(String version) {
		this.version = version;
	}

	public String getBizContent() {
		return bizContent;
	}

	public void setBizContent(String bizContent) {
		this.bizContent = bizContent;
	}

	public T getBizObj() {
		return bizObj;
	}

	public void setBizObj(T bizObj) {
		this.bizObj = bizObj;
	}
}
