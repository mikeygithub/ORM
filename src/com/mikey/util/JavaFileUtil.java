package com.mikey.util;

import com.mikey.bean.ColumnInfo;
import com.mikey.bean.JavaFieldGetSet;
import com.mikey.bean.TableInfo;
import com.mikey.core.DBManager;
import com.mikey.core.MySqlTypeConvertor;
import com.mikey.core.TableContext;
import com.mikey.core.TypeConvertor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Program: ORM
 * @Author: 麦奇
 * @Email： 1625017540@qq.com
 * @Create: 2019-04-06 15:52
 * @Describe：封装了生成Java文件(源代码)常用的操作
 **/
public class JavaFileUtil {
    /**
     * 根据字段信息生成java属性信息。如：varchar username-->private String username;以及相应的set和get方法源码
     * @param columnInfo
     * @param typeConvertor
     * @return
     */
    public static JavaFieldGetSet createFieldGetSetSRC(ColumnInfo columnInfo, TypeConvertor typeConvertor){

        JavaFieldGetSet jfgs = new JavaFieldGetSet();

        String javaFieldType = typeConvertor.databaseType2JavaType(columnInfo.getDataType());

        jfgs.setFieldInfo("\tprivate "+javaFieldType +" "+columnInfo.getName()+";\n");

        //public String getUsername(){return username;}
        //生成get方法的源代码
        StringBuilder stringBuilde=new StringBuilder();
        stringBuilde.append("\tpublic "+javaFieldType+" get"+StringUtil.firstChar2UpperCase(columnInfo.getName())+"(){\n");
        stringBuilde.append("\t\treturn "+columnInfo.getName()+";\n");
        stringBuilde.append("\t}\n");
        jfgs.setGetInfo(stringBuilde.toString());

        //public void setUsername(String username){this.username=username;}
        //生成set方法的源代码
        StringBuilder setSrc = new StringBuilder();
        setSrc.append("\tpublic void set"+StringUtil.firstChar2UpperCase(columnInfo.getName())+"(");
        setSrc.append(javaFieldType+" "+columnInfo.getName()+"){\n");
        setSrc.append("\t\tthis."+columnInfo.getName()+"="+columnInfo.getName()+";\n");
        setSrc.append("\t}\n");
        jfgs.setSetInfo(setSrc.toString());
        return jfgs;
    }


    public static String createJavaSrc(TableInfo tableInfo,TypeConvertor convertor){
        Map<String,ColumnInfo> columns = tableInfo.getColumnInfoMap();
        List<JavaFieldGetSet> javaFields = new ArrayList<JavaFieldGetSet>();

        for (ColumnInfo columnInfo:columns.values()){
            javaFields.add(createFieldGetSetSRC(columnInfo,convertor));
        }

        StringBuilder stringBuilder = new StringBuilder();

        //生成package
        stringBuilder.append("package "+ DBManager.getConf().getPoPackage()+";\n\n");
        //生成import
        stringBuilder.append("import java.sql.*;\n");
        stringBuilder.append("import java.util.*;\n");
        //生成类声明语句
        stringBuilder.append("public class "+StringUtil.firstChar2UpperCase(tableInfo.getTname())+" {\n\n");

        //生成属性列表
        for (JavaFieldGetSet javaFieldGetSet:javaFields){
            stringBuilder.append(javaFieldGetSet.getFieldInfo());
        }
        stringBuilder.append("\n\n");
        //生成get方法
        for (JavaFieldGetSet javaFieldGetSet:javaFields){
            stringBuilder.append(javaFieldGetSet.getGetInfo());
        }
        //生成set方法
        for (JavaFieldGetSet javaFieldGetSet:javaFields){
            stringBuilder.append(javaFieldGetSet.getSetInfo());
        }

        stringBuilder.append("}\n");
        return stringBuilder.toString();


    }

    public static void createJavaPOFile(TableInfo tableInfo,TypeConvertor convertor){
        String src = createJavaSrc(tableInfo,convertor);

        String srcPath = DBManager.getConf().getSrcPath()+"\\";

        String packagePath = DBManager.getConf().getPoPackage().replaceAll("\\.","/");

        File file=new File(srcPath+packagePath);

        if (!file.exists()){
            file.mkdirs();
        }

        BufferedWriter bufferedWriter = null;

        try {
            bufferedWriter=new BufferedWriter(new FileWriter(file.getAbsoluteFile()+"/"+StringUtil.firstChar2UpperCase(tableInfo.getTname())+".java"));
            bufferedWriter.write(src);
            System.out.println("建立表："+tableInfo.getTname()+"对应的java类："+StringUtil.firstChar2UpperCase(tableInfo.getTname()+".java"));

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (bufferedWriter!=null){
                    bufferedWriter.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }

    public static void main(String[] args){

        Map<String,TableInfo> map = TableContext.tables;

        for (TableInfo tableInfo:map.values()){
            createJavaPOFile(tableInfo,new MySqlTypeConvertor());
        }

    }
}
