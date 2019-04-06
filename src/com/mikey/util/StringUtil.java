package com.mikey.util;

/**
 * @Program: ORM
 * @Author: 麦奇
 * @Email： 1625017540@qq.com
 * @Create: 2019-04-06 15:51
 * @Describe：封装了字符串常用的操作
 **/
public class StringUtil {
    /**
     * 将目标字符串首字母变为大写
     * @param str
     * @return
     */
    public static String firstChar2UpperCase(String str){
        //abcd--->Abcd
        //abcd--->ABCD--->Abcd
        return str.toUpperCase().substring(0,1)+str.substring(1);
    }
}
