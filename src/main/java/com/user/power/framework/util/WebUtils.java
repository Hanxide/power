package com.user.power.framework.util;

import com.user.power.framework.domain.BaseDto;
import com.user.power.framework.domain.Dto;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class WebUtils {

    public static int SQL_NEXTVAL = 1;

    public static SessionContainer getSessionContainer(HttpServletRequest request) {
        SessionContainer sessionContainer = (SessionContainer) request
                .getSession().getAttribute("SessionContainer");
        if (sessionContainer == null) {
            sessionContainer = new SessionContainer();
            HttpSession session = request.getSession(true);
            session.setAttribute("SessionContainer", sessionContainer);
        }
        return sessionContainer;
    }

    public static SessionContainer getSessionContainer(HttpSession session) {
        SessionContainer sessionContainer = (SessionContainer) session
                .getAttribute("SessionContainer");
        if (sessionContainer == null) {
            sessionContainer = new SessionContainer();
            session.setAttribute("SessionContainer", sessionContainer);
        }
        return sessionContainer;
    }

    /**
     * 设置一个Session属性对象
     *
     * @param request             request对象
     * @param sessionKey          session名称
     * @param objSessionAttribute 需要设置的session对象
     */
    public static void setSessionAttribute(HttpServletRequest request,
                                           String sessionKey, Object objSessionAttribute) {
        HttpSession session = request.getSession();
        if (session != null)
            session.setAttribute(sessionKey, objSessionAttribute);
    }

    /**
     * 移除Session对象属性值
     *
     * @param request    request对象
     * @param sessionKey session名称
     */
    public static void removeSessionAttribute(HttpServletRequest request,
                                              String sessionKey) {
        HttpSession session = request.getSession();
        if (session != null)
            session.removeAttribute(sessionKey);
    }

    /**
     * 将请求参数封装为Dto
     *
     * @param request
     * @return
     */
    public static Dto getPraramsAsDto(HttpServletRequest request) {
        Dto dto = new BaseDto();
        Map map = request.getParameterMap();
        Iterator keyIterator = (Iterator) map.keySet().iterator();
        while (keyIterator.hasNext()) {
            String key = (String) keyIterator.next();
            String value = "";
            if(((String[]) (map.get(key))).length > 0){
                for (String v : ((String[]) (map.get(key)))){
                    value += v +",";
                }
                dto.put(key,value.substring(0,value.length() - 1));
            } else {
                value = ((String[]) (map.get(key)))[0];
                dto.put(key, value);
            }
        }
        return dto;
    }

    /**
     * 将请求参数封装为Dto
     *
     * @param request
     * @return
     */
    public static Dto getPraramsAsDtoUTF8(HttpServletRequest request) {
        Dto dto = new BaseDto();
        Map map = request.getParameterMap();
        Iterator keyIterator = (Iterator) map.keySet().iterator();
        while (keyIterator.hasNext()) {
            String key = (String) keyIterator.next();
            String value = ((String[]) (map.get(key)))[0];
            try {
                value = new String(value.getBytes("ISO8859-1"),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            dto.put(key, value);
        }
        return dto;
    }

    /**
     * 将请求参数封装为Dto
     *
     * @param request
     * @return
     */
    public static Dto getMultipartPraramsAsDto(HttpServletRequest request) {
        DiskFileUpload upload = new DiskFileUpload();
        upload.setHeaderEncoding("UTF-8");
        Dto dto = new BaseDto();
        try {
            List items = upload.parseRequest(request);
            for (Object item1 : items) {
                FileItem item = (FileItem) item1;
                if (item.isFormField()) {
                    //参数名
                    String key = item.getFieldName();
                    //参数值
                    String value = item.getString("UTF-8");
                    dto.put(key, value);
                }
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return dto;
    }

    /**
     * 获取代码对照值
     *
     * @param
     * @param
     * @param request
     * @return
     */
    public static String getCodeDesc(String pField, String pCode,
                                     HttpServletRequest request) {
        List codeList = (List) request.getSession().getServletContext()
                .getAttribute("EACODELIST");
        String codedesc = null;
        for (int i = 0; i < codeList.size(); i++) {
            Dto codeDto = (BaseDto) codeList.get(i);
            if (pField.equalsIgnoreCase(codeDto.getAsString("field"))
                    && pCode.equalsIgnoreCase(codeDto.getAsString("code")))
                codedesc = codeDto.getAsString("codedesc");
        }
        return codedesc;
    }

    /**
     * 根据代码类别获取代码表列表
     *
     * @param
     * @param request
     * @return
     */
    public static List getCodeListByField(String pField,
                                          HttpServletRequest request) {
        List codeList = (List) request.getSession().getServletContext()
                .getAttribute("EACODELIST");
        List lst = new ArrayList();
        for (int i = 0; i < codeList.size(); i++) {
            Dto codeDto = (BaseDto) codeList.get(i);
            if (codeDto.getAsString("field").equalsIgnoreCase(pField)) {
                lst.add(codeDto);
            }
        }
        return lst;
    }

    /**
     * 获取全局参数值
     *
     * @param pParamKey 参数键名
     * @return
     */
    public static String getParamValue(String pParamKey,
                                       HttpServletRequest request) {
        String paramValue = "";
        ServletContext context = request.getSession().getServletContext();
        if (SystemUtils.isEmpty(context)) {
            return "";
        }
        List paramList = (List) context.getAttribute("EAPARAMLIST");
        for (int i = 0; i < paramList.size(); i++) {
            Dto paramDto = (BaseDto) paramList.get(i);
            if (pParamKey.equals(paramDto.getAsString("paramkey"))) {
                paramValue = paramDto.getAsString("paramvalue");
            }
        }
        return paramValue;
    }

    /**
     * 获取全局参数
     *
     * @return
     */
    public static List getParamList(HttpServletRequest request) {
        ServletContext context = request.getSession().getServletContext();
        if (SystemUtils.isEmpty(context)) {
            return new ArrayList();
        }
        return (List) context.getAttribute("EAPARAMLIST");
    }

    /**
     * 获取指定Cookie的值
     *
     * @param cookies      cookie集合
     * @param cookieName   cookie名字
     * @param defaultValue 缺省值
     * @return
     */
    public static String getCookieValue(Cookie[] cookies, String cookieName,
                                        String defaultValue) {
        if (cookies == null) {
            return defaultValue;
        }
        for (int i = 0; i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookieName.equals(cookie.getName()))
                return (cookie.getValue());
        }
        return defaultValue;
    }

    public static String getClassPath() {
        String path = WebUtils.class.getResource("/").getPath();
        while (path.indexOf("%20") != -1) {
            path = path.replace("%20", " ");
        }
        if (path.startsWith("/"))
            path = path.substring(1, path.length());
        return path;
    }

    /**
     * 根据容器获取当前request
     *
     * @return request
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 图片页面展示
     *
     * @param file     图片文件
     * @param response 页面推送
     * @throws java.io.IOException IO异常
     */
    public static void printImageToClient(File file, HttpServletResponse response) throws IOException {
        String mimeType = "";
        String fileName = file.getName();
        if (fileName.length() > 5) {
            if (fileName.substring(fileName.length() - 5, fileName.length()).equals(".jpeg")) {
                mimeType = "image/jpeg";
            } else if (fileName.substring(fileName.length() - 4, fileName.length()).equals(".png")) {
                mimeType = "image/png";
            } else if (fileName.substring(fileName.length() - 4, fileName.length()).equals(".gif")) {
                mimeType = "image/gif";
            } else {
                mimeType = "image/jpg";
            }
        }
        if (file.exists()) {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            response.setHeader("Content-Type", mimeType);
            response.setHeader("Content-Length", String.valueOf(file.length()));
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            response.setHeader("Last-Modified", sdf.format(new Date(file.lastModified())));
            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            byte input[] = new byte[4096];
            boolean eof = false;
            while (!eof) {
                int length = bis.read(input);
                if (length == -1) {
                    eof = true;
                } else {
                    bos.write(input, 0, length);
                }
            }
            bos.flush();
            bis.close();
            bos.close();
            response.setStatus(HttpServletResponse.SC_OK); //防止出现connection reset by peer
            response.flushBuffer();
        } else {
            throw new FileNotFoundException(file.getAbsolutePath());
        }
    }
}
