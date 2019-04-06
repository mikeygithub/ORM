package com.mikey.core;

import com.mikey.bean.ColumnInfo;
import com.mikey.bean.TableInfo;
import com.mikey.util.JavaFileUtil;
import com.mikey.util.StringUtil;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @Program: ORM
 * @Author: 麦奇
 * @Email： 1625017540@qq.com
 * @Create: 2019-04-06 15:49
 * @Describe：
 * 负责获取管理数据库所有表结构和类结构的关系，
 * 并可以根据表结构生成类结构。
 **/
public class TableContext {
    /**
     * 表名Key   表信息对象为value
     */
    public static Map<String, TableInfo> tables=new HashMap<String, TableInfo>();
    /**
     * 将po的class的对象和表信息对象关联起来便于重用！
     */
    public static Map<Class,TableInfo> poClassTableMap=new HashMap<Class, TableInfo>();

    private TableContext(){}

    static {
        try {
            Connection connection=DBManager.getConnection();
            DatabaseMetaData metaData=connection.getMetaData();

            ResultSet tableRet=metaData.getTables(null,"%","%",new String[]{"TABLE"});

            while (tableRet.next()){
                String tableName=(String)tableRet.getObject("TABLE_NAME");
                TableInfo tableInfo=new TableInfo(tableName,new HashMap<String,ColumnInfo>(),new ArrayList<ColumnInfo>());
                tables.put(tableName,tableInfo);

                ResultSet set=metaData.getColumns(null,"%",tableName,"%");
                while(set.next()){
                    ColumnInfo ci = new ColumnInfo(set.getString("COLUMN_NAME"),
                            set.getString("TYPE_NAME"), 0);
                    tableInfo.getColumnInfoMap().put(set.getString("COLUMN_NAME"), ci);
                }

                ResultSet set2=metaData.getPrimaryKeys(null,"%",tableName);
                while (set2.next()){
                    ColumnInfo ci2=(ColumnInfo)tableInfo.getColumnInfoMap().get(set2.getObject("COLUMN_NAME"));
                    ci2.setKeyType(0);
                    tableInfo.getPriKeys().add(ci2);
                }

                if (tableInfo.getPriKeys().size()>0){
                    tableInfo.setOnlyPrikey(tableInfo.getPriKeys().get(0));
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 根据表结构，更新配置的po包下面的java类
     * 实现了从表结构转化到类结构
     */
    public static void updateJavaPOFile(){
        Map<String,TableInfo> map=TableContext.tables;
        for (TableInfo t:map.values()) {
            JavaFileUtil.createJavaPOFile(t,new MySqlTypeConvertor());
        }
    }

    public static void loadPOTables() {
        for (TableInfo tableInfo : tables.values()) {
            try {
                Class clazz = Class.forName(DBManager.getConf().getPoPackage()
                        + "." + StringUtil.firstChar2UpperCase(tableInfo.getTname()));
                poClassTableMap.put(clazz, tableInfo);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

        public static void main(String[] args){

            Map<String, TableInfo> tables = TableContext.tables;

            System.out.println(tables);

        }
    }
