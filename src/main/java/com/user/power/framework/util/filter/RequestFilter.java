package com.user.power.framework.util.filter;


import com.user.power.framework.domain.Dto;
import com.user.power.framework.domain.SysConstants;
import com.user.power.framework.util.SystemUtils;
import com.user.power.framework.util.WebUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RequestFilter implements Filter {
    private Log log = LogFactory.getLog(RequestFilter.class);
    protected FilterConfig filterConfig;
    protected boolean enabled;

    public RequestFilter() {
        filterConfig = null;
        enabled = true;
    }

    public void init(FilterConfig pFilterConfig) throws ServletException {
        this.filterConfig = pFilterConfig;
        String value = filterConfig.getInitParameter("enabled");
        this.enabled = SystemUtils.isEmpty(value) || value.equalsIgnoreCase("true");

    }

    /**
     * 自定义拦截器,判断用户是否登录
     */
    public void doFilter(ServletRequest pRequest, ServletResponse pResponse, FilterChain fc) throws IOException,
            ServletException {
        HttpServletRequest request = (HttpServletRequest) pRequest;
        HttpServletResponse response = (HttpServletResponse) pResponse;
        String ctxPath = request.getContextPath();
        String uri = request.getRequestURI();
        Dto loginUser =  WebUtils.getSessionContainer(request).getUserInfo();
        String postType = request.getParameter("postType");
        postType = SystemUtils.isEmpty(postType) ? SysConstants.PostType_Normal : postType;

        if (postType.equals(SysConstants.PostType_Nude)) {
            fc.doFilter(request, response);
        } else {
            if (uri.contains("/login") ||  (uri.contains("/eliteadmin-inverse")) || (uri.contains("/plugins")) || (uri.contains(".jsp"))
                    || (uri.contains(".js")) || (uri.contains(".png")) || (uri.contains(".jpg"))) {
                fc.doFilter(request, response);
            }  else {
                String isAjax = request.getHeader("x-requested-with");
                if (SystemUtils.isEmpty(loginUser) && !uri.contains("/login") && enabled) {
                    if (SystemUtils.isEmpty(isAjax)) {
                        response.getWriter().write(
                                "<script type=\"text/javascript\">"+
                                        "alert(\"Please login again,session invalid!\");"+
                                        "parent.location.href='" +ctxPath+ "/login.jsp';</script>");
                        response.getWriter().flush();
                        response.getWriter().close();
                    } else {
                        response.sendError(SysConstants.Ajax_Timeout);
                    }
                } else {
                    fc.doFilter(request, response);
                }
            }
        }
    }

    public void destroy(){
        filterConfig = null;
    }
}
