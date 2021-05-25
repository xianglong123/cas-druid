# cas-druid
    博客： https://www.cnblogs.com/ming-blogs/p/10288895.html

# 项目技术
    springboot2.0.X + gradle6.0+ + druid1.1.10

# 用到的表结构
```sql
create table if not exists user
(
	id bigint auto_increment comment '主键ID'
		primary key,
	name varchar(30) null comment '姓名',
	age int null comment '年龄',
	email varchar(50) null comment '邮箱',
	version int null comment '乐观锁',
	deleted int null,
	create_time datetime null comment '创建时间',
	update_time datetime null comment '更新时间'
);
-- 上面这张表和下面这张表不在同一个库中，关键是来测试多数据源的。

create table if not exists tcc_account.account
(
	id varchar(32) not null comment '主键ID'
		primary key,
	user_id varchar(128) not null comment '用户ID',
	balance decimal not null comment '用户余额',
	freeze_amount decimal not null comment '冻结金额，扣款暂存余额',
	create_time datetime not null,
	update_time datetime null
)
comment '账户余额表' collate=utf8mb4_bin;


```
    
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


## 总结一些mysql的知识
    索引的分类与简述
    唯一索引：
        唯一索引与普通索引类似，不同的就是：索引列的值必须唯一，但允许有空值。如果是组合索引，则列值的组合必须唯一。
        简单来说：唯一索引是加速查询 + 列表唯一（可以有null）。以下几种方式来创建：
        1）、创建唯一索引
        ```sql```
        CREATE UNIQUE INDEX indexName ON table(column(length))
        
        2）、修改表结构
        ```sql```
        ALTER TABLE table_name ADD UNIQUE indexName ON (column(length))
        
    主键索引：
        主键索引是一种特殊的唯一索引，一个表只能有一个主键，不允许有空值。简单来说：主键索引是加速查询 + 列值唯一（不可以有null）+ 表中只有一个。
           一般是在建表的时候同时创建主键索引：
           ```sql```
           CREATE TABLE mytable( ID INT NOT NULL, username VARCHAR(16) NOT NULL, PRIMARY KEY(ID) );
           当然也可以用 ALTER 命令。记住：一个表只能有一个主键
    
    组合索引：
        组合索引指在多个字段上创建的索引，只有在查询条件中使用了创建索引时的第一个字段，索引才会被使用。使用组合索引时遵循最左前缀集合。
        可以说：组合索引是多列值组成一个索引，专门用于组合索引，其效率大于索引合并。
        ```sql```
        ALTER TABLE `table` ADD INDEX name_city_age (name,city,age);
        
    全文索引：
        全文索引主要用来查找文本中的关键字，而不是直接与索引中的值相比较。fulltext索引跟其他索引大不相同，它更像是一个搜索引擎，而不是简单的
        where语句的参数匹配。fulltext索引配合match against操作使用，而不是一般的where语句加like. 它可以在create table, alter table,
        create index使用， 不过目前只有char， varchar，text列上可以创建全文索引。值得一提的是，在数据量较大的时候，现将数据放入
        一个没有全文索引的表中，然后再用create index创建fulltext索引，要比先为一张表建立fulltext然后再将数据写入的速度快很多。
        1）、创建表的适合添加全文索引
         CREATE TABLE `table` (
             `id` int(11) NOT NULL AUTO_INCREMENT ,
             `title` char(255) CHARACTER NOT NULL ,
             `content` text CHARACTER NULL ,
             `time` int(10) NULL DEFAULT NULL ,
             PRIMARY KEY (`id`),
             FULLTEXT (content)
         );
         
         2）、修改表结构添加全文索引
         ALTER TABLE article ADD FULLTEXT index_content(content)
         
         3）、直接创建索引
         CREATE FULLTEXT INDEX index_content ON article(content)
         
         4）、全文索引案例
         #######################         #######################         #######################
         -- 初始化表结构
         CREATE TABLE articles (
                                   id INT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
                                   title VARCHAR(200),
                                   body TEXT,
                                   FULLTEXT (title,body)
         ) ENGINE=InnoDB;
         
         -- 插入数据
         INSERT INTO articles (title,body) VALUES
         ('MySQL Tutorial','DBMS stands for DataBase ...'),
         ('How To Use MySQL Well','After you went through a ...'),
         ('Optimizing MySQL','In this tutorial we will show ...'),
         ('1001 MySQL Tricks','1. Never run mysqld as root. 2. ...'),
         ('MySQL vs. YourSQL','In the following database comparison ...'),
         ('MySQL Security','When configured properly, MySQL ...');
         
         
         -- 查询数据
         select * from articles  where match(title, body) AGAINST ('database' IN NATURAL LANGUAGE MODE );
         select * from articles  where match(title, body) AGAINST ('database');

         #######################         #######################         #######################
    
    聚簇索引：
        很简单记住一句话，找到了索引就找到了需要的数据，那么这个索引就是聚簇索引，所以主键就是聚簇索引，修改聚簇索引其实就是修改主键。
        当SQL查询的列就是索引本身时，我们称这种场景下该普通索引也可以叫做聚簇索引，MyisAM引擎没有聚簇索引
        【select no from student where no = 'test'】 这个时候no也是聚簇索引。
    
    非聚簇索引：
        索引的存储和数据的存储是分离的，也就是说找到了索引但没有找到数据，需要根据索引上的值（主键）再次回表查询，非聚簇索引也叫做辅助索引。
    
    一分钟明白MYSQL聚簇索引和非聚簇索引 【https://cloud.tencent.com/developer/article/1631424】

    MySQL 的覆盖索引与回表 【https://zhuanlan.zhihu.com/p/107125866】
    
    扩展：
    前缀索引[https://www.jianshu.com/p/fc80445044cc]：
    
    
    
### 演示索引优化
    初始化表
```sql
create table if not exists user
(
	id bigint auto_increment comment '主键ID'
		primary key,
	name varchar(30) null comment '姓名',
	age int null comment '年龄',
	email varchar(50) null comment '邮箱',
	version int null comment '乐观锁',
	deleted int null,
	create_time datetime null comment '创建时间',
	update_time datetime null comment '更新时间'
);

create index user_age_name_index
	on user (age, name);
```
    -- 不加age索引
    -- 1,SIMPLE,user,,ALL,,,,,20,10,Using where
    
    -- 加了索引【优化】
    -- 1,SIMPLE,user,,ref,user_age_index,user_age_index,5,const,1,100,Using index
    
    -- [不加组合索引，回表查询]：select id, age, name from user where age = 12
    -- 1,SIMPLE,user,,ref,user_age_index,user_age_index,5,const,1,100,null
    
    -- [加了组合索引]： select id, age, name from user where age = 12
    -- 1,SIMPLE,user,,ref,user_age_name_index,user_age_name_index,5,const,1,100,Using index
    -- 如果只有age的索引,查询列中包含name,则会从age这个非聚簇索引查询到id聚簇索引，然后通过回表通过id再查询name.效率底下，优化策略：建立组合索引（age, name）,删除age单索引，查询走索引不走回表，效率提升

    -- [分页查询优化]explain select id, age, name from user order by age limit 1,20;
    -- 加了age和name 的组合索引，sql会走index

    -- 待续....

### explain 【https://www.php.cn/mysql-tutorials-454417.html】
    * 表的读取顺序[id]
        id相同，执行顺序由上至下
        id不同，如果是子查询，id的序号会递增，id值越大优先级越高，越先被执行
    * 数据读取操作的操作类型[select_type]
        分别用来表示查询的类型，主要用于区别普通查询，联合查询，子查询等的复杂查询。
        SIMPLE 简单的select查询， 查询中不包含子查询或者UNION
        PRIMARY 查询中若 ·包含任何复杂的· 子部分，最外层查询则被标记为PRIMARY
        SUBQUERY 在SELECT或WHERE列表中包含了子查询
        DERIVED 在FORM 列表中包含的子查询被标记为DERIVED，MYSQL会递归执行这些子查询，把结果放在临时表中
        UNION若第二个SELECT出现在UNION之后，则被标记为UNION：若UNION包含在FROM子句的子查询中，外层SELECT将被标记为：DERIVED
        UNION RESULT 从UNION表获取结果的SELECT
    * 当前执行的表[table]
        tables  
    * 哪些索引可以使用[type]
        从最好的到最差的依次是： system > const > eq_ref > ref > range > index > all
        一般来说，得保证查询至少达到range级别，最好能达到ref
        * system 表只有一行记录（等于系统表），这是const类型的特例，平时不会出现，这个也可以忽略不计
        * const 表示通过索引一次就找到了，const用于比较primary key或者unique索引。因为只匹配一行数据，所以很快。
          如将主键置于where 列表中，Mysql就能将该查询转换为一个常量。
          1,SIMPLE,user,,const,PRIMARY,PRIMARY,8,const,1,100,
          explain select * from user where id = 4;
        * eq_ref 唯一性索引扫描,对于每个索引键，表中只有一条记录与之匹配。常见于主键或唯一索引扫描
        * ref 非唯一性索引扫描，返回匹配某个单独值的所有行，本质上也是一种索引访问，它返回所有匹配某个单独值的行，
          然而，它可能会找到多个符合条件的行，所以他应该属于查找和扫描的混合体。
        * range 只检索给定范围的行，使用一个索引来选择行，key列显示使用了哪个索引，一般就是在你的where语句中出现between、<、>、in等的查询，
        这种范围扫描索引比全表扫描要好，因为它只需要开始于索引的某一点，而结束于另一点，不用扫描全部索引。
        * index Full Index Scan, Index于ALL区别为index类型只遍历索引树。这通常比ALL快，因为索引文件通常比数据文件小。
        （也就是说虽然all和index都是读全表，但index是从索引中读取的，而all是从硬盘读取的）
        * all Full Table Scan将遍历全表以找到匹配的行   
    * 哪些索引被实际使用[possible_keys|key]
        possible_keys 显示可能应用在这张表中的索引，一个或多个。查询涉及到的字段上若存在索引，则该索引将被列出，但不一定被查询实际使用。
        key 实际使用的索引，如果为NULL，则没有使用索引。（可能原因包括没有建立索引或索引失效）
        查询中若使用了覆盖索引（select 后要查询的字段刚好和创建的索引字段完全相同），则该索引仅出现在key列表中
    * 表之间的引用[key_len]
        表示索引中使用的字节数，可通过该列计算查询中使用的索引的长度，在不损失精确性的情况下，长度越短越好。key_len显示的值为索引字段的最大可能长度，并非实际使用长度，即key_len是根据表定义计算而得，
        不是通过表内检索出的。
    * 每张表有多少行被优化器查询[ref]
        显示索引的那一列被使用了，如果可能的话，最好是一个常数。哪些列或常量被用于查找索引列上的值。
    * rows 
      根据表统计信息及索引选用情况，大致估算出找到所需的记录所需要读取的行数，也就是说，用的越少越好。
    * extra
      包含不适合在其他列中显式但十分重要的额外信息
      Using filesort（九死一生）
      说明mysql会对数据使用一个外部的索引排序，而不是按照表内的索引顺序进行读取。MySQL中无法利用索引完成的排序操作称为“文件排序”
      
      Using temporary（十死无生）
      使用了用临时表保存中间结果，MySQL在对查询结果排序时使用临时表。常见于排序order by和分组查询group by
      
      Using index（发财了）
      表示相应的select操作中使用了覆盖索引（Covering Index），避免访问了表的数据行，效率不错。如果同时出现using where，表明索引被用来执行索引键值的查找；如果没有同时出现using where，表明索引用来读取数据而非执行查找动作。

      Using where
      表明使用了where过滤

      Using join buffer
      表明使用了连接缓存,比如说在查询的时候，多表join的次数非常多，那么将配置文件中的缓冲区的join buffer调大一些。
      
      impossible where
      where子句的值总是false，不能用来获取任何元组

      distinct
      优化distinct操作，在找到第一匹配的元组后即停止找同样值的动作
   
### MySQL UNION 操作符
    select id, name from user union
    select id, title from articles;
![这是图片](./src/main/resources/static/union-2.jpg "Magic Gardens")
    select id, title from articles union
    select id, name from user;
![这是图片](./src/main/resources/static/union-1.jpg "Magic Gardens")


### mysql 优化策略
    设计表时：
        1、字段避免null值出现，null值很难查询优化且占用额外的索引空间，推荐默认数字0代替null。
        2、尽量使用INT而非BIGINT，如果非负则加上UNSIGNED（这样数值容量会扩大一倍），当然能使用TINYINT、SMALLINT、MEDIUM_INT更好。
        3、使用枚举或整数代替字符串类型
        4、尽量使用TIMESTAMP而非DATETIME
        5、单表不要有太多字段，建议在20以内
        6、用整型来存IP

    索引：
        1、索引并不是越多越好，要根据查询有针对性的创建，考虑在WHERE和ORDER BY命令上涉及的列建立索引，可根据EXPLAIN来查看是否用了索引还是全表扫描
        2、应尽量避免在WHERE子句中对字段进行NULL值判断，否则将导致引擎放弃使用索引而进行全表扫描
        3、值分布很稀少的字段不适合建索引，例如"性别"这种只有两三个值的字段
        4、字符字段只建前缀索引
        5、字符字段最好不要做主键
        6、不用外键，由程序保证约束
        7、尽量不用UNIQUE，由程序保证约束
        8、使用多列索引时主意顺序和查询条件保持一致，同时删除不必要的单列索引
      
    sql 的编写需要注意优化
          使用limit对查询结果的记录进行限定
          避免select *，将需要查找的字段列出来
          使用连接（join）来代替子查询
          拆分大的delete或insert语句
          可通过开启慢查询日志来找出较慢的SQL
          不做列运算：SELECT id WHERE age + 1 = 10，任何对列的操作都将导致表扫描，它包括数据库教程函数、计算表达式等等，查询时要尽可能将操作移至等号右边
          sql语句尽可能简单：一条sql只能在一个cpu运算；大语句拆小语句，减少锁时间；一条大sql可以堵死整个库
          OR改写成IN：OR的效率是n级别，IN的效率是log(n)级别，in的个数建议控制在200以内
          不用函数和触发器，在应用程序实现
          避免%xxx式查询
          少用JOIN
          使用同类型进行比较，比如用'123'和'123'比，123和123比
          尽量避免在WHERE子句中使用!=或<>操作符，否则将引擎放弃使用索引而进行全表扫描
          对于连续数值，使用BETWEEN不用IN：SELECT id FROM t WHERE num BETWEEN 1 AND 5
          列表数据不要拿全表，要使用LIMIT来分页，每页数量也不要太大   
    
    引擎
        MyISAM
          不支持行锁，读取时对需要读到的所有表加锁，写入时则对表加排它锁
          不支持事务
          不支持外键
          不支持崩溃后的安全恢复
          在表有读取查询的同时，支持往表中插入新纪录
          支持BLOB和TEXT的前500个字符索引，支持全文索引
          支持延迟更新索引，极大提升写入性能
          对于不会进行修改的表，支持压缩表，极大减少磁盘空间占用
    
        InnoDB
          支持行锁，采用MVCC来支持高并发
          支持事务
          支持外键
          支持崩溃后的安全恢复
          不支持全文索引
        
        总体来讲，MyISAM适合SELECT密集型的表，而InnoDB适合INSERT和UPDATE密集型的表

### 最左匹配原则
    我们创建组合索引的时候，比如（a,b,c）通过组合索引来创建索引树是先根据从左到右优先排序来构建的，所以我们可以看到这些索引的a列是有序的，b列c列是无序的。
    这就导致以下几种状况
    1、我们在查询数据的时候条件中必须要有a存在（最左条件存在）
    2、在全局b和c是无序的，所以不能使用范围查询，但是如果条件中指定a=x则b在这个前提下是有序的，所以可以使用范围查询，一次类推。
    3、如果跳过a，条件中直接用b和c则不采用索引
    4、like匹配：索引只适用于前缀匹配，中缀和后缀匹配都不走索引 （类似于前缀索引的功能）


### 好的博文
    MySql索引和结构深度解析!(多动图详细版)【https://zhuanlan.zhihu.com/p/364642137】
    MySql索引优化策略【https://www.cnblogs.com/qlqwjy/p/8592043.html】
    LRU算法【https://www.jianshu.com/p/d533d8a66795】
```sql
CREATE TABLE t5 (
                    c1 CHAR(1) NOT NULL DEFAULT 'a',
                    c2 CHAR(1) NOT NULL DEFAULT 'b',
                    c3 CHAR(1) NOT NULL DEFAULT 'c',
                    c4 CHAR(1) NOT NULL DEFAULT 'd',
                    c5 CHAR(1) NOT NULL DEFAULT 'e',
                    INDEX c1234(c1,c2,c3,c4)
);


explain select  * from t5 where c1 = 'a' order by c3, c2; -- 走文件排序
explain select  * from t5 where c1 = 'a' order by c2, c3, c4; -- 不走文件排序，走索引

explain select  c1, c4 from t5 where c1 = 'a' and c4 = 'd' group by c3, c2; -- 走临时表
explain select  c1, c4 from t5 where c1 = 'a' and c4 = 'd' group by c2, c3; -- 不走临时表，走索引
```

### 扩展优化实际
    1、延迟关联，就比如
        select * from user where age = 18;  和 
        select a.* from user a inner join (select id from user where age = 18) b on (a.id = b.id)
        上面两条语句第二条查询效率高，第一条是边查边回表，第二条是统一查id然后再一起通过id查数据，这个操作叫"延迟关联"
    
    2、索引转列，就是本列的数据不好创建索引，我们可以通过数据加密的方式将这列的数据加密存入额外列，在这个额外的列中创建索引，加密算法，crc32、md5、sha-1
```sql
-- 建表
CREATE TABLE t7(
    id INT AUTO_INCREMENT PRIMARY KEY,
    url VARCHAR(40),
    crcurl INT UNSIGNED NOT NULL DEFAULT 0);

-- 初始化数据
mysql> insert into t7(url) values('http://www.baidu.com');
Query OK, 1 row affected (0.10 sec)

mysql> insert into t7(url) values('http://www.zixue.com');
Query OK, 1 row affected (0.10 sec)

mysql> insert into t7(url) values('http://www.qlq.com');
Query OK, 1 row affected (0.19 sec)

-- 更新|加密数据
mysql> update t7 set crcurl = crc32(url);
Query OK, 3 rows affected (0.22 sec)
Rows matched: 3  Changed: 3  Warnings: 0

mysql> select * from t7;
+----+----------------------+------------+
| id | url                  | crcurl     |
+----+----------------------+------------+
|  7 | http://www.baidu.com | 3500265894 |
|  9 | http://www.zixue.com | 2107636668 |
| 11 | http://www.qlq.com   | 2605661224 |
+----+----------------------+------------+
3 rows in set (0.00 sec)

-- 创建索引
mysql> alter table t7 add index url(url(15));
Query OK, 0 rows affected (0.51 sec)
Records: 0  Duplicates: 0  Warnings: 0

mysql> alter table t7 add index crcurl(crcurl);
Query OK, 0 rows affected (0.43 sec)
Records: 0  Duplicates: 0  Warnings: 0

-- 效果比对
mysql> explain select * from t7 where url='http://www.baidu.com'\G
*************************** 1. row ***************************
           id: 1
  select_type: SIMPLE
        table: t7
   partitions: NULL
         type: ref
possible_keys: url
          key: url
      key_len: 48
          ref: const
         rows: 1
     filtered: 100.00
        Extra: Using where
1 row in set, 1 warning (0.00 sec)

mysql> explain select * from t7 where crcurl=crc32('http://www.baidu.com')\G
*************************** 1. row ***************************
           id: 1
  select_type: SIMPLE
        table: t7
   partitions: NULL
         type: ref
possible_keys: crcurl
          key: crcurl
      key_len: 4
          ref: const
         rows: 1
     filtered: 100.00
        Extra: NULL
1 row in set, 1 warning (0.00 sec)

-- 所以，上面的方法可以说是将对一列的索引转化为另一列，最好使用整型(长度短，节约空间)。利用一定的算法将相似度较高的数据存到另一列并添加索引。
```
    3、索引平衡，创建前缀索引减少索引占用空间，并在区分度允许的区间取平衡值
        区分度计算sql: select count(distinct left(word,6))/count(*) from dict;
        对于一般的系统应用: 区别度能达到0.1,索引的性能就可以接受.
    
    4、排序分组走索引，通过组合索引优化order by 和 group by ，避免使用文件排序和临时表
    
    总结：理想的索引应包含 1:查询频繁 2:区分度高  3:长度小  4: 尽量能覆盖常用查询字段.

### 单行函数
    1、字符函数
    length
    concat
    substr
    lpad
    rpad
    upper
    lower
    replace
    instr
    trim
    2、数学函数
    round
    ceil
    floor
    truncate
    mod
    3、日期函数
    now
    curdate
    curtime
    year
    month
    monthname
    day
    hour
    minute
    second
    str_to_date
    date_format
    datadiff
    4、其他函数
    version
    databases
    user
    tables
    5、控制函数
    if
    case
    
```sql
-- length
select length('123');

-- concat || upper || lower
select concat(upper('abc'), lower('efd')) as 姓名;

-- substr
select substr('李莫愁爱上了陆展元', 7) out_put;
select substr('李莫愁爱上了陆展元', 1, 3) out_put;

-- 姓名第一个字母大写
select concat(upper(substr('xianglong', 1, 1)), lower(substr('xianglong', 2))) out_put;

-- instr 返回某个子串第一个索引，没有返回0
select instr('xianglong', 'long') out_put;

-- trim 过滤空格
select trim('       xianglong      ') out_put;
select length(trim('       xianglong      ')) out_put;
select trim('a' from 'aaaaaaaaaaxiangaaalongaaa') out_put;

-- lpad 用指定的字符实现左填充指定长度
select lpad('xiang',10,'*') out_put;

-- rpad 用指定的字符实现右填充指定长度
select rpad('xiang',12,'*') out_put;

-- replace 替换函数
select replace('我喜欢你', '你', '她') out_put;

-- 数学函数
-- round 四舍五入
select round(-1.55);
select round(-1.567, 2);

-- ceil 向上取整 返回>=参数的最小整数
select ceil(1.01);

-- floor 向下取整，返回<=参数的最大整数
select floor(-9.99);

-- truncate 截断
select truncate(1.6999, 2);

-- mod 取余
select mod(10,-3);

-- 日期函数
-- now 返回当前系统时间 + 日期
select now();

-- curdate 返回当前系统日期，不包含日期
select curdate();

-- curtime 返回当前时间，不包含日期
select curtime();

-- 可以获取指定的部分，年，月，日，小时，分钟，秒
select year(now()) 年;
select year('1999-1-1') 年;
select month(now()) 月;
select monthname(now()) 月;

-- str_to_date 将日期格式的字符转换成指定格式的日期
# %Y    四位的年份
# %y    2位的年份
# %m    月份（01,02...11,12）
# %c    月份（1,2...11,12）
# %d    日（01,02...）
# %H    小时（24小时制）
# %h    小时（12小时制）
# %i    分钟（00,01...59）
# %s    秒（00,01...59）
select str_to_date('1992-1-1 12:12:59', '%Y-%m-%d %H:%i:%s')

-- date_format 将日期转换成字符
select date_format(now(), '%Y-%m-%d') out_put;

-- datediff 求两个日期之间的天数
select datediff(now(), '1996-9-14');

-- 其他函数
select version();
select database();
select user();

-- 流程控制函数
-- if函数， if else的效果
select if(10>5, '大', '小');

-- case函数的使用： switch case 的效果
select id, age,
case age
when 12 then age*1
when 26 then age*2
else age
end as new_age
from user;

select id, age,
case
when age < 13 then 'young'
when age > 25 then 'old'
else age
end as leavel
from user;
```

### 分组函数
    功能：用作统计使用，又称为聚合函数或统计函数或组函数
    分类：
    sum 求和、avg 平均值、max 最大值、min 最小值、 count 计算个数
    
```sql
-- sum 求和
select sum(age) from user ;
select avg(age) from user ;
select max(age) from user ;
select min(age) from user ;
select count(age) from user ;

select sum(age), round(avg(age), 2), max(age), min(age), count(age) from user ;

-- 是否忽略null

-- 和distinct搭配 去重
select sum(distinct age) from user;
select sum(age) from user;
```    

### group
    语法：
        select 分组函数,列（要求出现在group by的后面）
        from 表
        【where 筛选条件】
        group by 分组的列表
        【order by 子句】
    注意：
        查询列表必须特殊，要求是分组函数和group by 后出现的字段
    特点：
        1、分组查询中的筛选条件分为两类
                        数据源             位置                   关键字
        分组前筛选        原始类             group by子句的前面       where
        分组后筛选        分组后的结果集       group by子句的后面       having
        
        1⃣️、分组函数做条件肯定是放在having子句中
        2⃣️、能用分组前筛选的，就优先考虑使用分组前筛选
        
        2、group by 子句支持单个字段分组，多个字段分组（多个字段之间用逗号隔开没有顺序要求）
        3、也可以添加排序（排序是放在所有条件之后的）

### 逆向工程详细介绍
    <?xml version="1.0" encoding="UTF-8"?>
    <!DOCTYPE generatorConfiguration
            PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
            "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
    <!--配置节点的顺序不能变-->
    <generatorConfiguration>
        <!--参考资料：https://www.cnblogs.com/xiaocao1434/p/8797636.html-->
    
        <!--加载mybatis逆向工程需要的jar包位置，比如说需要数据库驱动包，需要指定一个驱动包的完整路径（必须）-->
        <classPathEntry location="C:\Users\chuyunfei\.m2\repository\mysql\mysql-connector-java\5.1.6\mysql-connector-java-5.1.6.jar"/>
    
        <!--
            context:生成一组对象的环境
                id:必选，上下文id，用于在生成错误时提示
                defaultModelType:指定生成对象的样式
                    1，conditional：类似hierarchical；
                    2，flat：所有内容（主键，blob）等全部生成在一个对象中；
                    3，hierarchical：主键生成一个XXKey对象(key class)，Blob等单独生成一个对象，其他简单属性在一个对象中(record class)
                targetRuntime:
                    1，MyBatis3：默认的值，生成基于MyBatis3.x以上版本的内容，包括XXXBySample；
                    2，MyBatis3Simple：类似MyBatis3，只是不生成XXXBySample；
                introspectedColumnImpl：类全限定名，用于扩展MBG
        -->
        <context id="mysql" defaultModelType="hierarchical" targetRuntime="MyBatis3Simple">
    
            <!--
                自动识别数据库关键字，默认false。
                设置为true时，根据SqlReservedWords中定义的关键字列表。
                一般保留默认值，遇到数据库关键字（Java关键字），使用columnOverride覆盖
             -->
            <property name="autoDelimitKeywords" value="false"/>
    
            <!-- 生成的Java文件的编码 -->
            <property name="javaFileEncoding" value="UTF-8"/>
    
            <!-- 格式化java代码 -->
            <property name="javaFormatter" value="org.mybatis.generator.api.dom.DefaultJavaFormatter"/>
    
            <!-- 格式化XML代码 -->
            <property name="xmlFormatter" value="org.mybatis.generator.api.dom.DefaultXmlFormatter"/>
    
            <!--
                beginningDelimiter和endingDelimiter：指明数据库的用于标记数据库对象名的符号。
                比如：ORACLE就是双引号，MYSQL默认是`反引号；
            -->
            <property name="beginningDelimiter" value="`"/>
            <property name="endingDelimiter" value="`"/>
    
            <!--配置数据库的连接：必须有！！-->
            <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                            connectionURL="jdbc:mysql:///service_system"
                            userId="root" password="root">
                <!--设置Driver的属性，里面的设置是Driver的属性，并不是连接属性-->
                <!--<property name="" value=""/>-->
            </jdbcConnection>
    
            <!--
                java类型处理器
                    用于处理DB中的类型到Java中的类型，默认使用JavaTypeResolverDefaultImpl；
                    注意：默认会先尝试使用Integer，Long，Short等来对应DECIMAL和 NUMERIC数据类型；
            -->
            <javaTypeResolver type="org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl">
                <!--
                    true：使用BigDecimal对应DECIMAL和 NUMERIC数据类型
                    false：默认,
                        scale>0;length>18：使用BigDecimal;
                        scale=0;length[10,18]：使用Long；
                        scale=0;length[5,9]：使用Integer；
                        scale=0;length<5：使用Short；
                 -->
                <property name="forceBigDecimals" value="false"/>
            </javaTypeResolver>
    
    
            <!--
                java模型创建器，是必须要的元素，负责：
                    1，key类（见context的defaultModelType）；
                    2，java类；
                    3，查询类
                targetPackage：生成的类要放的包，真实的包受enableSubPackages属性控制；
                targetProject：目标项目，指定一个存在的目录下，生成的内容会放到指定目录中，如果目录不存在，MBG不会自动建目录
             -->
            <javaModelGenerator targetPackage="com.copm.model" targetProject="src/main/java">
    
                <!--自动为每一个生成的类创建一个构造方法，构造方法包含了所有的field；而不是使用setter-->
                <property name="constructorBased" value="false"/>
    
                <!-- 在targetPackage的基础上，根据数据库的schema再生成一层package，最终生成的类放在这个package下，默认为false -->
                <property name="enableSubPackages" value="true"/>
    
                <!--
                    是否创建一个不可变的类。
                    如果为true，那么MBG会创建一个没有setter方法的类，取而代之的是类似constructorBased的类
                 -->
                <property name="immutable" value="false"/>
    
                <!--
                    设置一个根对象。
                    如果设置了这个根对象，那么生成的keyClass或者recordClass会继承这个类；在Table的rootClass属性中可以覆盖该选项
                    注意：如果在key class或者record class中有root class相同的属性，MBG就不会重新生成这些属性了，包括：
                        1，属性名相同，类型相同，有相同的getter/setter方法；
                 -->
                <!--<property name="rootClass" value="com.copm.domain"/>-->
    
                <!-- 设置是否在getter方法中，对String类型字段调用trim()方法 -->
                <property name="trimStrings" value="false"/>
            </javaModelGenerator>
    
    
            <!--
                生成SQL map的XML文件生成器。
                注意：在Mybatis3之后，我们可以使用mapper.xml文件+Mapper接口（或者不用mapper接口），或者只使用Mapper接口+Annotation。
                    所以，如果 javaClientGenerator配置中配置了需要生成XML的话，这个元素就必须配置
                targetPackage/targetProject:同javaModelGenerator
             -->
            <sqlMapGenerator targetPackage="com.copm.xml" targetProject="src/main/resources">
                <!-- 在targetPackage的基础上，根据数据库的schema再生成一层package，最终生成的类放在这个package下，默认为false -->
                <property name="enableSubPackages" value="true"/>
            </sqlMapGenerator>
    
    
            <!--
                对于mybatis来说，即生成Mapper接口。
                注意：如果没有配置该元素，那么默认不会生成Mapper接口
                targetPackage/targetProject:同javaModelGenerator
                type：选择怎么生成mapper接口（在MyBatis3/MyBatis3Simple下）：
                    1，ANNOTATEDMAPPER：会生成使用Mapper接口+Annotation的方式创建（SQL生成在annotation中），不会生成对应的XML；
                    2，MIXEDMAPPER：使用混合配置，会生成Mapper接口，并适当添加合适的Annotation，但是XML会生成在XML中；
                    3，XMLMAPPER：会生成Mapper接口，接口完全依赖XML；
                注意，如果context是MyBatis3Simple：只支持ANNOTATEDMAPPER和XMLMAPPER
            -->
            <javaClientGenerator targetPackage="com.copm.mapper" type="XMLMAPPER" targetProject="src/main/java">
                <!-- 在targetPackage的基础上，根据数据库的schema再生成一层package，最终生成的类放在这个package下，默认为false -->
                <property name="enableSubPackages" value="true"/>
    
                <!-- 可以为所有生成的接口添加一个父接口，但是MBG只负责生成，不负责检查-->
                <!--<property name="rootInterface" value=""/>-->
    
            </javaClientGenerator>
    
            <!--
                选择一个table来生成相关文件，可以有一个或多个table，必须要有table元素：多个table标签生成多个对应产物
                选择的table会生成以下文件：
                    1，SQL map文件
                    2，生成一个主键类；
                    3，除了BLOB和主键的其他字段的类；
                    4，包含BLOB的类；
                    5，一个用户生成动态查询的条件类（selectByExample, deleteByExample），可选；
                    6，Mapper接口（可选）
    
                tableName（必要）：要生成对象的表名；
                注意：大小写敏感问题。正常情况下，MBG会自动的去识别数据库标识符的大小写敏感度，在一般情况下，MBG会
                    根据设置的schema，catalog或tablename去查询数据表，按照下面的流程：
                    1，如果schema，catalog或tablename中有空格，那么设置的是什么格式，就精确的使用指定的大小写格式去查询；
                    2，否则，如果数据库的标识符使用大写的，那么MBG自动把表名变成大写再查找；
                    3，否则，如果数据库的标识符使用小写的，那么MBG自动把表名变成小写再查找；
                    4，否则，使用指定的大小写格式查询；
                另外：如果在创建表的时候，使用的""把数据库对象规定大小写，就算数据库标识符是使用的大写，在这种情况下也会使用给定的大小写来创建表名；
                    这个时候，请设置delimitIdentifiers="true"即可保留大小写格式；
    
                可选：
                    1，schema：数据库的schema；
                    2，catalog：数据库的catalog；
                    3，alias：为数据表设置的别名，如果设置了alias，那么生成的所有的SELECT SQL语句中，列名会变成：alias_actualColumnName
                    4，domainObjectName：生成的domain类的名字，如果不设置，直接使用表名作为domain类的名字；可以设置为somepck.domainName，那么会自动把domainName类再放到somepck包里面；
                    5，enableInsert（默认true）：指定是否生成insert语句；
                    6，enableSelectByPrimaryKey（默认true）：指定是否生成按照主键查询对象的语句（就是getById或get）；
                    7，enableSelectByExample（默认true）：MyBatis3Simple为false，指定是否生成动态查询语句；
                    8，enableUpdateByPrimaryKey（默认true）：指定是否生成按照主键修改对象的语句（即update)；
                    9，enableDeleteByPrimaryKey（默认true）：指定是否生成按照主键删除对象的语句（即delete）；
                    10，enableDeleteByExample（默认true）：MyBatis3Simple为false，指定是否生成动态删除语句；
                    11，enableCountByExample（默认true）：MyBatis3Simple为false，指定是否生成动态查询总条数语句（用于分页的总条数查询）；
                    12，enableUpdateByExample（默认true）：MyBatis3Simple为false，指定是否生成动态修改语句（只修改对象中不为空的属性）；
                    13，modelType：参考context元素的defaultModelType，相当于覆盖；
                    14，delimitIdentifiers：参考tableName的解释，注意，默认的delimitIdentifiers是双引号，如果类似MYSQL这样的数据库，使用的是`（反引号，那么还需要设置context的beginningDelimiter和endingDelimiter属性）
                    15，delimitAllColumns：设置是否所有生成的SQL中的列名都使用标识符引起来。默认为false，delimitIdentifiers参考context的属性
    
                注意，table里面很多参数都是对javaModelGenerator，context等元素的默认属性的一个复写；
             -->
            <table tableName="userinfo" >
    
                <!-- 参考 javaModelGenerator 的 constructorBased属性-->
                <property name="constructorBased" value="false"/>
    
                <!-- 默认为false，如果设置为true，在生成的SQL中，table名字不会加上catalog或schema； -->
                <property name="ignoreQualifiersAtRuntime" value="false"/>
    
                <!-- 参考 javaModelGenerator 的 immutable 属性 -->
                <property name="immutable" value="false"/>
    
                <!-- 指定是否只生成domain类，如果设置为true，只生成domain类，如果还配置了sqlMapGenerator，那么在mapper XML文件中，只生成resultMap元素 -->
                <property name="modelOnly" value="false"/>
    
                <!-- 参考 javaModelGenerator 的 rootClass 属性
                <property name="rootClass" value=""/>
                 -->
    
                <!-- 参考javaClientGenerator 的  rootInterface 属性
                <property name="rootInterface" value=""/>
                -->
    
                <!-- 如果设置了runtimeCatalog，那么在生成的SQL中，使用该指定的catalog，而不是table元素上的catalog
                <property name="runtimeCatalog" value=""/>
                -->
    
                <!-- 如果设置了runtimeSchema，那么在生成的SQL中，使用该指定的schema，而不是table元素上的schema
                <property name="runtimeSchema" value=""/>
                -->
    
                <!-- 如果设置了runtimeTableName，那么在生成的SQL中，使用该指定的tablename，而不是table元素上的tablename
                <property name="runtimeTableName" value=""/>
                -->
    
                <!--
                    注意，该属性只针对MyBatis3Simple有用；
                    如果选择的runtime是MyBatis3Simple，那么会生成一个SelectAll方法，如果指定了selectAllOrderByClause，那么会在该SQL中添加指定的这个order条件；
                 -->
                <!--<property name="selectAllOrderByClause" value="age desc,username asc"/>-->
    
                <!-- 如果设置为true，生成的model类会直接使用column本身的名字，而不会再使用驼峰命名方法，比如BORN_DATE，生成的属性名字就是BORN_DATE,而不会是bornDate -->
                <property name="useActualColumnNames" value="false"/>
    
                <!-- generatedKey用于生成生成主键的方法，
                    如果设置了该元素，MBG会在生成的**<insert>**元素中生成一条正确的<selectKey>元素，该元素可选
                    column:主键的列名；
                    sqlStatement：要生成的selectKey语句，有以下可选项：
                        Cloudscape:相当于selectKey的SQL为： VALUES IDENTITY_VAL_LOCAL()
                        DB2       :相当于selectKey的SQL为： VALUES IDENTITY_VAL_LOCAL()
                        DB2_MF    :相当于selectKey的SQL为：SELECT IDENTITY_VAL_LOCAL() FROM SYSIBM.SYSDUMMY1
                        Derby      :相当于selectKey的SQL为：VALUES IDENTITY_VAL_LOCAL()
                        HSQLDB      :相当于selectKey的SQL为：CALL IDENTITY()
                        Informix  :相当于selectKey的SQL为：select dbinfo('sqlca.sqlerrd1') from systables where tabid=1
                        MySql      :相当于selectKey的SQL为：SELECT LAST_INSERT_ID()
                        SqlServer :相当于selectKey的SQL为：SELECT SCOPE_IDENTITY()
                        SYBASE      :相当于selectKey的SQL为：SELECT @@IDENTITY
                        JDBC      :相当于在生成的insert元素上添加useGeneratedKeys="true"和keyProperty属性
                <generatedKey column="" sqlStatement=""/>
                 -->
    
                <!--
                    该元素会在根据表中列名计算对象属性名之前先重命名列名，非常适合用于表中的列都有公用的前缀字符串的时候，
                    比如列名为：CUST_ID,CUST_NAME,CUST_EMAIL,CUST_ADDRESS等；
                    那么就可以设置searchString为"^CUST_"，并使用空白替换，那么生成的Customer对象中的属性名称就不是
                    custId,custName等，而是先被替换为ID,NAME,EMAIL,然后变成属性：id，name，email；
    
                    注意，MBG是使用java.util.regex.Matcher.replaceAll来替换searchString和replaceString的，
                    如果使用了columnOverride元素，该属性无效；
    
                <columnRenamingRule searchString="" replaceString=""/>
                 -->
    
                <!-- 用来修改表中某个列的属性，MBG会使用修改后的列来生成domain的属性；
                    column:要重新设置的列名；
                    注意，一个table元素中可以有多个columnOverride元素哈~
                 -->
                <columnOverride column="username">
    
                    <!-- 使用property属性来指定列要生成的属性名称 -->
                    <property name="property" value="userName"/>
    
                    <!-- javaType用于指定生成的domain的属性类型，使用类型的全限定名
                    <property name="javaType" value=""/>
                     -->
    
                    <!-- jdbcType用于指定该列的JDBC类型
                    <property name="jdbcType" value=""/>
                     -->
    
                    <!-- typeHandler 用于指定该列使用到的TypeHandler，如果要指定，配置类型处理器的全限定名
                        注意，mybatis中，不会生成到mybatis-config.xml中的typeHandler
                        只会生成类似：where id = #{id,jdbcType=BIGINT,typeHandler=com._520it.mybatis.MyTypeHandler}的参数描述
                    <property name="jdbcType" value=""/>
                    -->
    
                    <!-- 参考table元素的delimitAllColumns配置，默认为false
                    <property name="delimitedColumnName" value=""/>
                     -->
                </columnOverride>
    
                <!-- ignoreColumn设置一个MGB忽略的列，如果设置了改列，那么在生成的domain中，生成的SQL中，都不会有该列出现
                    column:指定要忽略的列的名字；
                    delimitedColumnName：参考table元素的delimitAllColumns配置，默认为false
    
                    注意，一个table元素中可以有多个ignoreColumn元素
                <ignoreColumn column="deptId" delimitedColumnName=""/>
                -->
            </table>
    
        </context>
    
    </generatorConfiguration>
 











