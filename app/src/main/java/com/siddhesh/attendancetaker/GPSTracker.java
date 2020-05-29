package com.siddhesh.attendancetaker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GPSTracker implements LocationListener {

    private Context context;
    private Activity activity;
    private Double lattitude, longitude;
    private String address;
    private LocationManager locationManager;
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1; // 5 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 30 * 1; // 30 Sec
    Geocoder geocoder;
    List<Address> addresses;

    public GPSTracker(Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        geocoder = new Geocoder(context, Locale.getDefault());
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
        getLocation();
    }

    public String getAddress() {
        return address;
    }

    public String getLattitude() {
        return lattitude.toString();
    }

    public String getLongitude() {
        return longitude.toString();
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if (location != null)
            {
                lattitude = location.getLatitude();
                longitude = location.getLongitude();
                try {
                    addresses = geocoder.getFromLocation(lattitude, longitude, 1);
                    address = addresses.get(0).getAddressLine(0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                lattitude = 0.0;
                longitude = 0.0;
            }
    }

    @Override
    public void onLocationChanged(Location location) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        lattitude=location.getLatitude();
        longitude=location.getLongitude();
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lattitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //address = addresses.get(0).getAddressLine(0);
        //Toast.makeText(context,"Address:"+address+"\n"+"Lattitude:"+lattitude+"\n"+"Longitude:"+longitude,Toast.LENGTH_LONG).show();
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
