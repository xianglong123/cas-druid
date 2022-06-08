package com.cas.controller;

import cn.hutool.extra.spring.SpringUtil;
import com.cas.bean.Account;
import com.cas.bean.AccountPage;
import com.cas.dao.AccountMapper;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2022/5/18 9:46 上午
 * @desc
 */
@RequestMapping("/account")
@RestController
@Import(SpringUtil.class)
public class AccountController {

    @Resource
    private AccountMapper accountMapper;

    @RequestMapping("/queryById")
    public String queryById(String id) {
        accountMapper.queryById(id);
        return "ok";
    }

    @RequestMapping("/queryById2")
    public String queryById2(String id) {
        Account account = new Account();
        account.setUserId(id);
        accountMapper.queryById2(account);
        return "ok";
    }

    @RequestMapping("/findByIds")
    public String findByIds() {
        accountMapper.findByIds(Arrays.asList("1", "2"));
        return "ok";
    }


    @RequestMapping("/find")
    public String find() {
        AccountPage accountPage = new AccountPage();
        accountPage.setPageSize(2);
        accountPage.setPageNum(1);
        List<Account> accounts = accountMapper.find(accountPage);
        return "查询数量：" + accounts.size();
    }

}
