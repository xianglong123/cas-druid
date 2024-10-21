package com.cas.dao;

import com.cas.bean.Account;
import com.cas.bean.AccountPage;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 下午4:24 2021/3/15
 * @version: V1.0
 * @review:
 */
@Mapper
public interface AccountMapper {

    Account queryById(String userId);;

    Account queryById2(Account account);

    List<Account> findByIds(List<String> list);

    List<Account> find(AccountPage account);

}
