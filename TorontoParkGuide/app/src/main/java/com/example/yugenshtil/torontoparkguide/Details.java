package com.example.yugenshtil.torontoparkguide;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Details extends FragmentActivity
        implements OnMapReadyCallback {

    MapFragment mMapFragment;
    LocationManager locationManager;
    GoogleMap map;
    String postalCode;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);



        //  https://developers.google.com/maps/documentation/android/map

        mMapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();
        mMapFragment.getMapAsync(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);


        Intent intent = getIntent();

        Log.d("Ole", "Res is here");
        //  String[] str  = intent.getArray("v");
        ArrayList<String> test = intent.getStringArrayListExtra("test");

        TextView na = (TextView)findViewById(R.id.nr);
        name = test.get(0);
        na.setText(name);
        TextView lo = (TextView)findViewById(R.id.lr);
        lo.setText(test.get(1));
        TextView po = (TextView)findViewById(R.id.pr);
        postalCode = (test.get(2));
        po.setText(postalCode);
       TextView fa = (TextView)findViewById(R.id.f1);
       fa.setText(test.get(3));


        Log.d("Ole", "Res 0 is " + test.get(0));
        Log.d("Ole", "Res 1 is " + test.get(1));
        Log.d("Ole", "Res 2 is " + test.get(2));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // The method establishes Google Map as a fragment and uses the address of the chosen park
    // https://developers.google.com/maps/documentation/android/map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;
        System.out.println(1234);
        try {
            address = coder.getFromLocationName(postalCode,5);
            //  Log.d("What", )
            Address location = address.get(0);
            double l1 = location.getLatitude();
            double l2 =  location.getLongitude();

            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(l1, l2))
                    .title(name));
            map = googleMap;
            LatLng l = new LatLng(l1,l2);

            // The method shows the location much closer on the layout
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(l, 15);
            map.moveCamera(update);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
