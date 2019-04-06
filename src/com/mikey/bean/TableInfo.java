package com.mikey.bean;

import java.util.List;
import java.util.Map;

/**
 * @Program: ORM
 * @Author: 麦奇
 * @Email： 1625017540@qq.com
 * @Create: 2019-04-06 15:56
 * @Describe：表信息
 **/
public class TableInfo {
    //表名
    private String tname;
    //表的所有字段
    private Map<String,ColumnInfo> columnInfoMap;
    //唯一主健
    private ColumnInfo onlyPrikey;
    //联合主键
    private List<ColumnInfo> priKeys;

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public Map<String, ColumnInfo> getColumnInfoMap() {
        return columnInfoMap;
    }

    public void setColumnInfoMap(Map<String, ColumnInfo> columnInfoMap) {
        this.columnInfoMap = columnInfoMap;
    }

    public ColumnInfo getOnlyPrikey() {
        return onlyPrikey;
    }

    public void setOnlyPrikey(ColumnInfo onlyPrikey) {
        this.onlyPrikey = onlyPrikey;
    }

    public List<ColumnInfo> getPriKeys() {
        return priKeys;
    }

    public void setPriKeys(List<ColumnInfo> priKeys) {
        this.priKeys = priKeys;
    }

    public TableInfo() {
    }

    public TableInfo(String tname, Map<String, ColumnInfo> columnInfoMap, ColumnInfo onlyPrikey) {
        this.tname = tname;
        this.columnInfoMap = columnInfoMap;
        this.onlyPrikey = onlyPrikey;
    }

    public TableInfo(String tname, Map<String, ColumnInfo> columnInfoMap, List<ColumnInfo> priKeys) {
        this.tname = tname;
        this.columnInfoMap = columnInfoMap;
        this.priKeys = priKeys;
    }
}
