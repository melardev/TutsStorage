package com.melardev.tutsstorage;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.melardev.tutsstorage.model.City;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ActivityJsonDemo extends AppCompatActivity {


    private TextView txtJson;
    private static City city = new City("ES", "Madrid", 40.5, -3.666667);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json_demo);
        txtJson = (TextView) findViewById(R.id.txtJson);

    }

    public void serializeClassGSON(View view) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(city);
        IOHelper.writeToFile(this, "cityJsonObj.txt", jsonString);
    }

    public void unserializeClassGSON(View view) {
        Gson gson = new Gson();
        try {
            FileInputStream is = openFileInput("cityJsonObj.txt");
            String result = IOHelper.stringFromStream(is);
            //City city = gson.fromJson(Reader Instance, City.class);
            City city = gson.fromJson(result, City.class);
            txtJson.setText("Country : " + city.getCountry() + "\n" +
                    "Name : " + city.getName() + "\n" +
                    "Latitude,Longitud :" + city.getLatitude() + ", " + city.getLongitude());
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readJson(View view) {
        String jsonString = IOHelper.stringFromAsset(this, "cities.json");
        try {
            //JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray cities = new JSONArray(jsonString);

            String result = "";
            for (int i = 0; i < cities.length(); i++) {
                JSONObject city = cities.getJSONObject(i);
                //new Gson().fromJson(city.toString(), City.class);
                result += "Country : " + city.getString("country") + "\n" +
                        "Name : " + city.getString("name") + "\n" +
                        "Latitude,Longitud :" + city.getDouble("lat") + ", " + city.getString("lng");
            }
            txtJson.setText(result);
        } catch (Exception e) {
            Log.d("ReadPlacesFeedTask", e.getLocalizedMessage());
        }
    }

    public void writeJson(View view) {
        IOHelper.writeToFile(this, "cityJsonObj.txt", city.toJsonString());
    }
}
