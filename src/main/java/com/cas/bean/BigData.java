package com.cas.bean;

import java.util.Date;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2021/8/1 1:13 上午
 * @desc
 */
public class BigData {

    private String detail;
    private Date createtime;
    private Integer validity;

    public BigData() {
    }

    public BigData(String detail, Date createtime, Integer validity) {
        this.detail = detail;
        this.createtime = createtime;
        this.validity = validity;
    }

    public BigData(String detail, Integer validity) {
        this.detail = detail;
        this.validity = validity;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getValidity() {
        return validity;
    }

    public void setValidity(Integer validity) {
        this.validity = validity;
    }

    @Override
    public String toString() {
        return "BigData{" +
                "detail='" + detail + '\'' +
                ", createtime=" + createtime +
                ", validity=" + validity +
                '}';
    }
}
