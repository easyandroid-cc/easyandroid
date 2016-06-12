package cc.easyandroid.easydb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cgpllx on 2016/6/12.
 */
public class EasySqliteHelper extends SQLiteOpenHelper {
    private volatile boolean isOpen = false;

    public EasySqliteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * TableUtils.createTable(db, "", Tab.class);
     *
     * @param db SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            TableUtils.createTable(db, "", Tab.class);
            TableUtils.createTable(db, "", Tab.class);
            TableUtils.createTable(db, "", Tab.class);
            TableUtils.createTable(db, "", Tab.class);
            TableUtils.createTable(db, "", Tab.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * TableUtils.dropTable(db, "", Tab.class);
     *
     * @param db         SQLiteDatabase
     * @param oldVersion oldVersion
     * @param newVersion newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(db, "", Tab.class);
            TableUtils.dropTable(db, "", Tab.class);
            TableUtils.dropTable(db, "", Tab.class);
            TableUtils.dropTable(db, "", Tab.class);
            onCreate(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void close() {
        super.close();
        isOpen = false;
    }

    SQLiteDatabase mSQLiteDatabase;

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        isOpen = true;
    }

    public <T extends EasyDbObject> SQLiteDelegate<T> getSQLiteDelegate(String tableName) {
        TableInfo tableInfo = TableInfo.getTableInfo(tableName);
        SQLiteDelegate<T> sqLiteDelegate = new SQLiteDelegate(getSQLiteDatabase(), tableInfo.getTableName(), tableInfo.getTableMappingClass());
        return sqLiteDelegate;
    }

    private SQLiteDatabase getSQLiteDatabase() {
        if (mSQLiteDatabase == null) {
            mSQLiteDatabase = getWritableDatabase();
        }
        return mSQLiteDatabase;
    }
}
