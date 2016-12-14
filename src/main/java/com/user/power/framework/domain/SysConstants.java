package com.user.power.framework.domain;


import com.user.power.framework.util.i18n.I18nSupport;

public interface SysConstants {

    public static final String PAGE_PAGES = "total";      //分页总数
    public static final String PAGE_ROWS = "rows";      //具体的数据内容
    public static final String PAGE_RECORDS= "records";  //总记录数量
    public static final String PAGE_PAGE = "page";           //当前页数
    public static final String SELECT_ORDER_BY = "orderby";

    public static final String S_STYLE_N = "number";

    public static final String S_STYLE_L = "letter";

    public static final String S_STYLE_NL = "numberletter";

    public static final String Exception_Head = "\n" + I18nSupport.getString("system.exception.header") + "\n"
            + I18nSupport.getString("system.exception.point.info") + "\n";

    public static final String PostType_Nude = "1";

    public static final String PostType_Normal = "0";

    public static final int Ajax_Timeout = 999;

}
