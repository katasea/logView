package cn.katasea.sdk.service;

import cn.katasea.bean.vo.RequestVO;
import cn.katasea.bean.vo.ResponseVO;
import cn.katasea.myenum.EncryptType;
import cn.katasea.myenum.SignType;
import cn.katasea.util.*;
import cn.katasea.exception.PayBusinessException;
import cn.katasea.myenum.HandlerType;
import cn.katasea.myenum.URLType;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author katasea
 * 2019/5/23 11:01
 */
public class BaseSdkSrv {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private final String API_URI = "/api/gateway.do";
	/**
	 * 服务地址
	 */
	private String web_pay_url = "http://127.0.0.1:8007/web-pay";

	/**
	 * 接口服务地址
	 */
	private String apiUrl = web_pay_url + API_URI;

	/**
	 * 应用id
	 */
	private String appId;

	/**
	 * 应用安全key
	 */
	private String appSecret;

	/**
	 * 签名类型
	 */
	private String signType;

	/**
	 * 加密类型
	 */
	private String encryptType;

	//==============================构造方法==============================//

	BaseSdkSrv(String web_pay_url, String appId, String appSecret, String signType, String encryptType) {
		super();
		this.web_pay_url = web_pay_url;
		this.apiUrl = web_pay_url + API_URI;
		this.appId = appId;
		this.appSecret = appSecret;
		this.signType = signType;
		this.encryptType = encryptType;
	}

	BaseSdkSrv(String appId, String appSecret, String signType, String encryptType) {
		this.appId = appId;
		this.appSecret = appSecret;
		this.signType = signType;
		this.encryptType = encryptType;
	}

	//===============================内部服务===============================//


	/**
	 * 验证签名
	 */
	private boolean verifySign(String oldSign, JSONObject res) throws PayBusinessException {
		try {

			String newSign = Signature.createSign(res, appSecret);
			return oldSign.equals(newSign);
		} catch (Exception e) {
			throw new PayBusinessException("验证失败, " + e.getMessage());
		}
	}

	/**
	 * 验证签名
	 *
	 * @throws PayBusinessException 支付业务异常
	 */
	public boolean verifySign(String oldSign, String responseMsg) throws PayBusinessException {
		return verifySign(oldSign, JSON.parseObject(responseMsg));
	}

	/**
	 * 验证签名
	 *
	 * @throws PayBusinessException 支付业务异常
	 */
	public boolean verifySign(String oldSign,ResponseVO<?> responseVO) throws PayBusinessException {
		String sign = responseVO.getSign();
		String bizContent = responseVO.getBizContent();
		//去除一些字段信息，不参与签名
		responseVO.setSign(null);
		responseVO.setBizContent(null);
		boolean result = verifySign(oldSign,JSON.parseObject(JSON.toJSONString(responseVO)));
		responseVO.setSign(sign);
		responseVO.setBizContent(bizContent);
		return result;
	}

	/**
	 * 验证请求签名
	 *
	 * @param requestMessage 请求消息
	 * @throws PayBusinessException 支付业务异常
	 */
	public boolean verifyRequestSign(String oldSign, String requestMessage) throws PayBusinessException {
		return verifySign(oldSign,JSON.parseObject(requestMessage));
	}

	//===============================提供服务===============================//

	/**
	 * 通用业务处理方法
	 */
	public <T> ResponseVO<T> sendRequest(String method, Object params) throws PayBusinessException {
		try {
			String timestamp = DateUtil.getCurrentDateTime();

			// 创建请求参数对象
			RequestVO<Object> requestVO = new RequestVO<>();
			requestVO.setAppId(appId);
			requestVO.setTimestamp(timestamp);
			requestVO.setBizObj(params);
			requestVO.setMethod(method);
			requestVO.setSignType(signType);
			requestVO.setEncryptType(encryptType);

			// 创建签名信息
			logger.info("准备加密报文：入参明文与密钥：{},{}",JSONObject.toJSONString(requestVO),appSecret);
			String sign = Signature.createSign(requestVO, appSecret);
			requestVO.setSign(sign);

			// 加密报文
			try {
				logger.info("加密前报文：{}", JSONObject.toJSONString(requestVO));
				String encryptData = SecurityUtil.encrypt(JSONObject.toJSONString(requestVO.getBizObj()), encryptType, appSecret, appId);
				logger.info("加密后报文：{}", encryptData);
				requestVO.setBizContent(encryptData);
			} catch (Exception e) {
				e.printStackTrace();
				throw new PayBusinessException("请求报文加密失败");
			}

			// 清空明文
			requestVO.setBizObj(null);

			// 创建请求报文并发送请求
			String requestMessage = JSON.toJSONString(requestVO);
			logger.info("请求报文【已加密】：{}", requestMessage);
			URLType urlType = URLType.HTTP;

			if (apiUrl.toLowerCase().startsWith("https")) {
				urlType = URLType.HTTPS;
			}

			String responseMsg = HttpUtil.request(apiUrl, requestMessage, urlType);

			if (CommonUtil.isEmpty(responseMsg)) {
				throw new PayBusinessException("请求错误");
			}
			// 参数转换
			JSONObject res = JSON.parseObject(responseMsg);
			ResponseVO<T> responseVO = new ResponseVO<>();
			responseVO.setSign(res.getString("sign"));
			responseVO.setSignType(res.getString("signType"));
			responseVO.setEncryptType(res.getString("encryptType"));
			responseVO.setTimestamp(res.getString("timestamp"));
			responseVO.setRetCode(res.getString("retCode"));
			responseVO.setRetMsg(res.getString("retMsg"));
			responseVO.setBizContent(res.getString("bizContent"));

			// 解密报文
			try {
				String encData = responseVO.getBizContent();
				if(!requestVO.getEncryptType().equals(EncryptType.Plain.toString())) {

					if (CommonUtil.isNotEmpty(encData)) {
						logger.info("返回报文解密前：{}", JSONObject.toJSONString(responseVO));
						String decryptData = SecurityUtil.decrypt(encData, encryptType, appSecret, appId);
						responseVO.setBizObj((T) JSON.parseObject(decryptData));
						logger.info("返回报文解密后：{}", JSONObject.toJSONString(responseVO));
					}
				}else {
					responseVO.setBizObj((T) res.get("bizObj"));
					responseVO.setBizContent(null);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new PayBusinessException("响应报文解密失败");
			}

			// 校验返回签名
			if(!requestVO.getSignType().equals(SignType.Plain.toString())) {
				if (HandlerType.isSuccessful(responseVO) && !verifySign(responseVO.getSign(),responseVO)) {
					throw new PayBusinessException("验签失败");
				}
			}else {
				responseVO.setSign(null);
			}

			return responseVO;
		} catch (Exception e) {
			e.printStackTrace();
			throw new PayBusinessException(e);
		}
	}


}
