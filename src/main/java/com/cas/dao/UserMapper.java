package com.cas.dao;

import com.cas.bean.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 上午11:42 2021/3/14
 * @version: V1.0
 * @review:
 */
@Mapper
public interface UserMapper {

    User queryByName(String name);

}
