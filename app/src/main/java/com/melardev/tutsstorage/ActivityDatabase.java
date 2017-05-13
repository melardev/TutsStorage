package com.melardev.tutsstorage;


import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.melardev.tutsstorage.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class ActivityDatabase extends AppCompatActivity {

    private EditText etxtName;
    private EditText etxtNameExisting;
    private EditText etxtId;
    private RecyclerView recUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        etxtName = (EditText) findViewById(R.id.etxtName);
        etxtNameExisting = (EditText) findViewById(R.id.etxtNameExisting);
        etxtId = (EditText) findViewById(R.id.etxtId);

        recUsers = (RecyclerView) findViewById(R.id.recUsers);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recUsers.setLayoutManager(llm);
        recUsers.setAdapter(new RecUsersAdapter(this, llm));
    }

    public void editUser(View view) {
        String id = etxtId.getText().toString();
        String name = etxtNameExisting.getText().toString();

        if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(name)) {
            DBHelper.getInstance(getApplicationContext()).renameItem(id, name);
            //DBHelper.getInstance(getApplicationContext()).renameItem(DBHelper.getInstance(getApplicationContext()).getUserWithId(id));
        }
    }

    public void addUser(View view) {
        String userName = etxtName.getText().toString();
        if (!TextUtils.isEmpty(userName))
            DBHelper.getInstance(getApplicationContext()).addUser(userName);
    }

    public void deleteDB(View view) {
        if (deleteDatabase(DBHelper.DB_NAME))
            recUsers.getAdapter().notifyDataSetChanged();
    }

    private class RecUsersAdapter extends RecyclerView.Adapter<UserViewHolder> implements DBHelper.OnDatabaseChangedListener {
        private final ActivityDatabase context;
        private final LinearLayoutManager llm;

        public RecUsersAdapter(ActivityDatabase activityDatabase, LinearLayoutManager llm) {
            this.llm = llm;
            this.context = activityDatabase;

            DBHelper.getInstance(activityDatabase.getApplicationContext()).setOnDatabaseChangedListener(this);
        }


        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new UserViewHolder(context.getLayoutInflater().inflate(R.layout.user_row, parent, false));

        }

        @Override
        public void onBindViewHolder(final UserViewHolder holder, int position) {

            final User user = DBHelper.getInstance(context).getItemAt(position);
            holder.txtUserName.setText(user.getName());
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
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

                    DBHelper.getInstance(getApplicationContext()).deleteUser(user.getId());
                }
            });

        }


        @Override
        public int getItemCount() {
            return DBHelper.getInstance(context.getApplicationContext()).getCount();
        }

        @Override
        public void onNewDatabaseEntryAdded() {
            notifyItemInserted(getItemCount() - 1);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    llm.scrollToPosition(getItemCount() - 1);

                }
            });
        }

        @Override
        public void onDatabaseEntryRenamed() {
            notifyDataSetChanged();
        }
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {
        public final TextView txtUserName;
        public final TextView txtTime;
        private final ImageView imgDeleteRow;

        public UserViewHolder(View itemView) {
            super(itemView);
            txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            imgDeleteRow = (ImageView) itemView.findViewById(R.id.imgDeleteRow);
        }
    }
}
