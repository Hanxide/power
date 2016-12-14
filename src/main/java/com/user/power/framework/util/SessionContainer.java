package com.user.power.framework.util;

import com.user.power.framework.domain.Dto;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;


public class SessionContainer implements HttpSessionBindingListener {
    private Dto userInfo ;
    public void valueBound(HttpSessionBindingEvent httpSessionBindingEvent) {

    }

    public void valueUnbound(HttpSessionBindingEvent httpSessionBindingEvent) {

    }

    public Dto getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(Dto userInfo) {
        this.userInfo = userInfo;
    }
}
