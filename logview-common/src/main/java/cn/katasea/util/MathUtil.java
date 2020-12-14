package cn.katasea.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * ***************************************************************************************
 * 解析字符串并计算字符串的结果
 *
 * @author 工具类
 * ***************************************************************************************
 */
public class MathUtil {
	private static MathBasic ps = new MathBasic();
	public static Logger logger = LoggerFactory.getLogger(MathUtil.class);
	/**
	 * 调用入口方法
	 *
	 * @param str 传入字符串计算表达式
	 * @return 计算完的结果
	 */
	public static String cacComplex(String str, int scale) {
		//去掉空格否则可能会出错。
		str = str.replaceAll(" ", "");
		if (str == null || "".equals(str)) {
			return "0";
		} else {
			str = str.replace("null", "0");
			if (str.charAt(0) == '-') {
				str = "0" + str;
			}
		}

		int has = str.indexOf("[");
		int have = str.indexOf("{");
		if (has != -1 || have != 1) {
			str = str.replaceAll("[\\[\\{]", "(").replaceAll("[\\]\\}]", ")");

		}
		int c1 = str.lastIndexOf('(');
		if (c1 == -1) {
			return myTransetFunction(cac(str,scale),scale);
		}
		int cr = str.indexOf(')', c1);
		String left = "";
		String right = "";
		String middle = "";
		try {
			left = str.substring(0, c1);
			right = str.substring(cr + 1);
			middle = str.substring(c1 + 1, cr);
			String result = myTransetFunction(cacComplex(left + cac(middle,scale) + right,scale),scale);
			BigDecimal temp = new BigDecimal(result);
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return "0";
		}
	}

	private static String cac(String str, int scale) {
		if (str.equals("")) return "0";
		int m1 = str.indexOf('*');
		int d1 = str.indexOf('/');
		if (m1 == -1 && d1 == -1) {
			return myTransetFunction(cacNoMD(str),scale);
		}
		int index = 0;
		if (m1 < d1) {
			index = m1 == -1 ? d1 : m1;
		} else {
			index = d1 == -1 ? m1 : d1;
		}

		String left = str.substring(0, index);
		//获取符号左边第一个数字
		String m11 = lastNumber(left);
		left = left.substring(0, left.length() - m11.length());
		String right = str.substring(index + 1);
		//获取符号右边第一个数字
		String m2 = firstNumber(right);
		right = right.substring(m2.length());
		String tmp = "0";
		try {
			if (index == m1) {
				tmp = ps.mul(m11, m2);
			} else if (index == d1) {
				tmp = ps.div(m11, m2, scale);
			}
		} catch (Exception e) {
			e.printStackTrace();
			tmp = "0";
		}
		tmp = myTransetFunction(tmp,scale);
		return myTransetFunction(cac(left + tmp + right,scale),scale);
	}

	private static String myTransetFunction(String tmp, int scale) {
		if (tmp == null || "".equals(tmp)) {
			tmp = "0";
		}
		try {
			if (tmp.contains("E")) {
				String t1 = tmp.substring(0, tmp.indexOf("E"));
				String t2 = tmp.substring(tmp.indexOf("E") + 1);
				if ("0".equals(t1)) {
					tmp = "0";
				} else {
					tmp = String.valueOf(Math.pow(Double.parseDouble(t1), Double.parseDouble(t2)));
				}
			}
			BigDecimal temp = new BigDecimal(tmp);
			temp.setScale(scale, BigDecimal.ROUND_HALF_UP);
			String pattern = "0";
			if(scale>0) {
				pattern = pattern + ".";
				for(int i=0 ; i<scale; i++) {
					pattern += "0";
				}
			}

			java.text.DecimalFormat myformat=new java.text.DecimalFormat(pattern);
			return myformat.format(temp);
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}

	}

	private static String lastNumber(String str) {
		StringBuilder sb = new StringBuilder();
		for (int i = str.length() - 1; i >= 0; i--) {
			char c = str.charAt(i);
			if (!Character.isDigit(c) && c != '.') {
				//Mod By CFQ
				if (i == 0) {
					sb.append(c);
				} else if ((i - 1) >= 0) {
					if (!Character.isDigit(str.charAt(i - 1)) && str.charAt(i - 1) != '.') {
						sb.append(c);
					}
				}
				///
				break;
			}
			sb.append(c);
		}
		return sb.reverse().toString();
	}

	private static String firstNumber(String str) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (char c : str.toCharArray()) {
			i++;
			//支持  2*-1 这样的操作
			//如果第一个字符是-号，就是表示乘或除负数，则应该读取完这个数再退出循环
			if (!Character.isDigit(c) && c != '.' && i != 1) {
				break;
			}
			sb.append(c);
		}
		return sb.toString();
	}

	private static String cacNoMD(String str) {
		String ret = "0";
		StringBuilder sb = new StringBuilder();
		char sign = '+';
		for (char c : (str + "+").toCharArray()) {
			if (!Character.isDigit(c) && c != '.') {
				if (sb.length() == 0) {
					sb = new StringBuilder();
					sb.append(c);
					continue;
				} else {
					if (sign == '+') {
						ret = ps.add(ret, sb.toString());
					} else {
						ret = ps.sub(ret, sb.toString());
					}
					sb = new StringBuilder();
					sign = c;
				}
			} else {
				sb.append(c);
			}
		}
		return ret;
	}

	public static void main(String[] args) {
		String a1 = "1.0";
		String a2 = "2.0";
		System.out.println(MathUtil.cacComplex(a1+"+"+a2,-1));
		System.out.println(new BigDecimal(0.2).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());// 0.2

	}
}
