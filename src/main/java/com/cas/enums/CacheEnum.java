package com.cas.enums;

import com.cas.bean.BigData;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2022/5/17 3:41 下午
 * @desc
 */
public enum CacheEnum {
    //
    BIG_DATA("CAS_DRUID_BIG_DATA_ID_PREFIX_", BigData.class, "id"),
    ;

    private String prefix;
    private Class<?> clazz;
    private String cacheKey;

    CacheEnum(String prefix, Class<?> clazz, String cacheKey) {
        this.prefix = prefix;
        this.clazz = clazz;
        this.cacheKey = cacheKey;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getCacheKey() {
        return cacheKey;
    }

    public void setCacheKey(String cacheKey) {
        this.cacheKey = cacheKey;
    }
}
