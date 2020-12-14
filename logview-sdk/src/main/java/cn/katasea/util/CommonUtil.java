package cn.katasea.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.*;

/**
 * 工具类，存放大部分通用方法
 *
 * @author chenfuqiang
 */
public class CommonUtil {

	/**
	 * 判断是否为空字符串
	 *
	 * @param str 字符串
	 * @return true 空 false 非空
	 */
	public static boolean isEmpty(String str) {
		return str == null || "".equals(str)
				|| str.length() == 0 || "".equals(str.trim())
				|| "null".equals(str.toLowerCase())
				|| "NaN".equals(str) || "undefined".equals(str);
	}

	/**
	 * 判断是否为空对象
	 *
	 * @param obj 空对象
	 * @return true 空 false 非空
	 */
	public static boolean isEmpty(Object obj) {
		return obj == null || "".equals(String.valueOf(obj))
				|| String.valueOf(obj).length() == 0 || "".equals(String.valueOf(obj).trim())
				|| "null".equals(String.valueOf(obj).toLowerCase())
				|| "NaN".equals(String.valueOf(obj)) || "undefined".equals(String.valueOf(obj));
	}

	/**
	 * 判断是否不为空字符串
	 *
	 * @param str 字符串
	 * @return true 非空 false 空
	 */
	public static boolean isNotEmpty(String str) {
		return !CommonUtil.isEmpty(str);
	}

	/**
	 * 空字符串转为0
	 *
	 * @param str 字符串
	 * @return 字符串
	 */
	public static String nullToZero(String str) {
		if (CommonUtil.isEmpty(str)) {
			return "0";
		} else {
			return str;
		}
	}

	/**
	 * 空字符串转换为""
	 *
	 * @param str 字符串
	 * @return 字符串
	 */
	public static String nullToStr(String str) {
		if (CommonUtil.isEmpty(str)) {
			return Global.NULLSTRING;
		} else {
			return str;
		}
	}

	/**
	 * 判断list 是否是空的
	 *
	 * @param list 集合
	 * @return true 空 false 非空
	 */
	public static boolean isEmptyList(List<?> list) {
		return list == null || list.isEmpty();
	}

	/**
	 * 强转字符串
	 *
	 * @param object 对象
	 * @return 字符串
	 */
	public static String vo(Object object) {
		return String.valueOf(object);
	}

	/**
	 * 获取map的值，可以预防空指针问题。
	 *
	 * @param map 集合
	 * @param key 键
	 * @param <T> 泛型
	 * @return 值
	 */
	public static <T> T getMV(Map<String, T> map, String key) {
		if (map == null || map.size() == 0) {
			return null;
		} else {
			return map.get(key);
		}
	}

	public static <T> T getMV(Map<Integer, T> map, int key) {
		if (map == null || map.size() == 0) {
			return null;
		} else {
			return map.get(key);
		}
	}

	/**
	 * 把List<Map> 结果集转换为 Map<Key,Value>形式返回
	 * 例如 [{key:1,value:2}] => {1,2}
	 *
	 * @param list  集合
	 * @param key   键
	 * @param value 值
	 * @return MAP集合
	 */
	public static Map<String, String> changetListToMap(List<Map<String, Object>> list, String key, String value) {
		Map<String, String> resultMap = new HashMap<>();
		if (list != null) {
			for (Map<String, Object> map : list) {
				if (map != null)
					resultMap.put(String.valueOf(map.get(key)), String.valueOf(map.get(value)));
			}
		}
		return resultMap;
	}

	/**
	 * 把获取的数据库list 变为 map
	 * 例如获取的数据为 [{id:1,name:ff},{id:2,name:dd}] => {1:true,2:true} or {1:<T> value,2...}
	 *
	 * @param list  需要转换的集合对象
	 * @param key   list<Map<String,Object> 数据里要把哪个key的值当做新map的key。例如上面例子中的 id
	 * @param value Map的值
	 * @param <T>   泛型
	 * @return Map
	 */
	public static <T> Map<String, T> changetListToMap(List<Map<String, Object>> list, String key, T value) {
		Map<String, T> resultMap = new HashMap<>();
		if (list != null) {
			for (Map<String, Object> map : list) {
				resultMap.put(String.valueOf(map.get(key)), value);
			}
		}
		return resultMap;
	}

	/**
	 * 把List<Map> 结果集转换为 Map<Key,Object>形式返回
	 * 例如 [{key:1,value:2}] => {1,{key:1,value:2}}
	 *
	 * @param list 集合
	 * @param key  键
	 * @return MAP集合
	 */
	public static Map<String, Map<String, Object>> changeListToMap(List<Map<String, Object>> list, String key) {
		Map<String, Map<String, Object>> resultMap = new HashMap<>();
		for (Map<String, Object> map : list) {
			resultMap.put(String.valueOf(map.get(key)), map);
		}
		return resultMap;
	}


	/**
	 * 处理主键规则
	 *
	 * @param mapOfID  主键集合
	 * @param parentId 父节点
	 * @return 字符串主键
	 */
	public static String dealPKRule(Map<String, Object> mapOfID, String parentId) {
		String result;
		if (mapOfID == null) {
			if (CommonUtil.isEmpty(parentId)) {
				result = "1";
			} else {
				result = parentId + "01";
			}
		} else {
			result = String.valueOf(Integer.parseInt(String.valueOf(mapOfID.get("id"))) + 1);
		}
		return result;
	}


	//date = 20180910
	public static List<String> getYearMonthDayList(String date, String split) {
		if (CommonUtil.isEmpty(date)) {
			date = Global.year_month_day_time_no_.format(new Date());
		} else {
			date = date.replace("-", "");
		}
		int year = Integer.parseInt(date.substring(0, 4));
		String month = date.substring(4, 6);
		int day = 0;
		try {
			day = CommonUtil.getDays(Global.year_month_day_no_.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<String> result = new ArrayList<>();
		for (int i = 1; i <= day; i++) {
			if (i < 10) {
				result.add(String.valueOf(year) + split + month + split + "0" + i);
			} else {
				result.add(String.valueOf(year) + split + month + split + i);
			}
		}
		return result;
	}

	public static List<String> getYearMonthList(String date, String split) {
		if (CommonUtil.isEmpty(date)) {
			date = Global.year_month_day_time_no_.format(new Date());
		} else {
			date = date.replace("-", "");
		}
		int year = Integer.parseInt(date.substring(0, 4));
		List<String> list = new ArrayList<>();
		for (int i = 1; i < 13; i++) {
			if (i < 10) {
				list.add(String.valueOf(year) + split + "0" + i);
			} else {
				list.add(String.valueOf(year) + split + i);
			}
		}
		return list;
	}

	public static int getDays(Date date) {
		Calendar cal = Calendar.getInstance(); //调用Calendar 中的方法；
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1); // 把时间调整为当月的第一天；
		cal.add(Calendar.MONTH, 1); // 月份调至下个月；
		cal.add(Calendar.DAY_OF_MONTH, -1); // 时间减去一天（就等于上个月的最后一天）
		int month = cal.get(Calendar.MONTH) + 1; //调取月份（月份在表示中会少 1，如：1月份得出数字是 0；
		int days = cal.get(Calendar.DAY_OF_MONTH);//调取当月的天数。
		return days;
	}

	/**
	 * DATE:[{key:"yyyyMMdd",value:"/"}]
	 * 获取日期的键值对
	 *
	 * @param date 日期
	 * @param info 信息
	 * @return
	 */
	public static Map<String, String> getDateKVList(String date, Map<String, Object> info) {
		String key = String.valueOf(info.get("key"));
		if (key.contains("dd")) {
			List<String> keyList = CommonUtil.getYearMonthDayList(date, Global.NULLSTRING);
			List<String> valueList = CommonUtil.getYearMonthDayList(date, String.valueOf(info.get("value")));
			Map<String, String> result = new HashMap<>();
			for (int i = 0; i < keyList.size(); i++) {
				result.put(keyList.get(i), valueList.get(i));
			}
			return result;
		} else if (key.contains("MM")) {
			List<String> keyList = CommonUtil.getYearMonthList(date, Global.NULLSTRING);
			List<String> valueList = CommonUtil.getYearMonthList(date, String.valueOf(info.get("value")));
			Map<String, String> result = new HashMap<>();
			for (int i = 0; i < keyList.size(); i++) {
				result.put(keyList.get(i), valueList.get(i));
			}
			return result;
		} else {
			return null;
		}
	}

	/**
	 * 判断是否是本地客户端地址
	 *
	 * @param remoteAddr
	 * @return
	 */
	public static boolean isLocalhost(String remoteAddr) {
		if ("0:0:0:0:0:0:0:1".equals(remoteAddr) || "127.0.0.1".equals(remoteAddr)) {
			return true;
		} else {
			return false;
		}
	}

	public static String formateJSON(String jsonStr) {
		if (null == jsonStr || "".equals(jsonStr)) return "";
		StringBuilder sb = new StringBuilder();
		char last = '\0';
		char current = '\0';
		int indent = 0;
		for (int i = 0; i < jsonStr.length(); i++) {
			last = current;
			current = jsonStr.charAt(i);

			//遇到{ [换行，且下一行缩进
			switch (current) {
				case '{':
				case '[':
					sb.append(current);
					sb.append('\n');
					indent++;
					addIndentBlank(sb, indent);
					break;

				//遇到} ]换行，当前行缩进
				case '}':
				case ']':
					sb.append('\n');
					indent--;
					addIndentBlank(sb, indent);
					sb.append(current);
					break;

				//遇到,换行
				case ',':
					sb.append(current);
					if (last != '\\') {
						sb.append('\n');
						addIndentBlank(sb, indent);
					}
					break;
				default:
					sb.append(current);
			}
		}
		return sb.toString();
	}

	/**
	 *      * 添加space
	 *      * @param sb
	 *      * @param indent
	 *     
	 */
	private static void addIndentBlank(StringBuilder sb, int indent) {
		for (int i = 0; i < indent; i++) {
			sb.append('\t');
		}
	}

	/**
	 * 处理带有中文的关键字。
	 *
	 * @param T       报错提示类 e.g : this.getClass();
	 * @param keyword 关键字
	 * @return 处理结果
	 */
	public static String decodeKeyWord(Class T, String keyword) {
		if (CommonUtil.isEmpty(keyword)) {
			keyword = Global.NULLSTRING;
		} else {
			try {
				keyword = URLDecoder.decode(keyword, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				keyword = Global.NULLSTRING;
			}
			keyword = keyword.replace("\'", "%");
		}
		return keyword;
	}

}

