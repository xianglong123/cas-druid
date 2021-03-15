package com.cas.bean;

import java.util.Date;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 下午4:22 2021/3/15
 * @version: V1.0
 * @review:
 */
public class Account {

    private String id;
    private String userId;
    private Float balance;
    private Float freezeAmount;
    private Date createTime;
    private Date updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public Float getFreezeAmount() {
        return freezeAmount;
    }

    public void setFreezeAmount(Float freezeAmount) {
        this.freezeAmount = freezeAmount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
