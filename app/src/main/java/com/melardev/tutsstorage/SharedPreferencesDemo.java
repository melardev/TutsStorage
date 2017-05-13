package com.melardev.tutsstorage;

import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SharedPreferencesDemo extends AppCompatActivity {

    private EditText etxtPrefName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_preferences_demo);

        etxtPrefName = (EditText) findViewById(R.id.etxtNamePref);
        String defaultSharedPreferencesName = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
                && false
                )
            defaultSharedPreferencesName = PreferenceManager.getDefaultSharedPreferencesName(this);
        else
            defaultSharedPreferencesName = getPackageName() + "_preferences";

        SharedPreferences sp = getSharedPreferences(defaultSharedPreferencesName, MODE_PRIVATE); //same as PreferenceManager.getDefaultSharedPreferences(this);


        String prefString = sp.getString("pref_name", "");
        if (!prefString.isEmpty())
            etxtPrefName.setText(prefString);
    }

    public void acceptNameAndFinish(View view) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String name = etxtPrefName.getText().toString();

        sp.edit() //Get Editor object
                .putString("pref_name", name)
                //.commit() //Blocking Operation
                .apply(); //Asynchronous


        finish();
    }


}
