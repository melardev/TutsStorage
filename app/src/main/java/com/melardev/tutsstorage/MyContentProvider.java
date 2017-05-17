package com.melardev.tutsstorage;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

public class MyContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.melardev.tutsstorage.MyContentProvider";
    private static final String BASE_PATH = "usernames";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    private static final int USERNAMES = 10;
    private static final int USERNAME_ID = 20;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, USERNAMES);            // content://com.melardev.tutsstorage.MyContentProvider
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", USERNAME_ID);   // content://com.melardev.tutsstorage.MyContentProvider/2
    }

    private DBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = DBHelper.getInstance(getContext());
        return true; //successfully loaded
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        //Implement this to handle query requests from clients.
        SQLiteQueryBuilder sqb = new SQLiteQueryBuilder();
        sqb.setTables(DBHelper.TB_NAME);
        int matchCode = uriMatcher.match(uri);

        switch (matchCode) {
            case USERNAMES:
                break;
            case USERNAME_ID:
                sqb.appendWhere(DBHelper.COL_ID + "=" + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = sqb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);// make sure that potential listeners are getting notified
        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int matchCode = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsAffected = 0;
        switch (matchCode) {
            case USERNAMES:
                //Delete multiple usernames
                rowsAffected = db.delete(DBHelper.TB_NAME, selection, selectionArgs);
                break;
            case USERNAME_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    rowsAffected = db.delete(DBHelper.TB_NAME, DBHelper.COL_ID + "=" + id, null);
                else
                    rowsAffected = db.delete(DBHelper.TB_NAME, DBHelper.COL_ID + "=" + id + " and " + selection, selectionArgs);

                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsAffected;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // Implement this to handle requests to insert a new row.
        int matchCode = uriMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
        long id = 0;
        switch (matchCode) {
            case USERNAMES:
                id = sqlDB.insert(DBHelper.TB_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        //Implement this to handle requests to update one or more rows.
        int matchCode = uriMatcher.match(uri);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int affectedRows;
        switch (matchCode) {
            case USERNAMES:
                //update everything
                affectedRows = db.update(DBHelper.TB_NAME, values, selection, selectionArgs);
                break;
            case USERNAME_ID:
                //Update single user
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection))
                    affectedRows = db.update(DBHelper.TB_NAME, values, DBHelper.COL_ID + "=" + id, null);
                else
                    affectedRows = db.update(DBHelper.TB_NAME, values, DBHelper.COL_ID + "=" + id + " AND " + selection, null);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return affectedRows;
    }
}
