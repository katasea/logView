package cn.katasea.myenum;

import cn.katasea.bean.dto.HandlerStateDTO;

/**
 * 业务状态和信息枚举类
 *
 * @author 陈富强
 */
public enum HandlerType {

	SUCCESS("0000", "处理成功"),
	UNKNOWN("9999", "未知错误"),
	NO_LOGIN("9998", "未登录"),

	REQ_POST_TEXT_NULL("REQ_POST_TEXT_NULL", "请求报文为空！"),
	REQ_POST_TEXT_ILLEGAL("REQ_POST_TEXT_ILLEGAL", "请求报文未按格式要求！"),
	REQ_APP_ID_EMPTY("REQ_APP_ID_EMPTY", "请求的应用ID为空！"),
	REQ_APP_ID_ILLEGAL("REQ_APP_ID_ILLEGAL", "请求的应用ID不合法！"),
	REQ_TIMESTAMP_EMPTY("REQ_TIMESTAMP_EMPTY", "请求的时间戳为空！"),
	REQ_DECRYPT_FAIL("REQ_DECRYPT_FAIL", "解密失败！"),
	REQ_CHECK_SIGN_FAIL("REQ_CHECK_SIGN_FAIL", "验签失败！"),
	REQ_METHOD_EMPTY("REQ_METHOD_EMPTY", "请求的方法为空！"),
	REQ_PAYCONFIG_NULL("REQ_PAYCONFIG_NULL", "支付配置参数表[PAYCONFIG]为空！"),
	REQ_CHANNEL_TYPE_ERROR("REQ_CHANNEL_TYPE_ERROR", "支付渠道非法，未找到对应支付渠道！"),

	SRV_SWITCH_EMPTY("SRV_SWITCH_EMPTY", "系统内部错误，未找到对应前置服务！"),

	SYSTEM_ERROR("SYSTEM_ERROR", "系统内部错误"),
	OUT_SYSTEM_ERROR("OUT_SYSTEM_ERROR", "系统外部错误");


	private String retCode;
	private String retMsg;

	HandlerType(String retCode, String retMsg) {
		this.retCode = retCode;
		this.retMsg = retMsg;
	}

	public String getRetCode() {
		return retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public static <T> boolean isSuccessful(HandlerStateDTO responseVO) {
		return responseVO.getRetCode().equalsIgnoreCase(HandlerType.SUCCESS.getRetCode());
	}
}
