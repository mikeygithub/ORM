package com.mikey.util;

import java.lang.reflect.Method;

/**
 * @Program: ORM
 * @Author: 麦奇
 * @Email： 1625017540@qq.com
 * @Create: 2019-04-06 15:51
 * @Describe：反射工具类
 **/
public class RefUtil {
    /**
     * 调用obj对象对应属性fieldName的get方法
     * @param fieldName
     * @param obj
     * @return
     */
    public static Object invokeGet(String fieldName,Object obj){
        try {
            Class clazz=obj.getClass();
            Method method=clazz.getMethod("get"+StringUtil.firstChar2UpperCase(fieldName),null);
            return method.invoke(obj,null);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 调用obj对象对应属性fieldName的set方法
     * @param obj
     * @param columnName
     * @param columnValue
     */
    public static void invokeSet(Object obj,String columnName,Object columnValue) {
        try {
            if (columnValue!=null){
                Method method=obj.getClass().getDeclaredMethod("set"+StringUtil.firstChar2UpperCase(columnName),null);
                method.invoke(obj,columnValue);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
