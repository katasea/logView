package cn.katasea.sdk.service;

import cn.katasea.bean.vo.ResponseVO;
import cn.katasea.exception.PayBusinessException;
import cn.katasea.myenum.IMethodType;
import cn.katasea.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author katasea
 * @date 2019/5/23 11:07
 */
public class SdkSrv extends BaseSdkSrv {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	//==============================构造方法==============================//
	public SdkSrv(String AioCloudSrv, String appId, String appSecret, String signType, String encryptType) {
		super(AioCloudSrv, appId, appSecret, signType, encryptType);
	}
	public SdkSrv(String appId, String appSecret, String signType, String encryptType) {
		super(appId, appSecret, signType, encryptType);
	}


	//==============================提供服务==============================//

	/**
	 * 统一入口服务
	 * @param method 方法名 eg: test.service.forfun
	 * @param version 版本号，默认版本号：DEFAULT.1.0
	 * @param param 业务入参
	 * @return
	 * @throws PayBusinessException
	 */
	public ResponseVO unifyCall(String method,String version,Map<String,Object> param) throws PayBusinessException {
		//根据方法名和版本号拼接对应的serviceID
		method = method.replace(".","/");
		if(CommonUtil.isEmpty(version)) version = "DEFAULT.1.0";
		String serviceId = method + "/" + version;
		logger.info("获取SERVICEID: {}",serviceId);

		return sendRequest(serviceId,param);
	}

	/**
	 * 支付下单
	 * @param chargeRequest 下单请求参数
	 * @return 返回相应参数
	 */
//	public ResponseVO<ChargeResponse> charge(ChargeRequest chargeRequest) throws PayBusinessException {
//		String method = IMethodType.CHARGE.getCode();
//		return sendRequest(method,chargeRequest,ChargeResponse.class);
//	}
}
