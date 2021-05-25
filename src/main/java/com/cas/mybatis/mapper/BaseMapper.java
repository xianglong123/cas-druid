package com.cas.mybatis.mapper;

import com.cas.mybatis.base.BaseModel;

import java.util.List;

public interface BaseMapper<T extends BaseModel> {

    List<T> selectAll();

    int deleteByPrimaryKey(Integer id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(Integer id);

    int updateByPrimaryKey(T record);
}
