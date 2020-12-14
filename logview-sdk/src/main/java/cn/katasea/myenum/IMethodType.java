package cn.katasea.myenum;

/**
 * @author katasea
 * 2019/5/23 13:32
 */
public enum IMethodType {
	CHARGE("aio/payment/charge");
	private String code;

	IMethodType(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
