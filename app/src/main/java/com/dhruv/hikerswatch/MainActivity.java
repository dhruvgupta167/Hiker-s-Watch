package com.dhruv.hikerswatch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
               updateAddress(location);
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
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            Location lastKnownLocation = (Location) locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            updateAddress(lastKnownLocation);
        }
    }

    public void updateAddress (Location location) {
        TextView lattitudeTextView = (TextView) findViewById(R.id.lattitudeTextView);
        lattitudeTextView.setText("Latitude:  " + String.valueOf(location.getLatitude()));

        TextView longitudeTextView = (TextView) findViewById(R.id.longitudeTextView);
        longitudeTextView.setText(("Longitude:  " + String.valueOf(location.getLongitude())));

        TextView accuracyTextView = (TextView) findViewById(R.id.accuracyTextView);
        accuracyTextView.setText(("Accuracy:  " + String.valueOf(location.getAccuracy())));

        TextView altitudeTextView = (TextView) findViewById(R.id.altituteTextView);
        altitudeTextView.setText("Altitude:  " + String.valueOf(location.getAltitude()));

        Geocoder geocoder;
        List<Address> listAddress;
        geocoder = new Geocoder(this, Locale.getDefault());
        String address = "Could not find address! :(" ;

        try {
            listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (listAddress!=null && listAddress.size() > 0) {
                address = "Address: \n";

                if (listAddress.get(0).getThoroughfare()!=null) {
                    address += listAddress.get(0).getThoroughfare() + "\n";
                }
                if (listAddress.get(0).getLocality()!=null) {
                    address += listAddress.get(0).getLocality() + "\n";
                }
                if (listAddress.get(0).getPostalCode()!=null) {
                    address += listAddress.get(0).getPostalCode() + "\n";
                }
                if (listAddress.get(0).getAdminArea()!=null) {
                    address += listAddress.get(0).getAdminArea() + "\n";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView addressTextView = (TextView) findViewById(R.id.addressTextView);
        addressTextView.setText(address);
    }
}
