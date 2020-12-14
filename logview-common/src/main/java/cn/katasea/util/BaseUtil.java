package cn.katasea.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author katasea
 * 2019/7/30 16:11
 */
public class BaseUtil {
	static Logger logger = LoggerFactory.getLogger(BaseUtil.class);
	public static <T> T convertValue(Object bean, Class<T> clazz){
		try{
			ObjectMapper mapper = new ObjectMapper();
			return mapper.convertValue(bean, clazz);
		}catch(Exception e){
			logger.error("错误的转换：BeanUtil.convertValue() --->" + e.getMessage());
			return null;
		}
	}
}
