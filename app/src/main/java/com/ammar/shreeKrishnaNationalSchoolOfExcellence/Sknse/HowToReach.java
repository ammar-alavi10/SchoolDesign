package com.ammar.shreeKrishnaNationalSchoolOfExcellence.Sknse;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HowToReach extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_reach);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng school = new LatLng(27.600808, 76.103009);
        mMap.addMarker(new MarkerOptions().position(school).title("Shree Krishna National School of Excellence"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(school));
    }
}