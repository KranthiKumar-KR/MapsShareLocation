package com.example.kranthikumarpolimetla.mapssharelocation;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
ImageButton share;
    private GoogleMap mMap;
    LocationManager locationManager;
    String provider;
    Double lng;
    Double lat;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 0: {
                // grantResults[0] = -1
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("INFO", "ACCESS GRANTED");
                    //getLocation(); this will crash due to a bug in android system 6.0.x but once this bug is solved you can call your method without restarting your app
                } else {
                    Log.i("INFO", "ACCESS DENIED");
                }
                return;
            }
        }

    }

    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){
                //provider = locationManager.NETWORK_PROVIDER;
                //location = locationManager.getLastKnownLocation(provider);
                Log.i("info","need to ask permission since it is note accepted in the first time");
                // this part triggers to ask permission when access is not granted in the first time user runs the app (this triggers in the second run)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            }
            else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                // after first installation this part triggers so getLocation() should be called from onRequestPermissionsResult method
                Log.i("info", "if statement working, permission yet not granted");
            }

            return;
        } else {

            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {

                Log.i("Location info", "Location achieved!");
                onLocationChanged(location);

            } else {

                Log.i("Location info", "No location :(");

            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
share = (ImageButton) findViewById(R.id.imageButton);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogPlus dialog = DialogPlus.newDialog(MapsActivity.this)
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                            }
                        })
                        .setExpanded(true, 600)  // This will enable the expand feature, (similar to android L share dialog)
                        .setContentHolder(new ViewHolder(R.layout.inflatedbutton))
                        .create();
                Button btn1 = (Button) dialog.findViewById(R.id.btn1);
                btn1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MapsActivity.this, "button 1 clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                Button btn2 = (Button) dialog.findViewById(R.id.btn2);
                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MapsActivity.this, "button 2 clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                Button btn3 = (Button) dialog.findViewById(R.id.btn3);
                btn3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MapsActivity.this, "button 3 clicked", Toast.LENGTH_SHORT).show();
                    }
                });
                ImageButton close = (ImageButton) dialog.findViewById(R.id.close);
                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                TextView tv = (TextView) dialog.findViewById(R.id.sendingAlert);
                tv.setText("Sending alert");
                dialog.show();
            }
        });
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        getLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){
                    //provider = locationManager.NETWORK_PROVIDER;
                    //location = locationManager.getLastKnownLocation(provider);
                    Log.i("info","need to ask permission since it is note accepted in the first time");
                    // need to ask permission since it is note accepted in the first time
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                }
                else{
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                    // after first installation this triggers so location should be called from onRequestPermissionsResult method
                    Log.i("info", "if statement working, permission yet not granted");
                }

                return;
            } else {
                locationManager.removeUpdates(this);
            }
        } catch (Exception e) {
            Log.i("Exception", "Exception on pause");
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){

                    Log.i("info","need to ask permission since it is note accepted in the first time");
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                }
                else{
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                    Log.i("info", "if statement working, permission yet not granted");
                }

                return;
            } else {

                locationManager.requestLocationUpdates(provider,1000, 10, this);

            }
        } catch (Exception e) {
            Log.i("Exception", "Exception on resume");
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        List<Address> addressList;
        String locationName;
        Geocoder geocoder= new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            addressList = geocoder.getFromLocation(lat, lng, 1);
            locationName = addressList.get(0).getAddressLine(0);
            Log.i("location name", locationName);
            LatLng CurrentLocation = new LatLng(lat, lng);
            mMap.addMarker(new MarkerOptions().position(CurrentLocation).title(locationName));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(CurrentLocation, 15));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(CurrentLocation));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        lat = location.getLatitude();
        lng = location.getLongitude();

        Log.i("Latitude", lat.toString());
        Log.i("Longitude", lng.toString());
        List<android.location.Address> addressList;
        String locationName;
        Geocoder geocoder= new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            addressList = geocoder.getFromLocation(lat, lng, 1);
            locationName = addressList.get(0).getAddressLine(0);
            Log.i("location name", locationName);
            if (mMap != null) {
                mMap.clear();
                LatLng newLocation = new LatLng(lat, lng);
                mMap.addMarker(new MarkerOptions().position(newLocation).title(locationName));
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 15));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

