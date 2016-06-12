package cc.easyandroid.easydb;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.Locale;


/**
 * Created by cgpllx on 2016/6/12.
 */
public class TableUtils {
    static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS %1$s ( " + //
            SQLiteDelegate.ID + " PRIMARY KEY," +  //
            SQLiteDelegate.GSONSTRING + " NOT NULL," +//
            SQLiteDelegate.CREATEDTIME + " NOT NULL)";

    static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS %1$s";

    public static <T extends EasyDbObject> void createTable(SQLiteDatabase database, String tableName, Class<T> clazz) throws SQLException {
        String sql = String.format(Locale.CHINA, CREATE_TABLE_SQL, tableName);
        database.execSQL(sql);
        TableInfo.addTableInfo(tableName, clazz);
    }

    public static <T extends EasyDbObject> void dropTable(SQLiteDatabase database, String tableName, Class<T> clazz) throws SQLException {
        String sql = String.format(Locale.CHINA, DROP_TABLE_SQL, tableName);
        database.execSQL(sql);
        TableInfo.removeTableInfo(tableName, clazz);
    }

}
