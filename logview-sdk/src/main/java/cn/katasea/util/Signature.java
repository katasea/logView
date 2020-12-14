package cn.katasea.util;

import cn.katasea.bean.vo.RequestVO;
import cn.katasea.bean.vo.ResponseVO;
import cn.katasea.myenum.SignType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;

public class Signature {
	private static List<String> ignoreSign = new ArrayList<>();
	private static Logger logger = LoggerFactory.getLogger(Signature.class);
	static {
		ignoreSign.add("sign");
		ignoreSign.add("encryptData");
		ignoreSign.add("extenalMap");
		ignoreSign.add("pageParams");
		ignoreSign.add("externalNo");
	}


	/**
	 * 请求签名
	 */
	public static String createSign(RequestVO<?> requestVO, String appSecret) {
		return createSign(JSON.parseObject(JSON.toJSONString(requestVO)), appSecret);
	}

	/**
	 * 响应签名
	 */
	public static String createSign(ResponseVO<?> responseParams, String appId, String appSecret) {
		return createSign(JSON.parseObject(JSON.toJSONString(responseParams)), appSecret);
	}

	/**
	 * 创建签名
	 */
	public static String createSign(JSONObject jsonObject, String appSecret) {
		// 签名数据集合
		Map<String, Object> signMap = new TreeMap<>();
		Set<Entry<String, Object>> entrys = jsonObject.entrySet();

		// 获取签名键值
		for (Entry<String, Object> entry : entrys) {
			// 非空 且 非过滤签名组合
			if (!CommonUtil.isEmpty(entry.getValue()) && !ignoreSign.contains(entry.getKey())) {
				signMap.put(entry.getKey(), getValue(entry.getValue()));
			}
		}
		// 创建签名
		return Signature.getSign(signMap, appSecret);
	}

	/**
	 * 取值
	 */
	private static String getValue(Object value) {
		if (value instanceof String)
			return getObjString(value);
		else
			return treeJsonParam(value);
	}

	/**
	 * 跳转签名
	 */
	public static String createSign(String appId, int chargeAmt, String outChargeNo, String status, String channel, String timestamp, String appSecret, String signType) {
		String result = appId + chargeAmt + outChargeNo + status + channel + timestamp + appSecret;
		if (CommonUtil.isEmpty(signType) || SignType.MD5.toString().equals(signType)) {
			result = MD5Util.encrypt(appId + chargeAmt + outChargeNo + status + channel + timestamp + appSecret);
		} else if (SignType.RSA.toString().equals(signType)) {
			// TODO RSA 签名
		} else if (SignType.SM3.toString().equals(signType)) {
			result = SM3Utils.encrypt(appId + chargeAmt + outChargeNo + status + channel + timestamp + appSecret);
		}
		return result;
	}

	/**
	 * 对MAP签名 过滤空值 拼接&key=***
	 */
	public static String getSign(Map<String, ?> map, String key) {
		if (map == null)
			return "";

		ArrayList<String> list = new ArrayList<String>();

		for (Map.Entry<String, ?> entry : map.entrySet()) {
			if (CommonUtil.isNotEmpty(getObjString(entry.getValue()))) {
				list.add(entry.getKey() + "=" + entry.getValue() + "&");
			}
		}

		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
		}
		String result = sb.toString();
		result += "key=" + key;
		String signType = String.valueOf(map.get("signType"));
		logger.info("签名类型：{}:", signType);

		if (CommonUtil.isEmpty(signType) || SignType.MD5.toString().equals(signType)) {
			logger.info("MD5签名前的报文为: {}", result);
//			System.out.println("MD5签名前的报文为: " + result);
			result = MD5Util.encrypt(result).toUpperCase();
//			System.out.println("MD5签名后的报文为:" + result);
			logger.info("MD5签名后的报文为：{}",result);
		} else if (SignType.RSA.toString().equals(signType)) {
			// TODO RSA 签名

		} else if (SignType.SM3.toString().equals(signType)) {
			logger.info("SM3签名前的报文为: {}", result);
			result = SM3Utils.encrypt(result);
			logger.info("SM3签名后的报文为:" + result);
		}else {
			result = null;
		}

		return result;
	}

	/**
	 * 对象转换为字符串
	 */
	private static String getObjString(Object object) {
		return (object == null ? "" : (String) object);
	}

	/**
	 * 转换PARAM
	 *
	 */
	private static String treeJsonParam(Object value) {
		String jsoNParam = null;

		if (value instanceof Map<?, ?>) {
			Map<String, Object> treeNestedMap = new TreeMap<String, Object>();

			Map<?, ?> nestedMap = (Map<?, ?>) value;

			for (Map.Entry<?, ?> nestedEntry : nestedMap.entrySet()) {
				treeNestedMap.put((String) nestedEntry.getKey(), nestedEntry.getValue());
			}

			jsoNParam = JSONObject.toJSONString(treeParams(treeNestedMap));
		} else if (value instanceof JSONObject) {
			Map<String, Object> jsonMap = new TreeMap<String, Object>();

			JSONObject nestedMap = (JSONObject) value;

			for (Map.Entry<?, ?> nestedEntry : nestedMap.entrySet()) {
				jsonMap.put((String) nestedEntry.getKey(), nestedEntry.getValue());
			}

			jsoNParam = JSONObject.toJSONString(treeParams(jsonMap));
		} else if (value instanceof ArrayList<?>) {
			ArrayList<?> ar = (ArrayList<?>) value;
			jsoNParam = JSONObject.toJSONString(treeList((ar)));
		} else if (value instanceof JSONArray) {
			JSONArray jarr = (JSONArray) value;
			jsoNParam = JSONObject.toJSONString(treeJsonArray((jarr)));
		} else if (value != null && value.getClass().getPackage().getName().startsWith("com.ylzinfo.onepay.sdk.domain")) {
			jsoNParam = JSONObject.toJSONString(treeParams(JSONObject.parseObject(JSON.toJSONString(value))));
		} else {
			jsoNParam = value.toString();
		}

		return jsoNParam;
	}

	/**
	 * 获取响应签名参数
	 */
	protected  static Map<String, String> signParams(ResponseVO<?> responseVO) {
		Map<String, String> signMap = new TreeMap<String, String>();
		signMap.put("respCode", responseVO.getRetCode());
		signMap.put("respMsg", responseVO.getRetMsg());
		signMap.put("signType", responseVO.getSignType());
		signMap.put("encryptType", responseVO.getEncryptType());
		signMap.put("timestamp", responseVO.getTimestamp());
		signMap.put("bizContent", treeJsonParam(responseVO.getBizObj()));
		return signMap;
	}

	/**
	 * 签名集合算法 -- 排序
	 */
	private static Map<String, Object> treeParams(Map<String, Object> params) {
		if (params == null) {
			return new TreeMap<String, Object>();
		}

		Map<String, Object> treeParams = new TreeMap<String, Object>();

		for (Map.Entry<String, Object> entry : params.entrySet()) {
			String key = entry.getKey();
			Object value = entry.getValue();

			if (value instanceof Map<?, ?>) {
				Map<String, Object> treeNestedMap = new TreeMap<String, Object>();

				Map<?, ?> nestedMap = (Map<?, ?>) value;

				for (Map.Entry<?, ?> nestedEntry : nestedMap.entrySet()) {
					treeNestedMap.put((String) nestedEntry.getKey(), nestedEntry.getValue());
				}

				treeParams.put(key, treeParams(treeNestedMap));
			} else if (value instanceof JSONObject) {
				Map<String, Object> treeNestedMap = new TreeMap<String, Object>();

				JSONObject nestedMap = (JSONObject) value;

				for (Map.Entry<?, ?> nestedEntry : nestedMap.entrySet()) {
					treeNestedMap.put(key, nestedEntry.getValue());
				}

				treeParams.put(key, treeParams(treeNestedMap));
			} else if (value instanceof ArrayList<?>) {
				ArrayList<?> ar = (ArrayList<?>) value;
				treeParams.put(key, treeList(ar));
			} else if (value instanceof JSONArray) {
				JSONArray ar = (JSONArray) value;
				treeParams.put(key, treeJsonArray(ar));
			} else if ("".equals(value)) {
				// flatParams.put(key, "");
			} else if (value == null) {
				// flatParams.put(key, "");
			} else if (value.getClass().getPackage().getName().startsWith("cn.aio.bean")) { // 实体类
				treeParams.put(key, treeParams(JSONObject.parseObject(JSON.toJSONString(value))));
			} else {
				treeParams.put(key, value.toString());
			}
		}

		return treeParams;
	}

	/**
	 * JsonArray排序
	 */
	private static JSONArray treeJsonArray(JSONArray jarr) {
		if (jarr == null || jarr.size() == 0)
			return null;

		JSONArray jsonArray = new JSONArray();

		int size = jarr.size();

		for (int i = 0; i < size; i++) {
			Object value = jarr.get(i);

			if (value instanceof Map<?, ?>) {
				Map<String, Object> treeNestedMap = new TreeMap<String, Object>();

				Map<?, ?> nestedMap = (Map<?, ?>) value;

				for (Map.Entry<?, ?> nestedEntry : nestedMap.entrySet()) {
					treeNestedMap.put((String) nestedEntry.getKey(), nestedEntry.getValue());
				}

				jsonArray.add(i, treeParams(treeNestedMap));
			} else if (value instanceof JSONObject) {
				Map<String, Object> treeNestedMap = new TreeMap<String, Object>();

				JSONObject nestedMap = (JSONObject) value;

				for (Map.Entry<?, ?> nestedEntry : nestedMap.entrySet()) {
					treeNestedMap.put((String) nestedEntry.getKey(), nestedEntry.getValue());
				}

				jsonArray.add(i, treeParams(treeNestedMap));
			} else if (value instanceof ArrayList<?>) {
				ArrayList<?> ar = (ArrayList<?>) value;
				jsonArray.add(i, treeList(ar));
			} else if (value instanceof JSONArray) {
				JSONArray ar = (JSONArray) value;
				jsonArray.add(i, treeJsonArray(ar));
			} else if (value.getClass().getPackage().getName().startsWith("cn.aio.bean")) { // 实体类
				jsonArray.add(i, treeParams(JSONObject.parseObject(JSON.toJSONString(value))));
			} else {
				jsonArray.add(i, value.toString());
			}
		}

		return jsonArray;
	}

	/**
	 * List排序
	 */
	private static JSONArray treeList(ArrayList<?> list) {
		if (list == null || list.size() == 0)
			return null;

		JSONArray jsonArray = new JSONArray();
		int size = list.size();

		for (int i = 0; i < size; i++) {
			jsonArray.add(i, list.get(i));
		}

		return treeJsonArray(jsonArray);
	}

	/**
	 * 创建MD5签名摘要
	 */
	public static String createMD5Sign(JSONObject jsonObject) {
		// 签名数据集合
		Map<String, String> signMap = new TreeMap<String, String>();
		Set<Entry<String, Object>> entrys = jsonObject.entrySet();

		// 获取签名键值
		for (Entry<String, Object> entry : entrys) {
			// 非空过滤
			if (!CommonUtil.isEmpty(entry.getValue())) {
				signMap.put(entry.getKey(), getValue(entry.getValue()));
			}
		}

		ArrayList<String> list = new ArrayList<String>();

		for (Map.Entry<String, String> entry : signMap.entrySet()) {
			if (CommonUtil.isNotEmpty(getObjString(entry.getValue()))) {
				list.add(entry.getKey() + "=" + entry.getValue() + "&");
			}
		}

		int size = list.size();
		String[] arrayToSort = list.toArray(new String[size]);
		Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(arrayToSort[i]);
		}
		String result = sb.toString();

		return MD5Util.encrypt(result).toUpperCase();

	}
}
