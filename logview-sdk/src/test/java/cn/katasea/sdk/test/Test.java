package cn.katasea.sdk.test;

/**
 * @author katasea
 * 2020/12/11 16:46
 */
public class Test {



	@org.junit.Test
	public void test() {
		int[] input = {0,1,1};
		String t = "";
		boolean[] result = new boolean[input.length];

		for (int i = 0; i < input.length; i++) {
			t = t+input[i];
			int x = binary2Decimal(t);
			result[i] = (x%5==0);
		}

		System.out.println(printArray(result));

	}

	public String printArray(boolean[] objs) {
		StringBuilder sb = new StringBuilder();
		StringBuilder result = new StringBuilder();
		for (Object obj : objs) {
			sb.append(obj.toString()).append(",");
		}
		result.append("[");
		result.append(sb.substring(0,sb.length()-1));
		result.append("]");
		return result.toString();
	}

	/**
	 * 二进制转十进制
	 * @param number
	 * @return
	 */
	public static int binary2Decimal(String number) {
		return scale2Decimal(number, 2);
	}

	/**
	 * 其他进制转十进制
	 * @param number
	 * @return
	 */
	public static int scale2Decimal(String number, int scale) {
		if (2 > scale || scale > 32) {
			throw new IllegalArgumentException("scale is not in range");
		}
		// 不同其他进制转十进制,修改这里即可
		int total = 0;
		String[] ch = number.split("");
		int chLength = ch.length;
		for (int i = 0; i < chLength; i++) {
			total += Integer.valueOf(ch[i]) * Math.pow(scale, chLength - 1 - i);
		}
		return total;

	}
}
