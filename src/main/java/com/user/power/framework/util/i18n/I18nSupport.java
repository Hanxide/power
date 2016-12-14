package com.user.power.framework.util.i18n;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class I18nSupport {
    public static String getString(String key,
                                       Object... params) {
            Locale locale=getCurrentLocale();
            //由于我们系统对与英语环境的资源文件没有en 下标 ，将locale的语言设置为"";
            if(locale.getLanguage().toLowerCase().equals("en")) {
                locale=new Locale("","","");
            }
            ResourceBundle bundle=ResourceBundle.getBundle("ApplicationResources",locale); //根据客户浏览器设置的语言环境取得资源文件
            try{
                String value = bundle.getString(key);
                if (params.length > 0)
                    return MessageFormat.format(value, params);
                return value;
            }catch (MissingResourceException e){
                System.out.println(e.getMessage());
                return key;
            }
        }

        public static String getLanguage(){
            return getCurrentLocale().getLanguage();
        }
        public static Locale getCurrentLocale(){
            HttpSession session =null;
            try{
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                session = request.getSession();
            }catch (NullPointerException e){

            }
            Locale locale=null;
            if(session!=null)
                locale=(Locale)session.getAttribute("javax.servlet.jsp.jstl.fmt.locale.session"); // 获取页面上fmt:setBundle 设置的Locale
            if(locale==null){ //如果session为Null，根据浏览器设置的Local
                try{
                    HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
                    if(request!=null){
                        Enumeration<Locale> localeEnumeration = request.getLocales();
                        if(localeEnumeration != null && localeEnumeration.hasMoreElements()){//找到浏览器设置的第一语言
                            locale = localeEnumeration.nextElement();
                        }
                    }
                }catch (NullPointerException e){
                }
            }
            if(locale==null)
                locale=Locale.getDefault();
            return locale;
        }
    
}
