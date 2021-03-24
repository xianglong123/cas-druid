package com.cas.config.dynamic;

import lombok.Data;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 上午10:57 2021/3/15
 * @version: V1.0
 * @review:
 */
@Data
public class CommonConstant {

    /**
     * 主数据源
     */
    public static final String MASTER_DATASOURCE = "masterDruidDataSource";

    /**
     * 从数据源
     */
    public static final String SLAVE_DATASOURCE = "slaveDruidDataSource";


    public static String getMasterDatasource() {
        return MASTER_DATASOURCE;
    }

    public static String getSlaveDatasource() {
        return SLAVE_DATASOURCE;
    }
}
