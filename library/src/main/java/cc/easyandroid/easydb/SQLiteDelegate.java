/*
 * Copyright 2015 Antonio L贸pez Mar铆n <tonilopezmr.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.easyandroid.easydb;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * Class delegated charge of implementing CRUD methods for any object model.
 *
 * @author Antonio López Marín
 */
public class SQLiteDelegate<T extends EasyDbObject> implements DataAccesObject {
    public static final Gson GSON = new Gson();
    private static final String ID = "id";
    private static final String TIMESTAMP = "timestamp";
    private static final String GSONSTRING = "gson";
    protected SQLiteDatabase db;
    protected String tabName;
    protected Class<T> clazz;

    public SQLiteDelegate(SQLiteDatabase db, String tabName, Class<T> clazz) {
        this.db = db;
        this.tabName = tabName;
        this.clazz = clazz;
    }

    @Override
    public <T extends EasyDbObject> void insert(T dto) throws Exception {
        String table = null;
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, dto.buildKeyColumn());
        contentValues.put(TIMESTAMP, System.currentTimeMillis());
        contentValues.put(GSONSTRING, GSON.toJson(dto));
        long rowid = db.replace(table, null, contentValues);
        if (rowid == -1)
            throw new SQLiteException("Error inserting " + dto.getClass().toString());
    }

    @Override
    public T findById(String id) throws Exception {

        String[] columns = null;
        String selection = ID + "=?";
        String[] selectionArgs = {id};
        String groupBy = null;
        String having = null;
        String orderBy = "DESC";//从大到小排序，查出来的是最后一个
        Cursor cursor = db.query(tabName, columns, selection, selectionArgs, groupBy, having, orderBy);
        T easyDbObject = null;
        try {
            if (cursor.moveToFirst()) {
                String gson = cursor.getString(cursor.getColumnIndex("gson"));
                easyDbObject = GSON.fromJson(gson, clazz);
            }
        } catch (Exception e) {
            throw new SQLiteException("Error findAllFromTabName " + tabName.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return easyDbObject;
    }

    @Override
    public synchronized boolean delete(String id) throws Exception {
        String whereClause = ID + "=?";
        String[] whereArgs = {id};
        int confirm = db.delete(tabName, whereClause, whereArgs);
        return confirm != 0;
    }

    @Override
    public synchronized boolean deleteAll() throws Exception {
        int confirm = db.delete(tabName, null, null);
        return confirm != 0;
    }

    @Override
    public Cursor findAllCursor(String orderBy) {
        String[] columns = null;
        String selection = null;
        String[] selectionArgs = null;
        String groupBy = null;
        String having = null;
        Cursor cursor = db.query(tabName, columns, selection, selectionArgs, groupBy, having, orderBy);
        return cursor;
    }

    /**
     * 根据条件查询
     *
     * @param orderBy eg "_id" DESC 表示按倒序排序(即:从大到小排序) 用 ACS 表示按正序排序(即:从小到大排序)
     * @return
     */
    @Override
    public ArrayList<T> findAllFromTabName(String orderBy) throws Exception {
        Cursor cursor = findAllCursor(orderBy);
        ArrayList<T> list = new ArrayList<>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    String gson = cursor.getString(cursor.getColumnIndex("gson"));
                    T t = GSON.fromJson(gson, clazz);
                    list.add(t);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            throw new SQLiteException("Error findAllFromTabName " + tabName.toString());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }
}