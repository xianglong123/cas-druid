package com.cas.service.impl;

import com.cas.bean.BigData;
import com.cas.dao.BigDataMapper;
import com.cas.service.BigDataService;
import com.cas.util.StringUtil;
import com.cas.util.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2021/8/1 1:24 上午
 * @desc
 */
@Service
public class BigDataServiceImpl implements BigDataService {
    private static final Logger log = LoggerFactory.getLogger(BigDataServiceImpl.class);


    @Autowired
    private ThreadPoolUtil threadPoolUtil;

    @Resource
    private BigDataMapper bigDataMapper;

    @Override
    public void add() {
        threadPoolUtil.execute(() -> {
            for (int i = 0; i < 1; i++) {
                add10000();
            }
        });
    }

    private void add10000() {
        int count = 0;
        for (int i = 0; i <= 1; i ++) {
            int res = bigDataMapper.add(new BigData(StringUtil.getTel(), StringUtil.getOneNum()));
            count += res;
        }
        log.info("已完成" + count + " 条数据添加");
    }

    @Override
    public void queryById(String id) {
        BigData bigData = bigDataMapper.queryById(Integer.valueOf(id));
        log.info(bigData.toString());
    }
}
