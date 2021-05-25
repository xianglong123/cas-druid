package com.cas.mybaits;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: xianglong[1391086179@qq.com]
 * @date: 下午12:24 2021/5/23
 * @version: V1.0
 * @review:
 */
public class GeneratorTest {

    public void generator() throws Exception{
        List warnings = new ArrayList();
        boolean overwrite = true;
        File configFile = new File("/Users/xianglong/IdeaProjects/cas-druid/src/main/resources/mybatis/generatorConfig.xml");
        ConfigurationParser cp = new ConfigurationParser(warnings);
        Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
        myBatisGenerator.generate(null);
    }


    public static void main(String[] args) throws Exception {
        GeneratorTest generatorTest = new GeneratorTest();
        generatorTest.generator();
    }

}
