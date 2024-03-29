package com.cas.service.impl;

import com.alibaba.fastjson.JSONObject;
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
import java.util.List;

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
//        threadPoolUtil.execute(() -> {
            for (int i = 0; i < 1; i++) {
                add10000();
            }
//        });
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
    public BigData queryById(String id) {
        BigData bigData = bigDataMapper.queryById(Integer.valueOf(id));
        log.info("==============从DB中获取数据，【{}】", bigData.toString());
        return bigData;
    }

    @Override
    public void update(int id) {
        int update = bigDataMapper.update(new BigData(id, "15811317734", 6));
        log.info("==============已修改, count=[{}]", update);
    }

    @Override
    public BigData queryByProcedure() {
        List<BigData> bigData = bigDataMapper.queryByProcedure();
        bigData.forEach(a -> {
            log.info(JSONObject.toJSONString(a));
        });
        return bigData.get(0);
    }
}
