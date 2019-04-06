package com.mikey.bean;

/**
 * @Program: ORM
 * @Author: 麦奇
 * @Email： 1625017540@qq.com
 * @Create: 2019-04-06 16:40
 * @Describe：
 **/
public class JavaFieldGetSet {
    /**
     *属性的源码信息。如：private int userId;
     */
    private String fieldInfo;
    /**
     * get方法的源码信息。如：public int getUserId(){}
     */
    private String getInfo;
    /**
     * set方法的信息
     */
    private String setInfo;

    @Override
    public String toString() {
        return "JavaFieldGetSet{" +
                "fieldInfo='" + fieldInfo + '\'' +
                ", getInfo='" + getInfo + '\'' +
                ", setInfo='" + setInfo + '\'' +
                '}';
    }

    public JavaFieldGetSet() {
    }

    public JavaFieldGetSet(String fieldInfo, String getInfo, String setInfo) {
        this.fieldInfo = fieldInfo;
        this.getInfo = getInfo;
        this.setInfo = setInfo;
    }

    public String getFieldInfo() {
        return fieldInfo;
    }

    public void setFieldInfo(String fieldInfo) {
        this.fieldInfo = fieldInfo;
    }

    public String getGetInfo() {
        return getInfo;
    }

    public void setGetInfo(String getInfo) {
        this.getInfo = getInfo;
    }

    public String getSetInfo() {
        return setInfo;
    }

    public void setSetInfo(String setInfo) {
        this.setInfo = setInfo;
    }
}
