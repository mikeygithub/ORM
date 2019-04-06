package com.mikey.core;

import com.sun.org.apache.regexp.internal.RE;

/**
 * @Program: ORM
 * @Author: 麦奇
 * @Email： 1625017540@qq.com
 * @Create: 2019-04-06 15:42
 * @Describe：
 **/
public class QueryFactory {

//    public QueryFactory() {
//    }

    //原型对象
    private static Query prototypeObj;

    static {

        try {

            Class clazz=Class.forName(DBManager.getConf().getQueryClass());

            prototypeObj=(Query)clazz.newInstance();

        }catch (Exception e){

            e.printStackTrace();

        }

        TableContext.loadPOTables();
    }

    /**
     * 私有化构造器
     */
    private QueryFactory(){

    }
    /**
     * createQuery
     * @return
     */
    private static Query createQuery(){
        try {
            return (Query)prototypeObj.clone();
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
