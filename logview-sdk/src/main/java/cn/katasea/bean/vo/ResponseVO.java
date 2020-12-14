package cn.katasea.bean.vo;

import cn.katasea.bean.dto.HandlerStateDTO;
import cn.katasea.myenum.HandlerType;

import java.io.Serializable;

/**
 * View Object of Response
 *
 * @author katasea
 * @date 2019/5/16 15:05
 */
public class ResponseVO<T> extends HandlerStateDTO implements Serializable{
	private static final long serialVersionUID = -7602478003003767252L;

	private String signType;
	private String encryptType;
	private String timestamp;
	private String sign;

	//response of business
	private T bizObj;
	private String bizContent;
	public ResponseVO() {
		//创建的时候默认是成功的
		super();
	}

	public ResponseVO(RequestVO requestVO) {
		this.setSign(requestVO.getSign());
		this.setEncryptType(requestVO.getEncryptType());
		this.setRetInfo(HandlerType.SUCCESS);
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
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

	public T getBizObj() {
		return bizObj;
	}

	public void setBizObj(T bizObj) {
		this.bizObj = bizObj;
	}

	public String getBizContent() {
		return bizContent;
	}

	public void setBizContent(String bizContent) {
		this.bizContent = bizContent;
	}
}
