package cn.katasea.bean.dto;

import cn.katasea.myenum.HandlerType;

import java.io.Serializable;

/**
 * 业务处理状态
 *
 * @author katasea
 */
public class HandlerStateDTO implements Serializable {
	private static final long serialVersionUID = -243551697818046991L;

	private String retCode;//处理结果状态码 参考 BusinessRetMsgEnum
	private String retMsg;//处理结果消息 []

	public HandlerStateDTO() {
		//创建的时候默认是成功的
		this.retCode = HandlerType.SUCCESS.getRetCode();
		this.retMsg = HandlerType.SUCCESS.getRetMsg();
	}
	public HandlerStateDTO(HandlerType handlerType) {
		//创建的时候默认是成功的
		this.retCode = handlerType.getRetCode();
		this.retMsg = handlerType.getRetMsg();
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public void setRetInfo(HandlerStateDTO handlerStateDTO) {
		//创建的时候默认是成功的
		this.retCode = handlerStateDTO.getRetCode();
		this.retMsg = handlerStateDTO.getRetMsg();
	}
	public void setRetInfo(HandlerType handlerType) {
		HandlerStateDTO handlerStateDTO = new HandlerStateDTO(handlerType);
		setRetInfo(handlerStateDTO);
	}
}
