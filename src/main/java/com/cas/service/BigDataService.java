package com.cas.service;

import com.cas.bean.BigData;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2021/8/1 1:23 上午
 * @desc
 */
public interface BigDataService {

    void add();

    BigData queryById(String id);

    void update(int id);
}
