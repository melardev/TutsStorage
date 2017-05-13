package com.melardev.tutsstorage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.melardev.tutsstorage.model.User;


/**
 * Created by melardev on 2/12/2017.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String COL_ID = "_id";
    private static final String COL_NAME = "file_name";

    private static final String COL_TIME_ADDED = "time_added";
    static final String TB_NAME = "recordings";
    static final String DB_NAME = "db_names";
    private static final int DB_VERSION = 1;
    private static OnDatabaseChangedListener mOnDatabaseChangedListener;
    private static DBHelper mInstance;

    public void getAllUsers() {

    }

    public User getUserWithId(String id) {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COL_ID, COL_NAME, COL_TIME_ADDED};
        Cursor c = db.query(TB_NAME, projection, COL_ID + "= ?", new String[]{id}, null, null, null);
        if (c.moveToFirst()) {
            User item = new User();
            item.setId(c.getInt(c.getColumnIndex(COL_ID)));
            item.setName(c.getString(c.getColumnIndex(COL_NAME)));
            item.setTime(c.getInt(c.getColumnIndex(COL_TIME_ADDED)));
            c.close();
            return item;
        }
        return null;
    }

    public void renameItem(String id, String newName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, newName);
        int rowsAffected = db.update(TB_NAME, cv, COL_ID + " = " + id, null);

        if (rowsAffected == 1) {
            Log.d("DBHElper", "updated successfuly");
            if (mOnDatabaseChangedListener != null) {
                mOnDatabaseChangedListener.onDatabaseEntryRenamed();
            }
        }
    }

    public void deleteUser(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int rowsAffected = db.delete(TB_NAME, COL_ID + " = ?", new String[]{String.valueOf(id)});
        if (rowsAffected == 1 && mOnDatabaseChangedListener != null) {
            mOnDatabaseChangedListener.onDatabaseEntryRenamed();
        }

    }

    public void deleteDatabase() {

    }

    public interface OnDatabaseChangedListener {
        void onNewDatabaseEntryAdded();

        void onDatabaseEntryRenamed();
    }

    private DBHelper(Context mContext) {
        super(mContext, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TB_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY,"
                + COL_NAME + " TEXT,"
                + COL_TIME_ADDED + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public long addUser(String userName) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NAME, userName);
        cv.put(COL_TIME_ADDED, System.currentTimeMillis());
        long rowId = db.insert(TB_NAME, null, cv);
        if (mOnDatabaseChangedListener != null)
            mOnDatabaseChangedListener.onNewDatabaseEntryAdded();

        return rowId;
    }

    public void setOnDatabaseChangedListener(OnDatabaseChangedListener listener) {
        mOnDatabaseChangedListener = listener;
    }

    public User getItemAt(int position) {

        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COL_ID, COL_NAME, COL_TIME_ADDED};
        Cursor c = db.query(TB_NAME, projection, null, null, null, null, null);
        if (c.moveToPosition(position)) {
            User item = new User();
            item.setId(c.getInt(c.getColumnIndex(COL_ID)));
            item.setName(c.getString(c.getColumnIndex(COL_NAME)));
            item.setTime(c.getInt(c.getColumnIndex(COL_TIME_ADDED)));
            c.close();
            return item;
        }
        return null;
    }

    public int getCount() {
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COL_ID};
        Cursor c = db.query(TB_NAME, projection, null, null, null, null, null);
        int count = c.getCount();
        c.close();
        return count;
    }

    public void removeItemWithId(int id) {
        SQLiteDatabase db = getWritableDatabase();
        String[] whereArgs = {COL_ID};
        db.delete(TB_NAME, COL_ID + "=?", whereArgs);
        //db.delete(TB_NAME, COL_ID + "=" + id,null);
    }


    public static DBHelper getInstance(Context applicationContext) {
        if (mInstance == null)
            mInstance = new DBHelper(applicationContext);
        return mInstance;
    }
}
