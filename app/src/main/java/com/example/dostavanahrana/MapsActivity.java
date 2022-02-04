package com.example.dostavanahrana;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.dostavanahrana.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private double lat, lon;
    private String tipProizvod, imeProizvod, cenaProizvod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        lat = intent.getDoubleExtra("Lat",0);
        lon = intent.getDoubleExtra("Lon",0);
        tipProizvod = intent.getStringExtra("TipProizvod");
        imeProizvod = intent.getStringExtra("ImeProizvod");
        cenaProizvod = intent.getStringExtra("CenaProizvod");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng momentalnaLokacija = new LatLng(lat, lon);
        mMap.addMarker(new MarkerOptions().position(momentalnaLokacija).title("Your location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(momentalnaLokacija));
        mMap.setMinZoomPreference(15);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Your location"));
                mMap.setMinZoomPreference(15);
                Intent intent1 = new Intent(MapsActivity.this,KorisnikNarackaActivity.class);
                intent1.putExtra("NewLat", latLng.latitude);
                intent1.putExtra("NewLon", latLng.longitude);
                intent1.putExtra("TipProizvod",tipProizvod);
                intent1.putExtra("CenaProizvod",cenaProizvod);
                intent1.putExtra("ImeProizvod",imeProizvod);
                startActivity(intent1);
            }
        });
    }
}