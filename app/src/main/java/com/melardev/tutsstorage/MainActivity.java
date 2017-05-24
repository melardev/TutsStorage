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

    public void contentProviderDemo(View view) {
        startDemo(ActivityContentProvider.class);
    }

    public void filesStorageDemo(View view) {
        startDemo(ActivityFilesStorage.class);
    }

    public void specialDirsDemo(View view) {
        startDemo(ActivityReadingFromApkDirs.class);
    }

    public void jsonDemo(View view) {
        startDemo(ActivityJsonDemo.class);
    }

    public void xmlDemo(View view) {
        startDemo(ActivityXMLDemo.class);
    }
}
