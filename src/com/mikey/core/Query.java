package com.mikey.core;

import com.mikey.bean.ColumnInfo;
import com.mikey.bean.TableInfo;
import com.mikey.util.JDBCUtil;
import com.mikey.util.RefUtil;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @Program: ORM
 * @Author: 麦奇
 * @Email： 1625017540@qq.com
 * @Create: 2019-04-06 15:30
 * @Describe：
 **/
public abstract class Query implements Cloneable{
    /**
     * 采用模板方法模式将JDBC操作封装成模板，便于重用
     * @param sql
     * @param params
     * @param clazz
     * @param callBack
     * @return
     */
    public Object executeQueryTemplate(String sql, Object[] params, Class clazz, CallBack callBack){
        Connection connection=DBManager.getConnection();
        PreparedStatement preparedStatement=null;

        ResultSet resultSet=null;

        try {
            preparedStatement=connection.prepareStatement(sql);
            JDBCUtil.handleParams(preparedStatement,params);
            System.out.println(preparedStatement);
            resultSet=preparedStatement.executeQuery();
            return resultSet;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }finally {
            DBManager.close(preparedStatement,connection);
        }
    }

    /**
     * 执行一个DML语句
     * @param sql
     * @param params
     * @return 执行sql语句后影响的行数
     */
    public int executeDML(String sql,Object[] params){
        Connection connection=DBManager.getConnection();
        int count=0;
        PreparedStatement preparedStatement=null;
        try {
            preparedStatement=connection.prepareStatement(sql);
            JDBCUtil.handleParams(preparedStatement,params);
            System.out.println(preparedStatement);
            count=preparedStatement.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBManager.close(preparedStatement,connection);
        }
        return count;
    }

    /**
     * 将一个对象存储到数据库中
     * @param obj
     */
    public void insert(Object obj){
        /**
         * 获取传入对象的Class
         */
        Class clazz=obj.getClass();
        /**
         * 存储sql的参数对象
         */
        ArrayList<Object> params = new ArrayList<>();
        /**
         * 表信息
         */
        TableInfo tableInfo=TableContext.poClassTableMap.get(clazz);
        /**
         * 构建sql
         */
        StringBuilder sql = new StringBuilder("insert into " + tableInfo.getTname() + " (");
        /**
         * 计算不为null的属性值
         */
        int countNotNullField=0;
        /**
         * 通过反射获取所有属性
         */
        Field[] fileds=clazz.getDeclaredFields();
        /**
         * 遍历构建SQL
         */
        for (Field field:fileds) {
            String fieldName=field.getName();
            Object fieldValue= RefUtil.invokeGet(fieldName,obj);

            if(fieldValue!=null){
                countNotNullField++;
                sql.append(fieldName+",");
                params.add(fieldValue);
            }
        }
        sql.setCharAt(sql.length()-1,')');
        sql.append(" values (");
        for (int i = 0; i < countNotNullField; i++) {
            sql.append("?,");
        }
        sql.setCharAt(sql.length()-1,')');
        executeDML(sql.toString(),params.toArray());
    }

    /**
     * 删除clazz表示类对应的表中的记录(指定主键值id的记录)
     * @param clazz
     * @param id
     */
    public void delete(Class clazz,Object id){
        //Emp.class,2-->delete from emp where id=2
        //通过Class对象找TableInfo
        TableInfo tableInfo=TableContext.poClassTableMap.get(clazz);
        //获取主键
        ColumnInfo onlyPriKey=tableInfo.getOnlyPrikey();
        //sql
        String sql = "delete from "+tableInfo.getTname()+ " where "+onlyPriKey.getName()+"=?";
        //execute
        executeDML(sql,new Object[]{id});
    }

    /**
     * 删除对象在数据库中对应的记录(对象所在的类对应到表，对象的主键的值对应到记录)
     * @param obj
     */
    public void delete(Object obj){
        Class clazz=obj.getClass();

        TableInfo tableInfo=TableContext.poClassTableMap.get(clazz);

        ColumnInfo onlyPrikey=tableInfo.getOnlyPrikey();

        Object prikeyValue=RefUtil.invokeGet(onlyPrikey.getName(),obj);

        delete(clazz,prikeyValue);
    }

    /**
     * 更新对象对应的记录
     * @param obj
     * @param fieldNames
     * @return
     */
    public int update(Object obj,String[] fieldNames){
        //obj{"uanme","pwd"}-->update 表名  set uname=?,pwd=? where id=?

        Class clazz=obj.getClass();

        List<Object> params=new ArrayList<>();

        TableInfo tableInfo=TableContext.poClassTableMap.get(clazz);

        ColumnInfo onlyPrikey = tableInfo.getOnlyPrikey();

        StringBuilder sql = new StringBuilder("update "+tableInfo.getTname()+ " set ");

        for (String fname: fieldNames) {
            Object fvalue=RefUtil.invokeGet(fname,obj);
            params.add(fvalue);
            sql.append(fname+"=?,");
        }
        sql.setCharAt(sql.length()-1,' ');
        sql.append(" where ");
        sql.append(onlyPrikey.getName()+"=? ");

        params.add(RefUtil.invokeGet(onlyPrikey.getName(),obj));
        return executeDML(sql.toString(),params.toArray());
    }

    /**
     * 查询返回多行记录，并将每行记录封装到clazz指定的类的对象中
     * @param sql
     * @param clazz
     * @param params
     * @return
     */
    public List queryRows(String sql,Class clazz,Object[] params){
        return (List)executeQueryTemplate(sql,params,clazz, new CallBack() {
            @Override
            public Object doExecute(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {

                List list=null;

                try {
                    ResultSetMetaData metaData= resultSet.getMetaData();
                    //多行
                    while (resultSet.next()){
                        if (list==null){
                            list=new ArrayList();
                        }
                        /**
                         * 调用无参构造方法
                         */
                        Object rowObj=clazz.newInstance();

                        for (int i = 0; i < metaData.getColumnCount(); i++) {
                            String columnName=metaData.getColumnLabel(i+1);
                            Object columnValue=resultSet.getObject(i+1);

                            RefUtil.invokeSet(rowObj,columnName,columnValue);
                        }
                        list.add(rowObj);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
                return list;
            }
        });
    }

    /**
     * 查询一行记录
     * 查询返回一行记录，并将该记录封装到clazz指定的类的对象中
     * @param sql
     * @param clazz
     * @param params
     * @return
     */
    public Object queryUniqueRow(String sql,Class clazz,Object[] params){

        List list=queryRows(sql,clazz,params);

        return (list!=null&&list.size()>0?list.get(0):null);
    }

    /**
     * 查询一个值
     * 根据主键的值直接查找对应的对象
     * @param sql
     * @param params
     * @return
     */
    public Object queryVlaue(String sql,Object[] params){
        return executeQueryTemplate(sql, params, null, new CallBack() {
            @Override
            public Object doExecute(Connection conn, PreparedStatement ps, ResultSet rs) {
                Object value = null;
                try {
                    while(rs.next()){
                        value = rs.getObject(1);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return value;
            }
        });
    }

    /**
     * 查询一个数值
     * 查询返回一个数字(一行一列)，并将该值返回
     * @param sql
     * @param clazz
     * @param params
     * @return
     */
    public Number queryNumber(String sql,Class clazz,Object[] params){
        return (Number)queryVlaue(sql,params);
    }
    /**
     * 分页查询
     * @param pageNum 第几页数据
     * @param size 每页显示多少记录
     * @return
     */
    public abstract Object queryPagenate(int pageNum,int size);

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
