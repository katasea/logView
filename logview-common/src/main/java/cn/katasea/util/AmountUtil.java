
package cn.katasea.util;
import cn.katasea.exception.PayBusinessException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AmountUtil {
	public static final BigDecimal ZERO = new BigDecimal("0.00");
	public static final String CURRENCY_FEN_REGEX = "\\-?[0-9]+";

	private static final String AMT_FLAG = "^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){1,4})?$";
	private static final String DEFAULT_AMT = "0.00";
	
    public static final String MONEY_PRECISION = "0.00";

	public static String formatAmount(BigDecimal bd) {
		if (CommonUtil.isEmpty(bd)) {
			return "0.00";
		}
		if (bd.compareTo(new BigDecimal(0)) == 0) {
			return "0.00";
		}
		DecimalFormat myformat = new DecimalFormat();
		myformat.applyPattern("##,###.00");
		return myformat.format(bd);
	}

	public static String formatAmount(String amt, String format)throws PayBusinessException {
		try {
			if (CommonUtil.isEmpty(amt)) {
				return "0.00";
			}
			
			BigDecimal amt_bd = new BigDecimal(amt);
			if (amt_bd.compareTo(new BigDecimal(0)) == 0) {
				return "0.00";
			}
			DecimalFormat myformat = new DecimalFormat();
			myformat.applyPattern(format);
			return myformat.format(amt_bd);
		} catch (Exception e) {
			System.out.println("formatAmount error: amt=" + amt + ";format=" + format);
			e.printStackTrace();
			throw  new PayBusinessException("金额转换异常",e);
		}
	}

	public static String sumAmount(String s1, String s2) {
		if (CommonUtil.isEmpty(s1)) {
			s1 = DEFAULT_AMT;
		}
		if (CommonUtil.isEmpty(s2)) {
			s2 = DEFAULT_AMT;
		}
		Pattern pattern = Pattern.compile(AMT_FLAG);
		Matcher matcher = pattern.matcher(s1);
		Matcher matcher2 = pattern.matcher(s2);
		if (!matcher.matches()) {
			s1 = DEFAULT_AMT;
		}
		if (!matcher2.matches()) {
			s2 = DEFAULT_AMT;
		}
		BigDecimal bd1 = new BigDecimal(s1);
		BigDecimal bd2 = new BigDecimal(s2);
		BigDecimal bd3 = bd1.add(bd2);
		return bd3.toString();
	}

	public static String getPercent(double baiy, double baiz) {
		String baifenbi = "0%";// 接受百分比的值
		/*
		 * double baiy=y*1.0; double baiz=z*1.0;
		 */
		double fen = baiy / baiz;
		if (fen >= 1) {
			return "100%";
		}
		// NumberFormat nf = NumberFormat.getPercentInstance(); 注释掉的也是一种方法
		// nf.setMinimumFractionDigits( 2 ); 保留到小数点后几位
		DecimalFormat df1 = new DecimalFormat("##%"); // ##.00%
														// 百分比格式，后面不足2位的用0补齐
		// baifenbi=nf.format(fen);
		baifenbi = df1.format(fen);
		return baifenbi;

	}

	public static String changeY2F(String amount) {
		String currency = amount.replaceAll("\\$|\\￥|\\,", ""); // 处理包含, ￥
																// 或者$的金额
		int index = currency.indexOf(".");
		int length = currency.length();
		Long amLong = 0l;
		if (index == -1) {
			amLong = Long.valueOf(currency + "00");
		} else if (length - index >= 3) {
			amLong = Long.valueOf((currency.substring(0, index + 3)).replace(".", ""));
		} else if (length - index == 2) {
			amLong = Long.valueOf((currency.substring(0, index + 2)).replace(".", "") + 0);
		} else {
			amLong = Long.valueOf((currency.substring(0, index + 1)).replace(".", "") + "00");
		}
		return amLong.toString();
	}

	public static String formatAmtByNet(String amt){
		if(CommonUtil.isEmpty(amt)){
			return null;
		}
		String str = changeY2F(amt);
		StringBuffer sb = new StringBuffer();
		if(str.length() < 12){
			int strIndex = 12 - str.length();
			for(int i=0; i<strIndex; i++){
				sb.append("0");
			}
		}else if(str.length() > 12){
			return null;
		}
		return sb.toString() + str;
	}
	
	public static String changeF2Y(String amount) throws PayBusinessException {
		if(CommonUtil.isEmpty(amount)){
			amount="0";
		}
		if (!amount.matches(CURRENCY_FEN_REGEX)) {
			throw new PayBusinessException("金额格式有误");
		}
		return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString();
	}

	public static String getMathAbs(String amt){
		if(CommonUtil.isEmpty(amt)){
			return amt;
		}
		if(amt.indexOf("-")==0){
			return amt.substring(1);
		}
		return amt;
	}

	public static String subtract(String num1, String num2, boolean abs) throws PayBusinessException {
		try {
			BigDecimal bd_num1 = new BigDecimal(num1);
			BigDecimal bd_num2 = new BigDecimal(num2);
			BigDecimal bd_target = bd_num1.subtract(bd_num2);
			if(abs){
				bd_target = bd_target.abs();
			}
			bd_target = bd_target.setScale(2, RoundingMode.HALF_UP);
			return bd_target.toString();
		} catch (Exception e) {
			throw new PayBusinessException("两数相减时计算异常");
		}
	}
	
	public static BigDecimal subtract2BigDecimal(String num1, String num2, boolean abs) throws PayBusinessException {
		try {
			BigDecimal bd_num1 = new BigDecimal(num1);
			BigDecimal bd_num2 = new BigDecimal(num2);
			BigDecimal bd_target = bd_num1.subtract(bd_num2);
			if(abs){
				bd_target = bd_target.abs();
			}
			bd_target = bd_target.setScale(2, RoundingMode.HALF_UP);
			return bd_target;
		} catch (Exception e) {
			throw new PayBusinessException("两数相减时计算异常");
		}
	}
}
