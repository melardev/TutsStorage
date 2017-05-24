package com.melardev.tutsstorage;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;

import com.melardev.tutsstorage.model.City;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class ActivityXMLDemo extends AppCompatActivity {

    private TextView txtXml;

    //SAX,DOM,XmlPullParser(recommended)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xmldemo);
        txtXml = (TextView) findViewById(R.id.txtXml);
    }

    public void readXmlPullParser(View view) {
        XmlPullParserFactory factory;
        FileInputStream fis = null;
        try {
            StringBuilder sb = new StringBuilder();
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            fis = openFileInput("cities.xml");

            xpp.setInput(fis, null);
            //xpp.setInput(new ByteArrayInputStream(xmlString.getBytes()),null);
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT)
                    sb.append("[START]");
                else if (eventType == XmlPullParser.START_TAG)
                    sb.append("\n<" + xpp.getName() + ">");
                else if (eventType == XmlPullParser.END_TAG)
                    sb.append("</" + xpp.getName() + ">");
                else if (eventType == XmlPullParser.TEXT)
                    sb.append(xpp.getText());

                eventType = xpp.next();
            }
            txtXml.setText(sb.toString());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void readXMLDOM(View view) {

        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;

        try {
            db = dbf.newDocumentBuilder();
            FileInputStream fis = openFileInput("cities.xml");
            doc = db.parse(fis);
            //doc = db.parse(new ByteArrayInputStream(xmlString.getBytes("UTF-8)));
            NodeList cities = doc.getElementsByTagName("city");
            String result = "";
            for (int i = 0; i < cities.getLength(); i++) {

                Node city = cities.item(i);
                /*
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
                    Node node = nodeList.item(0);
                    node.getNodeValue();
                  }
                  */
                NodeList cityInfo = city.getChildNodes();
                for (int j = 0; j < cityInfo.getLength(); j++) {
                    Node info = cityInfo.item(j);
                    result += "<" + info.getNodeName() + ">" + info.getTextContent() + "</" + info.getNodeName() + ">\n";
                }
            }

            txtXml.setText(result);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void writeToXmlFile(View view) {
        ArrayList<City> cities = new ArrayList<>();
        cities.add(new City("ES", "Madrid", 40.5, -3.666667));
        cities.add(new City("ES", "Valencia", 39.466667, -0.366667));

        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            serializer.setOutput(writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "cities");
            serializer.attribute("", "number", String.valueOf(cities.size()));
            for (City city : cities) {
                serializer.startTag("", "city");
                serializer.attribute("", "country", city.getCountry());

                serializer.startTag("", "name");
                serializer.text(city.getName());
                serializer.endTag("", "name");

                serializer.startTag("", "latitude");
                serializer.text(String.valueOf(city.getLatitude()));
                serializer.endTag("", "latitude");

                serializer.startTag("", "longitude");
                serializer.text(String.valueOf(city.getLongitude()));
                serializer.endTag("", "longitude");
                serializer.endTag("", "city");
            }
            serializer.endTag("", "cities");
            serializer.endDocument();
            String result = writer.toString();
            IOHelper.writeToFile(this, "cities.xml", result);
            txtXml.setText("From writeToXmlFile\n" + result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
