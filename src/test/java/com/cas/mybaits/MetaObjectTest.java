package com.cas.mybaits;

import com.cas.bean.User;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.junit.Test;

/**
 * @author xiang_long
 * @version 1.0
 * @date 2022/6/1 5:13 下午
 * @desc
 */
public class MetaObjectTest {

    @Test
    public void test() {
        User user = new User();
        Configuration configuration = new Configuration();
        MetaObject metaObject = configuration.newMetaObject(user);

        metaObject.setValue("detail.name", "xl");

        System.out.println(metaObject.getValue("detail.name"));

    }


}
