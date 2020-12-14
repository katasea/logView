package cn.katasea.reflect;


import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtil {

	public static void convertReqParam(Object obj, HttpServletRequest request, String[] paramNames) throws Exception, SecurityException{
		Class<?> clz = obj.getClass();
		for(String param : paramNames ){
			Field field = clz.getDeclaredField(param);
			field.setAccessible(true);
			field.set(obj, request.getParameter(param));
		}
	}
	

	public static Object invokeMethod(Object obj, String functionName, Object[] params) throws Exception {
		Method method = null;
		int p = params.length;
		if (null == params || params.length == 0) {
			// 处理无参调用
			method = obj.getClass().getMethod(functionName);
		} else {
			// 处理有参调用
			@SuppressWarnings("rawtypes")
			Class[] paraTypes = new Class[p];
			for (int i = 0; i < paraTypes.length; i++) {
				paraTypes[i] = params[i].getClass();
			}
			method = obj.getClass().getMethod(functionName, paraTypes);
		}

		if (method == null) {
			throw new RuntimeException("方法为空");
		}
		Object rs = null;
		// 调用返回数据
		rs = method.invoke(obj, params);
		return rs;
	}
	
	
}
