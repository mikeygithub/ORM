package com.mikey.pool;

import com.mikey.core.DBManager;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * @Program: ORM
 * @Author: 麦奇
 * @Email： 1625017540@qq.com
 * @Create: 2019-04-06 19:39
 * @Describe：连接池
 **/
public class DBConnPool {
    /**
     * 连接池对象
     */
    private List<Connection> pool;
    /**
     * 最大连接数
     */
    public static final int POOL_MAX_SIZE= DBManager.getConf().getPoolMaxSize();
    /**
     * 最小连接数
     */
    public static final int POOL_MIN_SIZE=DBManager.getConf().getPoolMinSize();
    /**
     * 初始化连接池，使池中的连接数达到最小值
     */
    public void initPool(){
        if (pool==null){
            pool=new ArrayList<Connection>();
        }
        while (pool.size() < DBConnPool.POOL_MIN_SIZE){
            pool.add(DBManager.createConnection());
            System.out.println("初始化数据库连接池："+pool.size());
        }

    }
    /**
     * 从连接池中取出一个连接
     * @return
     */
    public synchronized Connection getConnection(){
        int last_index=pool.size()-1;
        Connection connection=pool.get(last_index);
        pool.remove(last_index);
        return connection;
    }

    /**
     * 将连接放回池中
     * @param connection
     */
    public synchronized void close(Connection connection){
        if (pool.size()>=POOL_MAX_SIZE){
            try {
                if (connection!=null){
                    connection.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            pool.add(connection);
        }
    }

    /**
     * 构造器初始化
     */
    public DBConnPool(){
        initPool();
    }

}
