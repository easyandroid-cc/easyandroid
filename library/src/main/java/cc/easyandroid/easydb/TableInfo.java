package cc.easyandroid.easydb;


import android.support.v4.util.ArrayMap;

/**
 * Created by Administrator on 2016/6/12.
 */
public class TableInfo {
    private Class<?> tableMappingClass;// 对象名称，
    private String tableName;// 表名，
    /**
     * key是数据库表名
     */
    private static final ArrayMap<String, TableInfo> tableInfoMap = new ArrayMap<>();// 所有表放在这里 key是对象的name

    public static TableInfo addTableInfo(String tabName, Class<?> clazz) {
        if (clazz == null)
            throw new IllegalArgumentException("table info add error,because the clazz is null");
        if (tabName == null) {
            throw new IllegalArgumentException("table info add error,because the tabName is null");
        }
        TableInfo tableInfo = tableInfoMap.get(tabName);
        if (tableInfo == null) {// 没有就创建表
            tableInfo = new TableInfo();
            tableInfo.setTableName(tabName);
            tableInfo.setTableMappingClass(clazz);
            tableInfoMap.put(tabName, tableInfo);
        }
        return tableInfo;
    }

    public static TableInfo getTableInfo(String tableName) {
        if (tableName == null) {
            throw new IllegalArgumentException("table info get error,because the tablename is null");
        }
        TableInfo tableInfo = tableInfoMap.get(tableName);
        if (tableInfo == null) {// 没有就创建表
            throw new IllegalArgumentException("tabName is not EXISTS");
        }
        return tableInfo;
    }

    public static TableInfo removeTableInfo(String tabName, Class<?> clazz) {
        if (tabName == null) {
            throw new IllegalArgumentException("table info remove error,because the tablename is null");
        }
        TableInfo tableInfo = tableInfoMap.get(tabName);
        if (tableInfo == null) {// 没有就创建表
            throw new IllegalArgumentException("tabName is not EXISTS");
        }
        return tableInfo;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public Class<?> getTableMappingClass() {
        return tableMappingClass;
    }

    public void setTableMappingClass(Class<?> tableMappingClass) {
        this.tableMappingClass = tableMappingClass;
    }
}
