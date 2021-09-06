package com.example.memorableplaces;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    ArrayList<String> arr;
    ArrayList<Double> lat;
    ArrayList<Double> lng;
    Intent ret;
    Intent intent;
    int position;


    @Override
    public void onBackPressed() {
        //super.onBackPressed(); Commenting this very important!!
        ret = new Intent(getApplicationContext(),MainActivity.class);
        ret.putExtra("list",arr);
        ret.putExtra("lat",lat);
        ret.putExtra("lng",lng);
        setResult(1,ret);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates("gps",0,1,locationListener);
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
        intent=getIntent();
        arr=intent.getStringArrayListExtra("list");
        lat=(ArrayList<Double>) intent.getSerializableExtra("lat");
        lng=(ArrayList<Double>)intent.getSerializableExtra("lng");
        position=intent.getIntExtra("position",0);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);

        LatLng center = new LatLng(lat.get(position), lng.get(position));
        mMap.addMarker(new MarkerOptions().position(center).title(arr.get(position)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(center,10));

        if(position == 0) {
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull Location location) {
                    mMap.clear();
                    LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (listAddress != null && listAddress.size() > 0) {
                            mMap.addMarker(new MarkerOptions().position(position).title(listAddress.get(0).getAddressLine(0)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(position).title(location.getLatitude() + " " + location.getLongitude()));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 10));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {

                }

                @Override
                public void onProviderEnabled(@NonNull String provider) {

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }
            };

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            } else {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, locationListener);
            }
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                mMap.clear();
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try{
                    List<Address> listAddress=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                    if(listAddress!=null && listAddress.size()>0){
                        mMap.addMarker(new MarkerOptions().position(latLng).title(listAddress.get(0).getAddressLine(0)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                    }
                    else{
                        mMap.addMarker(new MarkerOptions().position(latLng).title(latLng.latitude+"  "+latLng.longitude));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        if(position==0) {
            mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(@NonNull LatLng latLng) {
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                    try {
                        List<Address> listAddress = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                        if (listAddress != null && listAddress.size() > 0) {
                            mMap.addMarker(new MarkerOptions().position(latLng).title(listAddress.get(0).getAddressLine(0)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                            arr.add(listAddress.get(0).getAddressLine(0));
                        } else {
                            mMap.addMarker(new MarkerOptions().position(latLng).title(latLng.latitude + "  " + latLng.longitude));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                            arr.add(latLng.latitude + "  " + latLng.longitude);
                        }
                        lat.add((Double) latLng.latitude);
                        lng.add((Double) latLng.longitude);
                        Toast.makeText(getApplicationContext(), "Location Saved", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        }

    }
}