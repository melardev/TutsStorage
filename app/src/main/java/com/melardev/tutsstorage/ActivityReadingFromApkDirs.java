package com.melardev.tutsstorage;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class ActivityReadingFromApkDirs extends AppCompatActivity {

    private TextView txtSpecialDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading_from_apk_dirs);
        txtSpecialDir = (TextView) findViewById(R.id.txtSpecialDir);
    }

    public void readFromAssets(View view) {
        AssetManager am = getAssets();
        try {
            InputStream is = am.open("cities.json");
            String result = IOHelper.stringFromStream(is);
            txtSpecialDir.setText(result);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFromRaw(View view) {
        Resources resources = getResources();
        InputStream is = resources.openRawResource(R.raw.testfile);
        try {
            String result = IOHelper.stringFromStream(is);
            txtSpecialDir.setText(result);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFromXml(View view) {
        StringBuilder sb = new StringBuilder();
        Resources res = getResources();
        XmlResourceParser xrp = res.getXml(R.xml.testfile);

        try {
            xrp.next();
            int eventType = xrp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT)
                    sb.append("[START]");
                else if (eventType == XmlPullParser.START_TAG) {
                    sb.append("\n<" + xrp.getName());
                    for (int i = 0; i < xrp.getAttributeCount(); i++)
                        sb.append("\n" + xrp.getAttributeName(i) + "=" + xrp.getAttributeValue(i));
                    /*
                    if (xrp.getName().equals("PreferenceScreen"))
                        sb.append(" xmlns:android = " + xrp.getNamespace());//xrp.getAttributeValue(null, "xmlns:android"));
                    else if (xrp.getName().equals("CheckBoxPreference"))
                        sb.append(" title = " + xrp.getAttributeValue(null, "title"));
                        */
                    sb.append(">\n");
                } else if (eventType == XmlPullParser.END_TAG)
                    sb.append("</" + xrp.getName() + ">\n");
                else if (eventType == XmlPullParser.TEXT)
                    sb.append(xrp.getText());

                eventType = xrp.next();
            }
            sb.append("[END]");
            txtSpecialDir.setText(sb.toString());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
