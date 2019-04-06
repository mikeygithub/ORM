package com.mikey.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @Program: ORM
 * @Author: 麦奇
 * @Email： 1625017540@qq.com
 * @Create: 2019-04-06 16:46
 * @Describe：
 **/
public interface CallBack {
    public Object doExecute(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet);
}
