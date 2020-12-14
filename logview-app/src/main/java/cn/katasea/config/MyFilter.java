package cn.katasea.config;


import cn.katasea.util.CommonUtil;
import cn.katasea.util.Global;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.Date;

@WebFilter(filterName="myFilter",urlPatterns="/*")
public class MyFilter implements Filter {



    @Override
    public void destroy() {
        System.out.println("过滤器销毁");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
        String date = request.getParameter("date");
        if(CommonUtil.isEmpty(date)) {
            date = Global.year_month.format(new Date());
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig config) throws ServletException {}

}