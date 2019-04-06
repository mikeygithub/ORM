package com.mikey.util;

import java.sql.PreparedStatement;

/**
 * @Program: ORM
 * @Author: 麦奇
 * @Email： 1625017540@qq.com
 * @Create: 2019-04-06 15:50
 * @Describe：封装了JDBC查询常用的操作
 **/
public class JDBCUtil {

    public static void handleParams(PreparedStatement preparedStatement,Object[] params) {
        if (params!=null){
            for (int i = 0; i < params.length; i++) {
                try {
                    preparedStatement.setObject(1+i,params[i]);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

}
