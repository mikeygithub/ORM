#动手简单实现ORM

##1.简单类
ColumnInfo：封装了表字段的信息

Configuration：配置类封装了配置信息

JavaFieldGetSet:属性的get/set信息

TableInfo:表的相关信息

##2.核心类
CallBack：回调执行SQL

DBManager：数据库管理/获取连接/放回连接

MySqlQuery：Mysql数据库查询的子类

MySqlTypeConvertor：Mysql转java的转换类

Query：查询类/抽象

QueryFactory：查询类工厂

TableContext：表的上下文

TypeConvertor：类型转换接口：java转数据库/数据库转java
##3.工具类
JavaFileUtil：根据表字段信息转为相关的java属性

JDBCUtil：封装了JDBC查询常用的操作

RefUtil：反射工具类/反射调用get/set

StringUtil：封装了字符串常用的操作/首字母大写

##4.数据库连接池

DBConnPool:数据库连接池
