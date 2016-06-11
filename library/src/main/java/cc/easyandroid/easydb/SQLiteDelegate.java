/*
 * Copyright 2015 Antonio López Marín <tonilopezmr.com>
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
 */
public class SQLiteDelegate<T extends EasyDbObject> implements DataAccesObject<T> {
    public static final Gson GSON = new Gson();
    public static final String ID = "id";
    public static final String TIMESTAMP = "timestamp";
    public static final String GSONSTRING = "gson";
    protected final SQLiteDatabase db;
    protected final String tabName;
    protected final Class<T> clazz;

    public SQLiteDelegate(SQLiteDatabase db, String tabName, Class<T> clazz) {
        this.db = db;
        this.tabName = tabName;
        this.clazz = clazz;
    }

    @Override
    public void insert(T dto) throws Exception {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID, dto.buildKeyColumn());
        contentValues.put(TIMESTAMP, System.currentTimeMillis());
        contentValues.put(GSONSTRING, GSON.toJson(dto));
        long rowid = db.replace(tabName, null, contentValues);
        if (rowid == -1)
            throw new SQLiteException("Error inserting " + dto.getClass().toString());
    }

    @Override
    public T findById(String id) throws Exception {

        String selection = ID + "=?";
        String[] selectionArgs = {id};
        String orderBy = TIMESTAMP+" "+"DESC";//
        Cursor cursor = db.query(tabName, null, selection, selectionArgs, null, null, orderBy);
        T easyDbObject = null;
        try {
            if (cursor.moveToFirst()) {
                String gson = cursor.getString(cursor.getColumnIndex("gson"));
                easyDbObject = GSON.fromJson(gson, clazz);
            }
        } catch (Exception e) {
            throw new SQLiteException("Error findAllFromTabName " + tabName);
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

    /**
     * @param order eg "_id" DESC  时间正序还是倒序
     * @return Cursor
     */
    @Override
    public Cursor findAllCursor(String order) {
        String orderBy = TIMESTAMP + " " + order;
        return db.query(tabName, null, null, null, null, null, orderBy);
    }

    /**
     * @param order eg "_id" DESC  时间正序还是倒序
     * @return ArrayList
     */
    @Override
    public ArrayList<T> findAllFromTabName(String order) throws Exception {
        Cursor cursor = findAllCursor(order);
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
            throw new SQLiteException("Error findAllFromTabName " + tabName);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

}