package com.cas.dao;

import com.cas.bean.Account;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 下午4:24 2021/3/15
 * @version: V1.0
 * @review:
 */
@Mapper
public interface AccountMapper {

    Account queryById(String userId);

}
