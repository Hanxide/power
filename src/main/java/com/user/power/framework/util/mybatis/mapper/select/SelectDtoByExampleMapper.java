package com.user.power.framework.util.mybatis.mapper.select;

import com.user.power.framework.domain.Dto;
import com.user.power.framework.util.mybatis.DtoProvider;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface SelectDtoByExampleMapper<T> {
    @SelectProvider(type = DtoProvider.class, method = "dynamicSQL")
    List<Dto> selectDtoByExample(Object example);
}
