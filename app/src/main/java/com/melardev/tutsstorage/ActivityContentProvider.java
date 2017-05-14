package com.melardev.tutsstorage;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ActivityContentProvider extends AppCompatActivity {

    //https://developer.android.com/guide/components/fundamentals.html#Components
    //content://authority/optionalPath/optionalId

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_provider);
    }
}
