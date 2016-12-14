package com.user.power.framework.util.mybatis.mapper;


import com.user.power.framework.util.mybatis.mapper.select.SelectAllDtoMapper;
import com.user.power.framework.util.mybatis.mapper.select.SelectDtoByExampleMapper;

public interface BaseSelectDtoMapper<T> extends
        SelectAllDtoMapper<T>,SelectDtoByExampleMapper<T> {
}
