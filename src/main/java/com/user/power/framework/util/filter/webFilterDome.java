package com.user.power.framework.util.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by youxiang on 2016/12/13.
 * 自定义测试 web过滤器
 */
public class webFilterDome implements Filter {

    public void destroy() {
        System.out.println("web过滤器销毁");
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("web过滤器执行");
        filterChain.doFilter(servletRequest,servletResponse);
        System.out.println("web过滤器执行结束");
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("web过滤器创建");
    }
}
