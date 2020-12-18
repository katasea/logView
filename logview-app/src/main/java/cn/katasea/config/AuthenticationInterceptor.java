package cn.katasea.config;

/**
 * @author katasea
 * 2020/5/12 13:28
 */


import cn.katasea.bean.vo.ResponseVO;
import cn.katasea.myenum.HandlerType;
import cn.katasea.util.CommonUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {


	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
		if(CommonUtil.isNotEmpty(String.valueOf(request.getSession().getAttribute("isLogin")))) {
			return true;
		}else {
			return responseFalse(request,response);
		}
	}

	private boolean responseFalse(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		ResponseVO<String> responseVO = new ResponseVO<>();
		responseVO.setRetInfo(HandlerType.NO_LOGIN);
		log.info("登录拦截器：登录失败！地址:{},信息:{}",request.getRequestURI(),JSON.toJSONString(responseVO));
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String errorMsg = "系统平台会话已超时,或者未登录!请重新登录";
		out.println("<script language='javascript'>");
		out.println("var frameArray = window.top.frames;");
		out.println("window.parent.top.location.href='" + request.getContextPath() + "/login"+ "';");
		out.println("</script>");
		out.flush();
		out.close();
		return false;
	}


	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
	}
}