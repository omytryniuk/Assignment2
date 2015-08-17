package com.example.yugenshtil.torontoparkguide;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(checkDataBase()){
            Toast.makeText(MainActivity.this, "Application is loading ...", Toast.LENGTH_LONG).show();
            TextView t = (TextView)findViewById(R.id.info);
            Intent intent = new Intent(MainActivity.this, Intro.class);
            startActivityForResult(intent, 0);
          //  t.setText("Application is loading ...");
        }

        else {
            if (isOnline()) {
                //Toast.makeText(MainActivity.this, "Create database", Toast.LENGTH_SHORT).show();
                //     DatabaseHelper myDb;
                //     SQLiteDatabase db;
                new AccessWebServiceTask().execute();

                //Intent intent = new Intent(this, oo.class);
                //startActivityForResult(intent, 0);
            }

            else {

           //     Toast.makeText(MainActivity.this, "The Internet is required for the first launch. Please, turn on the Internet and restart the application", Toast.LENGTH_LONG).show();
                TextView t = (TextView)findViewById(R.id.info);
                t.setText("The Internet is required for the first launch. Please, turn on the Internet and restart the application");
                // Intent intent = new Intent(Settings.ACTION_SETTINGS);
                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //  SystemClock.sleep(5000);
                // startActivity(intent);
            }
        }

    }



    private String getXMLData (String s) throws XmlPullParserException {
        String tagName = " ";
        String LocationName = " ";
        String Address = " ";
        String PostalCode = " ";
        String Intersection = " ";
        String FacilityType = " ";
        Set<String> u = new HashSet<String>();


        String ro=" ";

        String name =" ";
        InputStream in = null;
        String strDefinition = "O";
        try {
            //  in = OpenHttpConnection("http://zenit.senecac.on.ca:15108/android/parks.xml");
            in = OpenHttpConnection("http://www1.toronto.ca/City_Of_Toronto/Information_Technology/Open_Data/Data_Sets/Assets/Files/locations-20110725.xml");



            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            // WAS PRACTICING WITH CODE FROM http://www.xmlpull.org/v1/download/unpacked/doc/quick_intro.html

            // INPUT STREAM READER FROM http://www.tutorialforandroid.com/2009/05/how-to-use-xmlpullparser-in-android.html
            Log.d("Zoro", "Da1");

            xpp.setInput(new InputStreamReader(in));
            int eventType = xpp.getEventType();
            Log.d("Zoro","Da2");

            if  (eventType != XmlPullParser.END_DOCUMENT)
                Log.d("Zoro","Da1");
            else
                Log.d("Zoro","En1");



            while (eventType != XmlPullParser.END_DOCUMENT) {
                Log.d("Zoro","Inside");

                String tag = xpp.getName();

                if(eventType == XmlPullParser.START_DOCUMENT) {
                    System.out.println("Start document");

                } else if(eventType == XmlPullParser.END_DOCUMENT) {
                    System.out.println("End document");

                } else if(eventType == XmlPullParser.START_TAG) {
                    if(xpp.getName().equals("Location")){
                        LocationName = " ";
                        Address = " ";
                        PostalCode = " ";
                        Intersection = " ";
                        FacilityType = " ";
                        //  Log.d("Ole", "New Location");
                    }

                    //     Log.d("Ole", "START TAG" + xpp.getName());
                    tagName = xpp.getName();
                    System.out.println("Start tag "+xpp.getName());
                } else if(eventType == XmlPullParser.END_TAG) {
                    if(xpp.getName().equals("Location")){
                        for(String so : u){
                            FacilityType+=so+" ";
                            //   System.out.println("Oleki " + s);
                            Log.d("List", FacilityType +'\n');
                        }
                        u.clear();


                        ro = LocationName + " is located at " + Address + " Postal Code is: " + PostalCode + " Facilities:" +FacilityType;
                        //  Log.d("List", ro);
                        DatabaseHelper myDb;
                        myDb = new DatabaseHelper(this);
                        myDb.insertData(LocationName, Address, PostalCode,FacilityType);
                    }


                } else if(eventType == XmlPullParser.TEXT) {

                    if (xpp.getText().trim().length() != 0) {

                        if (tagName.equals("LocationName")){
                            LocationName = xpp.getText();
                        }
                        if (tagName.equals("Address")){
                            Address = xpp.getText();
                        }
                        if (tagName.equals("PostalCode")){
                            PostalCode = xpp.getText();
                        }
                        if (tagName.equals("FacilityType")){
                            // FacilityType += ","+ xpp.getText();
                            u.add(xpp.getText());
                        }
                        if (tagName.equals("Intersection")){
                            Intersection = xpp.getText();
                        }



                    }
                }

                eventType = xpp.next();

            }



        }catch(IOException e){}


        //  Toast.makeText(Intro.this, "Inside XML", Toast.LENGTH_LONG).show();
        //  Log.d("Oles", "We are sending " + strDefinition);
        //  return strDefinition;
        return ro;

    }























    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        String DB_FULL_PATH = "/data/data/com.example.yugenshtil.torontoparkguide/databases/Parks.db";
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_FULL_PATH, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            //Toast.makeText(MainActivity.this,"Noooo Database", Toast.LENGTH_SHORT).show();
        }
        return checkDB != null?true:false;
    }


/*
    private boolean isNetworkConnected() {
        NetworkInfo ni = null;
        try {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            ni = cm.getActiveNetworkInfo();

        } catch (Exception e) {
            Toast.makeText(Intro.this, "pLEASE CONNECT TO THE INTERNET AND RESTART APP", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }

*/
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    private class AccessWebServiceTask extends
            AsyncTask<String,Void,String> {
        String res=" ";
        protected String doInBackground(String ... urls) {

            try {
                res=getXMLData("Check");
            } catch (XmlPullParserException e) {
                e.printStackTrace();

            }
            return res;
        }

        protected void onPostExecute(String result) {
            // TextView tv = (TextView) findViewById(R.id.word);
            //tv.setText(result);
            Intent intent = new Intent(MainActivity.this, Intro.class);
            startActivityForResult(intent, 0);
        }
    }
















    private InputStream OpenHttpConnection(String urlString)
            throws IOException {
        InputStream in = null;
        int response = -1;
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection)){
            throw new IOException("Not an HTTP connection");}
        try {
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setAllowUserInteraction(false);
            httpConn.setInstanceFollowRedirects(true);
            httpConn.setRequestMethod("GET");
            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = new BufferedInputStream(httpConn.getInputStream());
            }
        } catch (Exception ex) {
            Log.d("Networking", ex.getLocalizedMessage());
            throw new IOException("Error connecting");
        }
        return in;
    }








}
