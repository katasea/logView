package cn.katasea.main.service.impl;

import cn.katasea.exception.PayBusinessException;
import cn.katasea.main.service.ReflectionService;
import cn.katasea.bean.vo.RequestVO;
import cn.katasea.bean.vo.ResponseVO;
import cn.katasea.myenum.HandlerType;
import cn.katasea.reflect.ReflectConfig;
import cn.katasea.reflect.ReflectContext;
import cn.katasea.util.CommonUtil;
import cn.katasea.util.YamlUtil;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author katasea
 * 2019/9/16 10:33
 */
@Service
public class ReflectionServiceImpl implements ReflectionService {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	public ResponseVO<Object> doHandler(RequestVO requestVO) {
		//TODO
		long begin = System.currentTimeMillis();
		ResponseVO<Object> responseVO = new ResponseVO<>(requestVO);
		logger.info("==|-----------------统一反射调用开始-----------------|==");
		logger.info("支付服务处理启动，入参：{}", JSON.toJSONString(requestVO));
		String method = requestVO.getMethod();
		try {
			Map<String, Object> map = YamlUtil.getTypePropertieMap(YamlUtil.SRVPAY_CONFIG, method);
			if(map == null || map.keySet().size() == 0) {
				throw new PayBusinessException("未找到定义的方法，请检查配置或入参method");
			}
			ReflectConfig config = new ReflectConfig();
			config.setBeanName(String.valueOf(map.get("bean")));
			config.setMethodName(String.valueOf(map.get("method")));
			config.setParam(requestVO);

			responseVO.setBizObj(ReflectContext.excutor(config));

		} catch (Exception e) {
			Exception e1 = (Exception) e.getCause();
			if (e1 != null) {
				e = e1;
			}
			e.printStackTrace();
			responseVO.setRetCode(HandlerType.SYSTEM_ERROR.getRetCode());
			responseVO.setRetMsg(HandlerType.SYSTEM_ERROR.getRetMsg() +"：" + e.getMessage());
		}
		long end = System.currentTimeMillis();
		logger.info("接口耗时:{}毫秒,支付服务处理结束，返回：{}",(end-begin), CommonUtil.formateJSON(JSON.toJSONString(responseVO)));
		logger.info("==|-----------------统一反射调用结束-----------------|==");
		return responseVO;
	}
}
