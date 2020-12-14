package cn.katasea.main.aop;

import cn.katasea.util.CommonUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;

@Aspect
@Component
@Order(-5)
public class ServiceInterceptor {
    @Value("${server.port}")
    private String port;
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 定义一个切入点.
     * 解释下：
     * ~ 第一个 * 代表任意修饰符及任意返回值.
     * ~ 第二个 * 任意包名
     * ~ 第三个 * 代表任意方法.
     * ~ 第四个 * 定义在web包或者子包
     * ~ 第五个 * 任意方法
     * ~ .. 匹配任意数量的参数.
     */

    @Pointcut("execution(public * cn.katasea.main.service.impl.*.*(..))")

    public void Log() {
    }

    /**
     * 环绕通知   拦截指定的切点，这里拦截joinPointForAddOne切入点所指定的addOne()方法
     *
     */
    @Around(value= "Log()")
    public Object interceptorAddOne(ProceedingJoinPoint joinPoint) throws Throwable {
	    String targetName = joinPoint.getTarget().getClass().getName();
	    String methodName = joinPoint.getSignature().getName();

	    Class targetClass = Class.forName(targetName);
	    Object[] arguments = joinPoint.getArgs();   //获得参数列表
	    Method[] method = targetClass.getMethods();
	    String methode = "";
	    String option = "";
	    for (Method m : method) {
		    if (m.getName().equals(methodName)) {
			    Class[] tmpCs = m.getParameterTypes();
			    if (tmpCs.length == arguments.length) {
				    LogAnnotation methodCache = m.getAnnotation(LogAnnotation.class);
				    if(methodCache != null) {
					    methode = methodCache.moduleName();
					    option = methodCache.option();
				    }
				    break;
			    }
		    }
	    }
	    Object result = null;
	    if(CommonUtil.isNotEmpty(methode)) {
		    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		    logger.info("--------------%SERVICE%-----------------");
//		    if(attributes!=null) {
//			    HttpServletRequest request = attributes.getRequest();
//			    UserInfo user = (UserInfo) request.getSession().getAttribute("userSession");
//			    if (user != null) {
//				    logger.info("用户信息 : [" + user.getUserid() + "/" + user.getUsername() + "] ");
//			    }
//		    }
		    logger.info("服务描述  : " + methode);
		    logger.info("方法描述  : " + option);
		    logger.info("服务类名  : " + targetName);
		    logger.info("方法名称  : " + methodName);
		    if(arguments.length<=0){
			    logger.info("参数列表  : 方法没有参数");
		    }else{
		    	StringBuilder sb = new StringBuilder();
		    	sb.append("[");
			    for(int i=0;i<arguments.length;i++){
			    	if(i!=0) {
			    		sb.append(" , ");
				    }
			    	sb.append((i+1)).append(" : ").append(arguments[i]);
			    }
			    sb.append("]");
			    logger.info("参数列表  : "+sb.toString());

		    }
		    logger.info("----------------------------------------");
	    }
	    try {
		    result = joinPoint.proceed();
	    } catch (Exception e) {
		    throw e;
	    }
        return result;
    }

    @AfterReturning("Log()")
    public void doAfterReturning(JoinPoint joinPoint) {
        // 处理完请求，返回内容
    }


}
