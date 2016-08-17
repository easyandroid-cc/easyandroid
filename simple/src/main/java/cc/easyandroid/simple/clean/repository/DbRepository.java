package cc.easyandroid.simple.clean.repository;

import android.content.Context;

import java.util.ArrayList;

import cc.easyandroid.easydb.abs.DataAccesObject;
import cc.easyandroid.easydb.core.EasyDbObject;
import cc.easyandroid.simple.clean.repository.abs.DbDataSource;
import cc.easyandroid.simple.core.SimpleSqlite;

/**
 * 数据库的仓库
 */
public class DbRepository implements DbDataSource {
    public final SimpleSqlite simpleSqlite;

    public DbRepository(Context context) {
        simpleSqlite = new SimpleSqlite(context);
    }


    public <T extends EasyDbObject> void getAll(String tabName, DbDataSource.LoadDatasCallback<T> callback) {
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

    public <T extends EasyDbObject> boolean insert(String tabName, T dto) {
        DataAccesObject<T> dataAccesObject = simpleSqlite.getDao(tabName);
        try {
            dataAccesObject.insert(dto);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public <T extends EasyDbObject> boolean insertAll(String tabName, ArrayList<T> arrayList) {
        DataAccesObject<T> dataAccesObject = simpleSqlite.getDao(tabName);
        try {
            dataAccesObject.insertAll(arrayList);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
