package cc.easyandroid.simple.clean.repository;

import android.content.Context;

import java.util.ArrayList;

import cc.easyandroid.easydb.abs.DataAccesObject;
import cc.easyandroid.easydb.core.EasyDbObject;
import cc.easyandroid.simple.core.SimpleSqlite;

/**
 * 数据库的仓库
 */
public class DbRepository implements DbDataSource {

    public DbRepository(Context context) {
        simpleSqlite = new SimpleSqlite(context);
    }

    @Override
    public <T> T getObject(Class<T> clazz) {
        return null;
    }

    public <T extends EasyDbObject> void getAll(String tabName, DbDataSource.LoadDatasCallback callback) {
        DataAccesObject<T> dataAccesObject = simpleSqlite.getDao(tabName);
        try {
            callback.ondDatasLoaded(dataAccesObject.findAllFromTabName("DESC"));
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        callback.onDataNotAvailable();
    }

    public <T extends EasyDbObject> boolean deleteAll(String tabName) {
        DataAccesObject<T> dataAccesObject = simpleSqlite.getDao(tabName);
        try {
            return dataAccesObject.deleteAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public <T extends EasyDbObject> boolean deleteById(String tabName, String id) {
        DataAccesObject<T> dataAccesObject = simpleSqlite.getDao(tabName);
        try {
            return dataAccesObject.delete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public <T extends EasyDbObject> void insert(String tabName, T dto) {
        DataAccesObject<T> dataAccesObject = simpleSqlite.getDao(tabName);
        try {
            dataAccesObject.insert(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T extends EasyDbObject> void insertAll(String tabName, ArrayList<T> arrayList) {
        DataAccesObject<T> dataAccesObject = simpleSqlite.getDao(tabName);
        try {
            dataAccesObject.insertAll(arrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final SimpleSqlite simpleSqlite;


}
