package com.melardev.tutsstorage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void demoDatabase(View view) {
        startDemo(ActivityDatabase.class);
    }

    private void startDemo(Class activity) {
        Intent i = new Intent(this, activity);
        startActivity(i);
    }

    public void sharedPreferencesDemo(View view) {
        startDemo(SharedPreferencesDemo.class);
    }
}
