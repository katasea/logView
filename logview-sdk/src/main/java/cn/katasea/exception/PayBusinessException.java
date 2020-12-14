package cn.katasea.exception;

import cn.katasea.myenum.HandlerType;

public class PayBusinessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String errorCode;
	private String errorMessage;

	@Override
	public String toString() {
		return "错误代码："+getErrorCode()+"，错误信息："+getErrorMessage();
	}

	public String getErrorCode() {
		return errorCode;
	}



	public String getErrorMessage() {
		return errorMessage;
	}



	public PayBusinessException(String errorCode, String errorMessage) {
		super(errorMessage);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	public PayBusinessException(HandlerType handlerType) {
		super(handlerType.getRetMsg());
		this.errorCode = handlerType.getRetCode();
		this.errorMessage = handlerType.getRetMsg();
	}



	public PayBusinessException(String errorMessage) {
		super(errorMessage);
		this.errorCode = HandlerType.SYSTEM_ERROR.getRetCode();
		this.errorMessage = errorMessage;
	}

	public PayBusinessException(String errorMessage, Exception e) {
		super(errorMessage,e);
		this.errorMessage = errorMessage;
	}



	public PayBusinessException(Throwable e) {
		super(e);
	}
	
	

}
