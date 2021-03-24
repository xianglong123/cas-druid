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
    @ConditionalOnBean（仅仅在当前上下文中存在某个对象时，才会实例化一个Bean）
    @ConditionalOnClass（某个class位于类路径上，才会实例化一个Bean）
    @ConditionalOnExpression（当表达式为true的时候，才会实例化一个Bean）
    @ConditionalOnMissingBean（仅仅在当前上下文中不存在某个对象时，才会实例化一个Bean）
    @ConditionalOnMissingClass（某个class类路径上不存在的时候，才会实例化一个Bean）
    @ConditionalOnNotWebApplication（不是web应用）
    @EnableConfigurationProperties
    如果一个配置类只配置@ConfigurationProperties注解，而没有使用@Component，那么在IOC容器中是获取不到properties 配置文件转化的bean。 
    @EnableConfigurationProperties 相当于把使用 @ConfigurationProperties 的类进行了一次注入。
    
## mybatis学习网站
    基础知识：http://c.biancheng.net/view/4320.html
    一级缓存：https://mp.weixin.qq.com/s?__biz=MzkwMDE1MzkwNQ==&mid=2247496101&idx=1&sn=8d32c975eb41744903bb6331a500c28d&source=41#wechat_redirect
    二级缓存：https://www.cnblogs.com/cxuanBlog/p/11333021.html
