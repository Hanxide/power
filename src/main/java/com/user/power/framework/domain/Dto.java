package com.user.power.framework.domain;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;


public interface Dto extends Map<String,Object> {

    public Integer getAsInteger(String key);

    public Long getAsLong(String key);

    public String getAsString(String key);

    public BigDecimal getAsBigDecimal(String pStr);

    public Date getAsDate(String pStr);

    public List getAsList(String key);

    public Timestamp getAsTimestamp(String key);

    public Boolean getAsBoolean(String key);

    public void setDefaultAList(List pList);

    public void setDefaultBList(List pList);

    public List getDefaultAList();

    public List getDefaultBList();

    public void setDefaultJson(String jsonString);

    public String getDefaultJson();

    public void setSuccess(Boolean pSuccess);

    public Boolean getSuccess();

    public void setMsg(String pMsg);

    public String getMsg();

}
