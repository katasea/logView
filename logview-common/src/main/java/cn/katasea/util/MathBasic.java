package cn.katasea.util;
import java.math.BigDecimal;

/**
 ****************************************************************************************
 * 										精度计算基类
 * @author 工具类
 ****************************************************************************************
 */
public class MathBasic {
	private String null2zero(String str) {
		String result = "0";
		try {
			if(!CommonUtil.isEmpty(str)) {
				result = String.valueOf(Double.parseDouble(str));
			}
		}catch (Exception e) {
			throw e;
		}
		return result;
	}
	
	public String add(String v1, String v2) {
		v1 = null2zero(v1);
		v2 = null2zero(v2);
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.add(b2).toString();
	}
	
	public String add2(String[] params) {
		String v0 = null2zero(params[0]);
		BigDecimal b0 = new BigDecimal(v0);
		for(int i=1;i<params.length;i++) {
			String v1 = null2zero(params[i]);
			b0 = b0.add(new BigDecimal(v1));
		}
		return b0.toString();
	}
	
	public String sub(String v1, String v2) {
		v1=null2zero(v1);
		v2=null2zero(v2);
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.subtract(b2).toString();
	}
	
	public String sub2(String[] params) {
		String v0 = null2zero(params[0]);
		BigDecimal b = new BigDecimal(v0);
		for(int i=1;i<params.length;i++){
			String v1 = null2zero(params[i]);
			b = b.subtract(new BigDecimal(v1));
		}
		return b.toString();
	}
	
	public String mul(String v1, String v2){
		v1=null2zero(v1);
		v2=null2zero(v2);
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).toString();
	}
	
	public String mul2(String[] params) {
		String v0 = null2zero(params[0]);
		BigDecimal b1 = new BigDecimal(v0);
		for(int i=1;i<params.length;i++){
			String v1 = null2zero(params[i]);
			b1 = b1.multiply(new BigDecimal(v1));
		}
		return b1.toString();
	}
	
	public String div(String v1, String v2, int scale) {
		if(scale <0){
			throw new IllegalArgumentException("The Scale must be a positive integer or zero");
		}
		v1 = null2zero(v1);
		v2 = null2zero(v2);
		if(Double.parseDouble(CommonUtil.nullToZero(v2))==0.0){
			return "0";
		}
		else {
			BigDecimal b1 = new BigDecimal(v1);
			BigDecimal b2 = new BigDecimal(v2);
			return b1.divide(b2,scale, BigDecimal.ROUND_HALF_UP).toString();
		}
	}
}
