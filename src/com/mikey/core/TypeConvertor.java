package com.mikey.core;

/**
 * @Program: ORM
 * @Author: 麦奇
 * @Email： 1625017540@qq.com
 * @Create: 2019-04-06 15:43
 * @Describe：
 **/
public interface TypeConvertor {

    /**
     * 将数据库类型转换为java类型
     * @param columnType
     * @return
     */
    public String databaseType2JavaType(String columnType);

    /**
     * 将java类型转换为数据库类型
     * @param javaDataType
     * @return
     */
    public String javaType2DatabaseType(String javaDataType);



}
