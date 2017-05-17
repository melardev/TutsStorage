package com.melardev.tutsstorage;

import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.melardev.tutsstorage.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityContentProvider extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private RecyclerView recyclerview;
    private RecCursorAdapter recAdapter;
    private EditText etxtName;
    private EditText etxtNameExisting;
    private EditText etxtId;

    //https://developer.android.com/guide/components/fundamentals.html#Components
    //content://authority/optionalPath/optionalId

    /*
     A content provider is meant to share data with other applications, that data may be stored in files, SQLite database, web
     or any other persistent storage location that your app can access.
     Content providers can be accessesd through URIs beginning with content:// , a Content provider may not only give you access to the data(Read)
     but also perform write (create, update and delete) operations
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_provider);

        recyclerview = (RecyclerView) findViewById(R.id.recUsers);
        etxtName = (EditText) findViewById(R.id.etxtName);
        etxtNameExisting = (EditText) findViewById(R.id.etxtNameExisting);
        etxtId = (EditText) findViewById(R.id.etxtId);

        loadData();
    }

    public void loadData() {
        getSupportLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(this, MyContentProvider.CONTENT_URI, new String[]{DBHelper.COL_ID, DBHelper.COL_NAME, DBHelper.COL_TIME_ADDED}, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        recAdapter = new RecCursorAdapter(data);
        recyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerview.setAdapter(recAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        recyclerview.setAdapter(null);
    }

    public void addUser(View view) {
        String userName = etxtName.getText().toString();
        if (!TextUtils.isEmpty(userName)) {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.COL_NAME, userName);
            cv.put(DBHelper.COL_TIME_ADDED, new Date().getTime());
            getContentResolver().insert(MyContentProvider.CONTENT_URI, cv);
        }
    }

    public void editUser(View view) {
        String id = etxtId.getText().toString();
        String name = etxtNameExisting.getText().toString();

        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(name)) {
            ContentValues cv = new ContentValues();
            cv.put(DBHelper.COL_NAME, name);
            cv.put(DBHelper.COL_TIME_ADDED, System.currentTimeMillis());
            Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
            getContentResolver().update(uri, cv, null, null);
        }
    }

    public class RecCursorAdapter extends RecyclerView.Adapter<RecCursorAdapter.RecCursorAdapterViewHolder> {

        private Cursor cursor;

        public RecCursorAdapter(Cursor cursor) {
            this.cursor = cursor;
            //getContentResolver().registerContentObserver(MyContentProvider.CONTENT_URI, true, new MyContentObserver(new Handler(Looper.getMainLooper())));
            cursor.registerDataSetObserver(new MyDataSetObserver());
        }

        @Override
        public RecCursorAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecCursorAdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent, false));
        }

        @Override
        public void onBindViewHolder(RecCursorAdapterViewHolder holder, int position) {
            final User user = getUserFromCursor(position);
            holder.txtUserName.setText(user.getName());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String date = sdf.format(new Date(user.getTime()));
            holder.txtTime.setText(date);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    etxtNameExisting.setText(user.getName());
                    etxtNameExisting.requestFocus();
                    etxtId.setText(String.valueOf(user.getId()));
                }
            });
            holder.imgDeleteRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + user.getId());
                    getContentResolver().delete(uri, null, null);
                    loadData();
                }
            });
        }

        private User getUserFromCursor(int position) {
            cursor.moveToPosition(position);
            User user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex(DBHelper.COL_ID)));
            user.setName(cursor.getString(cursor.getColumnIndex(DBHelper.COL_NAME)));
            user.setTime(cursor.getLong(cursor.getColumnIndex(DBHelper.COL_TIME_ADDED)));
            return user;
        }

        @Override
        public int getItemCount() {
            if (cursor != null)
                return cursor.getCount();
            return 0;
        }

        public class RecCursorAdapterViewHolder extends RecyclerView.ViewHolder {
            public final TextView txtUserName;
            public final TextView txtTime;
            private final ImageView imgDeleteRow;

            public RecCursorAdapterViewHolder(View itemView) {
                super(itemView);
                txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
                txtTime = (TextView) itemView.findViewById(R.id.txtTime);
                imgDeleteRow = (ImageView) itemView.findViewById(R.id.imgDeleteRow);
            }
        }

        private class MyDataSetObserver extends DataSetObserver {
            @Override
            public void onChanged() {
                super.onChanged();
                notifyDataSetChanged();
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
                notifyDataSetChanged();
            }
        }

        private class MyContentObserver extends ContentObserver {
            /**
             * Creates a content observer.
             *
             * @param handler The handler to run {@link #onChange} on, or null if none.
             */
            public MyContentObserver(Handler handler) {
                super(handler);
            }

            @Override
            public void onChange(boolean selfChange, Uri uri) {
                super.onChange(selfChange, uri);
                notifyDataSetChanged();
            }
        }
    }


}
