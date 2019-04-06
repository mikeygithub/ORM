package com.mikey.core;

import com.mikey.bean.Configuration;
import com.mikey.pool.DBConnPool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * @Program: ORM
 * @Author: 麦奇
 * @Email： 1625017540@qq.com
 * @Create: 2019-04-06 15:50
 * @Describe： 根据配置信息
 **/
public class DBManager {

    /**
     * 配置信息类
     */
    private static Configuration conf;

    /**
     * 连接对象
     */
    private static DBConnPool pool;

    static {
        Properties pros=new Properties();
        try {
            pros.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties"));
        }catch (Exception e){
            e.printStackTrace();
        }

        conf=new Configuration();

        conf.setDriver(pros.getProperty("driver"));
        conf.setPoPackage(pros.getProperty("poPackage"));
        conf.setPwd(pros.getProperty("pwd"));
        conf.setSrcPath(pros.getProperty("srcPath"));
        conf.setUrl(pros.getProperty("url"));
        conf.setUser(pros.getProperty("user"));
        conf.setUsingDB(pros.getProperty("usingDB"));
        conf.setQueryClass(pros.getProperty("queryClass"));
        conf.setPoolMaxSize(Integer.parseInt(pros.getProperty("poolMaxSize")));
        conf.setPoolMinSize(Integer.parseInt(pros.getProperty("poolMinSize")));
    }

    /**
     * 获取连接对象
     * @return
     */
    public static Connection getConnection(){
        if (pool==null){
            pool=new DBConnPool();
        }
        return pool.getConnection();
    }

    /**
     * 创建连接
     * @return
     */
    public static Connection createConnection(){
        try {
            Class.forName(conf.getDriver());
            return DriverManager.getConnection(conf.getUrl(),conf.getUser(),conf.getPwd());
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 关闭传入的相关资源对象
     * @param resultSet
     * @param statement
     * @param connection
     */
    public static void close(ResultSet resultSet, Statement statement,Connection connection){
        try {
            if (resultSet!=null){
                resultSet.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            if (statement!=null){
                statement.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        pool.close(connection);
    }

    /**
     * 关闭Statement返回连接对象到连接池
     * @param statement
     * @param connection
     */
    public static void close(Statement statement,Connection connection){
        try {
            if (statement!=null){
                statement.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        pool.close(connection);
    }

    /**
     * 返回连接对象到连接池
     * @param connection
     */
    public static void close(Connection connection){
        pool.close(connection);
    }

    /**
     * 返回Configuration对象
     * @return
     */
    public static Configuration getConf(){
        return conf;
    }


}
