package com.user.power.framework.util;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.user.power.framework.domain.BaseDto;
import com.user.power.framework.domain.Dto;
import com.user.power.framework.domain.SysConstants;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.Column;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BaseController {

    protected Object getSessionAttribute(HttpServletRequest request, String sessionKey) {
        Object objSessionAttribute = null;
        HttpSession session = request.getSession(false);
        if (session != null) {
            objSessionAttribute = session.getAttribute(sessionKey);
        }
        return objSessionAttribute;
    }

    protected void setSessionAttribute(HttpServletRequest request, String sessionKey, Object objSessionAttribute) {
        HttpSession session = request.getSession();
        if (session != null)
            session.setAttribute(sessionKey, objSessionAttribute);
    }

    protected void removeSessionAttribute(HttpServletRequest request, String sessionKey) {
        HttpSession session = request.getSession();
        if (session != null)
            session.removeAttribute(sessionKey);
    }

    protected static Dto getPraramsAsDto(HttpServletRequest request) {

        return WebUtils.getPraramsAsDto(request);
    }

    protected static Dto getPraramsAsDtoUTF8(HttpServletRequest request) {
        return WebUtils.getPraramsAsDtoUTF8(request);
    }

    protected static Dto getMultipartPraramsAsDto(HttpServletRequest request) throws ServletException {
        return WebUtils.getMultipartPraramsAsDto(request);
    }

    protected String getCodeDesc(String pField, String pCode, HttpServletRequest request) {
        return WebUtils.getCodeDesc(pField, pCode, request);
    }

    protected List getCodeListByField(String pField, HttpServletRequest request) {
        return WebUtils.getCodeListByField(pField, request);
    }

    protected String getParamValue(String pParamKey, HttpServletRequest request) {
        return WebUtils.getParamValue(pParamKey, request);
    }

    protected void write(String str, HttpServletResponse response) throws IOException {
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(str);
        response.getWriter().flush();
        response.getWriter().close();
    }

    /**
     * 根据页面的请求返回动态分页组件
     *
     * @param request request请求
     * @param cls     需要分页的类对象
     * @param <T>     具体的类型
     * @return 分页组件对象
     */
    protected <T> PageInfo<T> convertPara2Page(HttpServletRequest request, Class cls) {
        //paging info parameter : rows-->10; page-->1;sord-->asc/desc;sidx-->sort for filed
        int rows = Integer.valueOf(request.getParameter("rows"));
        int page = Integer.valueOf(request.getParameter("page"));
        PageInfo<T> pageInfo = new PageInfo<T>();
        pageInfo.setPageNum(page);
        pageInfo.setPageSize(rows);
        if (null != request.getParameter("sidx") && !StringUtils.isEmpty(request.getParameter("sidx"))) {
            final String columnName = getAnnotationFiled(cls, request.getParameter("sidx"));
            if (null != columnName && !StringUtils.isEmpty(columnName)) {
                pageInfo.setOrderBy(columnName + " " + request.getParameter("sord"));
            } else {
                pageInfo.setOrderBy(request.getParameter("sidx") + " " + request.getParameter("sord"));
            }
        }
        return pageInfo;
    }

    /**
     * 获取初始化分页组件信息
     *
     * @param cls 需要分页的类对象
     * @param <T> 具体的类型
     * @return 分页组件对象
     */
    protected <T> PageInfo<T> getInitPage(Class cls) {
        PageInfo<T> pageInfo = new PageInfo<T>();
        pageInfo.setPageNum(0);            //当前页
        pageInfo.setPageSize(10);           //每页的数量，初始化为10条记录
        pageInfo.setSize(10);                  //当前页的数量
        return pageInfo;
    }

    protected void write(List<Dto> list, HttpServletResponse response) throws IOException {
        Map<String, Object> map = new HashMap<String, Object>();
        long total = ((Page) list).getTotal();   //总记录数
        int pages = ((Page) list).getPages();  //总页数
        int pageNum = ((Page) list).getPageNum();  //当前页数
        map.put(SysConstants.PAGE_RECORDS, total);
        map.put(SysConstants.PAGE_PAGES, pages);
        map.put(SysConstants.PAGE_PAGE, pageNum);
        map.put(SysConstants.PAGE_ROWS, list);

        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(map));
        response.getWriter().flush();
        response.getWriter().close();
    }

    protected SessionContainer getSessionContainer(HttpServletRequest request) {
        SessionContainer sessionContainer = (SessionContainer) this.getSessionAttribute(request, "SessionContainer");
        if (sessionContainer == null) {
            sessionContainer = new SessionContainer();
            HttpSession session = request.getSession(true);
            session.setAttribute("SessionContainer", sessionContainer);
        }
        return sessionContainer;
    }

    protected String encodeString(String str) throws UnsupportedEncodingException {
        if (null != str && !"".equals(str)) {
            return new String(str.getBytes("ISO8859-1"), "UTF-8");
        }
        return null;
    }

    protected ModelAndView ajaxDone(int statusCode, String message) {
        ModelAndView mav = new ModelAndView("ajaxDone");
        mav.addObject("statusCode", statusCode);
        mav.addObject("message", message);
        return mav;
    }

    protected Dto ajaxDoneDto(int statusCode, String message) {
        Dto mav = new BaseDto();
        mav.put("statusCode", statusCode);
        mav.put("message", message);
        return mav;
    }

    protected Dto ajaxDoneDtoAndClose(int statusCode, String message) {
        Dto mav = new BaseDto();
        mav.put("callbackType", "closeCurrent");
        mav.put("statusCode", statusCode);
        mav.put("message", message);     //"callbackType":"closeCurrent"}
        return mav;
    }

    protected ModelAndView ajaxDoneSuccess(String message) {
        return ajaxDone(200, message);
    }

    protected Dto ajaxDoneSuccessDto(String message) {
        return ajaxDoneDto(200, message);
    }

    protected Dto ajaxDoneSuccessClose(String message) {
        return ajaxDoneDtoAndClose(200, message);
    }

    protected ModelAndView ajaxDoneError(String message) {
        return ajaxDone(200, message);
    }

    protected Dto ajaxDoneErrorDto(String message) {
        return ajaxDoneDto(200, message);
    }

    private String getAnnotationFiled(Class cls, String filed) {
        String columnName = "";
        for (Field field : cls.getDeclaredFields()) {
            String name = field.getName();
            if (filed.equals(name)) {
                Annotation[] annotations = field.getDeclaredAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation instanceof Column) {
                        Column myAnnotation = (Column) annotation;
                        columnName = myAnnotation.name();
                    }
                }
            }
        }
        return columnName;
    }

    public static String getIpAddr(HttpServletRequest request){
        String ip = request.getHeader("X-Real-IP");
        if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isEmpty(ip) && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个IP值，第一个为真实IP。
            int index = ip.indexOf(',');
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            return request.getRemoteAddr();
        }
    }

}
