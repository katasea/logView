package cn.katasea.main.handler;

import cn.katasea.main.service.ApplicationService;
import cn.katasea.main.service.ReflectionService;
import cn.katasea.bean.po.Application;
import cn.katasea.bean.vo.RequestVO;
import cn.katasea.bean.vo.ResponseVO;
import cn.katasea.exception.PayBusinessException;
import cn.katasea.myenum.EncryptType;
import cn.katasea.myenum.HandlerType;
import cn.katasea.myenum.SignType;
import cn.katasea.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

/**
 * API handler
 *
 * @author katasea
 * 2019/5/16 14:25
 */
@Controller
@RequestMapping("/api")
public class Api {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	@Resource
	private ReflectionService reflectionService;
	@Resource
	private ApplicationService applicationService;

	/**
	 * interface of all request about pay
	 *
	 * @param request  request
	 * @param response response
	 */
	@PostMapping("/gateway.do")

	public void pay(HttpServletRequest request, HttpServletResponse response) {
		long start = System.currentTimeMillis();
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		ResponseVO<Object> responseVO = new ResponseVO<>();
		RequestVO<Object> requestVO = new RequestVO<>();
		try {
			inputStream = request.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, Global.ENCODING);
			String requestMessage = StreamUtil.readInputStream(inputStreamReader);
			logger.info("==|----------------------请求处理阶段-------------------------------|==");
			logger.info("AIO API 统一入口请求开始，请求报文为：{}", requestMessage);
			requestVO = validRequestMssage(requestVO, requestMessage);

			//reflection call
			if (!CommonUtil.isEmpty(requestVO.getMethod())) {
				// 完成代理
				logger.info("开始调用支付服务SRVPAY 反射服务，入参：{}", JSON.toJSONString(requestVO));
				responseVO = reflectionService.doHandler(requestVO);
				logger.info("支付服务SRVPAY 反射服务 返回报文：{}", JSON.toJSONString(responseVO));

			} else {
				// 方法为空需要返回错误
				responseVO.setRetCode(HandlerType.REQ_METHOD_EMPTY.getRetCode());
				responseVO.setRetMsg(HandlerType.REQ_METHOD_EMPTY.getRetMsg());
			}

		} catch (Exception e) {
			responseVO.setRetCode(HandlerType.SYSTEM_ERROR.getRetCode());
			responseVO.setRetMsg(HandlerType.SYSTEM_ERROR.getRetMsg() + ":" + e.getMessage());
		} finally {
			//返回消息
			try {
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
				if (inputStream != null) {
					inputStream.close();
				}
				logger.info("==|----------------------返回结果报文阶段-------------------------------|==");
				response.setContentType("application/json");
				PrintWriter out = response.getWriter();
				// 创建响应报文
				responseVO.setSignType(requestVO.getSignType());
				responseVO.setEncryptType(requestVO.getEncryptType());
				responseVO.setTimestamp(DateUtil.getCurrentDateTime());
				String sign = Signature.createSign(responseVO, requestVO.getAppId(), requestVO.getAppSecret());
				responseVO.setSign(sign);
				String bizContent = JSONObject.toJSONString(responseVO.getBizObj());

				long end = System.currentTimeMillis();
				long callTimeConsume = end - start;
				if(!requestVO.getEncryptType().equals(EncryptType.Plain.toString())) {
					// 加密报文
					logger.info("返回报文加密前：{}", JSONObject.toJSONString(responseVO));
					String encryptData = SecurityUtil.encrypt(bizContent, requestVO.getEncryptType(), requestVO.getAppSecret(), requestVO.getAppId());
					responseVO.setBizContent(encryptData);
					// 清空明文
					if (CommonUtil.isNotEmpty(requestVO.getEncryptType())) {
						responseVO.setBizObj(null);
					}
				}

				String responseMsg = JSON.toJSONString(responseVO);
				logger.info("接口耗时:{}毫秒,返回报文加密后：{}", callTimeConsume, CommonUtil.formateJSON(responseMsg));
				out.write(responseMsg);
				out.close();
			} catch (Exception e1) {
				logger.error("构造响应消息出现异常，异常信息：" + e1.getMessage(), e1);
			}
		}
	}

	/**
	 * CHECK POST TEXT PARAM
	 *
	 * @param requestVO      object
	 * @param requestMessage param string
	 */
	@SuppressWarnings("unchecked")
	private RequestVO<Object> validRequestMssage(RequestVO requestVO, String requestMessage) throws PayBusinessException {
		//valiad post text is null
		if (CommonUtil.isEmpty(requestMessage)) {
			throw new PayBusinessException(HandlerType.REQ_POST_TEXT_NULL.getRetCode(), HandlerType.REQ_POST_TEXT_NULL.getRetMsg());
		}
		//valiad post is illegal
		requestVO = JSON.parseObject(requestMessage, RequestVO.class);
		if (requestVO == null) {
			throw new PayBusinessException(HandlerType.REQ_POST_TEXT_ILLEGAL.getRetCode(), HandlerType.REQ_POST_TEXT_ILLEGAL.getRetMsg());
		}
		//valiad timestamp
		if (CommonUtil.isEmpty(requestVO.getTimestamp())) {
			throw new PayBusinessException(HandlerType.REQ_TIMESTAMP_EMPTY.getRetCode(), HandlerType.REQ_TIMESTAMP_EMPTY.getRetMsg());
		}
		// get appId、appSecret and check
		String appId = requestVO.getAppId();
		if (CommonUtil.isEmpty(appId)) {
			throw new PayBusinessException(HandlerType.REQ_APP_ID_EMPTY.getRetCode(), HandlerType.REQ_APP_ID_EMPTY.getRetMsg());
		}
		// GET APPINFORMATION FROM SRVPAY SERVICE
		Application app = applicationService.getApplication(appId);
		if (app == null) {
			throw new PayBusinessException(HandlerType.REQ_APP_ID_ILLEGAL.getRetCode(), HandlerType.REQ_APP_ID_ILLEGAL.getRetMsg());
		}
		String appSecret = app.getAppSecret();
		// RESET APPSECRET
		requestVO.setAppSecret(appSecret);

		try {
			String encryptData = requestVO.getBizContent();
			String decryptData;
			if (!(EncryptType.Plain.toString().equals(requestVO.getEncryptType()))) {
				logger.info("请求密文:{}", encryptData);
				decryptData = SecurityUtil.decrypt(encryptData, requestVO.getEncryptType(), appSecret, appId);
				logger.info("请求明文:{}", decryptData);
			} else {
				decryptData = encryptData;
				logger.info("----报文不需要解密 -----");
			}
			requestVO.setBizObj(JSON.parseObject(decryptData, Object.class));

		} catch (Exception e) {
			logger.error("解密失败:", e);
			throw new PayBusinessException(HandlerType.REQ_DECRYPT_FAIL.getRetCode(), HandlerType.REQ_DECRYPT_FAIL.getRetMsg() + ":" + e.getMessage());
		}
		if(!requestVO.getSignType().equals(SignType.Plain.toString())) {
			String signOld = requestVO.getSign();//

			//去除不要参与签名的参数
			requestVO.setSign(null);
			requestVO.setBizContent(null);
			requestVO.setAppSecret(null);

			String signNew = Signature.createSign(requestVO, appSecret);
			boolean isVerify = signOld.equals(signNew);
			logger.info("验证签名结果：{}，报文的签名值：{}，服务端的签名值：{}", isVerify, signOld, signNew);
			if (!isVerify ) {
				throw new PayBusinessException(HandlerType.REQ_CHECK_SIGN_FAIL.getRetCode(), HandlerType.REQ_CHECK_SIGN_FAIL.getRetMsg());
			}
			requestVO.setSign(signNew);
		}
		requestVO.setAppSecret(appSecret);
		return requestVO;
	}
}
