package com.cas.dao;

import com.cas.aspect.QueryCache;
import com.cas.aspect.UpdateResetCache;
import com.cas.bean.BigData;
import com.cas.enums.CacheEnum;

import java.util.List;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2021/8/1 1:12 上午
 * @desc 大数据单表模拟优化
 */
public interface BigDataMapper {

    int add(BigData bigData);

    BigData queryById(Integer id);

    @UpdateResetCache(CacheEnum.BIG_DATA)
    int update(BigData bigData);

    List<BigData> queryByProcedure();
}
