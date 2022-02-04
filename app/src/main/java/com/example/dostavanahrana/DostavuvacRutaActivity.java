package com.example.dostavanahrana;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.dostavanahrana.databinding.ActivityDostavuvacRutaBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.example.dostavanahrana.FetchURL;
import com.example.dostavanahrana.TaskLoadedCallback;

public class DostavuvacRutaActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    private ActivityDostavuvacRutaBinding binding;
    double korisnikLat, korisnikLon, dostavuvacLat, dostavuvacLon;
    private MarkerOptions dostavuvacLocation, korisnikLocation;
    private Polyline currentPolyline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDostavuvacRutaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        korisnikLat = intent.getDoubleExtra("KorisnikLat",0);
        korisnikLon = intent.getDoubleExtra("KorisnikLon",0);
        dostavuvacLat = intent.getDoubleExtra("DostavuvacLat",0);
        dostavuvacLon = intent.getDoubleExtra("DostavuvacLon",0);

        dostavuvacLocation = new MarkerOptions().position(new LatLng(dostavuvacLat, dostavuvacLon)).title("Dostavuvac");
        korisnikLocation = new MarkerOptions().position(new LatLng(korisnikLat, korisnikLon)).title("Korisnik");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        String url = getUrl(dostavuvacLocation.getPosition(), korisnikLocation.getPosition(), "driving");
        new FetchURL(DostavuvacRutaActivity.this).execute(url,"driving");
        Log.i("EHE:","STignavme do kraj na onCreate i pak nisto");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng momentalnaLokacija = new LatLng(dostavuvacLat, dostavuvacLon);
        mMap.addMarker(dostavuvacLocation);
        mMap.addMarker(korisnikLocation);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(momentalnaLokacija));
        mMap.setMinZoomPreference(15);
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
        Log.i("Spre","Stignavme do onTaskDone");
    }
}