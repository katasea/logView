package cn.katasea.reflect;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 反射工具上下文
 */
public class ReflectContext {

	private static Logger logger = LoggerFactory.getLogger(ReflectContext.class);

	public static Object excutor(ReflectConfig config) throws Exception {
		logger.info("反射调用服务信息：{}",JSON.toJSONString(config));
		Object obj = SpringUtil.getBean(config.getBeanName());
		Object result = ReflectUtil.invokeMethod(obj, config.getMethodName(), config.getParams().toArray(new Object[]{}));
		logger.info("反射调用结束返回：{}",JSON.toJSONString(result));
		return result;
	}
}
