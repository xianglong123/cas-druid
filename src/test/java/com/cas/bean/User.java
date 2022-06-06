package com.cas.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 上午11:39 2021/3/14
 * @version: V1.0
 * @review:
 *
 * create table user
 * (
 *     id          bigint auto_increment comment '主键ID'
 *         primary key,
 *     name        varchar(30) null comment '姓名',
 *     age         int         null comment '年龄',
 *     email       varchar(50) null comment '邮箱',
 *     version     int         null comment '乐观锁',
 *     deleted     int         null,
 *     create_time datetime    null comment '创建时间',
 *     update_time datetime    null comment '更新时间'
 * );
 */
public class User {

    private String id;
    private String name;
    private int age;
    private String email;
    private int version;
    private Detail detail;
    private int deleted;
    private Map<String, Object> map = new HashMap<>();
    private Date createTime;
    private Date updateTime;

    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setMap(Map<String, Object> map) {
        this.map = map;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
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
