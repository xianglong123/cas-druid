# cas-druid
    博客： https://www.cnblogs.com/ming-blogs/p/10288895.html

# 项目技术
    springboot2.0.X + gradle6.0+ + druid1.1.10
    
# 项目结构
    applicaiton.yaml: 只测试druid和密码回调
        相关类：
            DbPasswordCallback.java
        
    application-uat.yaml: 多数据源配置
        相关类：
            TargetDataSource.java 主要用于在方法上使用注解，修改其调用的数据源
            CommonConstant.java   给数据源一个独有的标志
            DataSourceAspect.java 切面，切自定义注解然后将注解中的数据获取，从而选中其代表的数据源
            DynamicDataSource.java 较为核心的方法，其中determineCurrentLookupKey()方法最重要，返回的是数据源的标识，除了默认数据源，其他数据源
                                   以map形式保存，我们传入key就能获取其数据源对象。
            DynamicDataSourceConfig.java 这里我们获取我们配置的多数据源并放入数据源列表。
            
    注意：我们如果除了这个不配置其他的，我们会一个错误就是"循环依赖"，主要是因为数据源除了我们配置的，druid还有一个默认的数据源会加载，我们要将其禁止掉。
    在启动类上加如下代码：
    
    @Import({DynamicDataSourceConfig.class})
    @SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })                      
    public class DruidApplication {
    
        public static void main(String[] args) {
            SpringApplication.run(DruidApplication.class, args);
        }
    
    }
    
# 读写分离
    思路：对每个方法要求，写的以add,update,delete开头，读的以query开头，然后用切面正则匹配。匹配之后用环绕通知对方法进行加强，改变其调用数据源


# 总结
    多数据源测试的时候，修改环境为uat
