package com.example.dostavanahrana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DostavuvacListaActivity extends AppCompatActivity {

    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private Button logout;
    private ListView lista;
    private ArrayList<String> lines = new ArrayList<>();
    private ArrayList<String> lines1 = new ArrayList<>();
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    double korisnikLat, korisnikLon, dostavuvacLat, dostavuvacLon, distance;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dostavuvac_lista);

        logout = findViewById(R.id.logout);
        lista = findViewById(R.id.listaAktivniProizvodi);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(DostavuvacListaActivity.this);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(@NonNull Location location) {
                    dostavuvacLat = location.getLatitude();
                    dostavuvacLon = location.getLongitude();
                }
            });
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
            }
        }

        FirebaseDatabase.getInstance().getReference("KorisnickiNaracki").orderByChild("Status").equalTo("Aktivna").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    lines1.add(postSnapshot.child("ID").getValue().toString());
                    lines.add(postSnapshot.child("ImeProizvod").getValue().toString() + " - " + postSnapshot.child("ImeKorisnik").getValue().toString());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                        DostavuvacListaActivity.this, android.R.layout.simple_list_item_1, lines);
                lista.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String id = lines1.get(i);
                Intent intent = new Intent(DostavuvacListaActivity.this, DostavuvacDetaliActivity.class);
                FirebaseDatabase.getInstance().getReference("KorisnickiNaracki").orderByChild("ID").equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot postSnapshot: snapshot.getChildren()){
                            korisnikLat =(Double) postSnapshot.child("KorisnikLat").getValue();
                            korisnikLon =(Double) postSnapshot.child("KorisnikLon").getValue();
                            Location startPoint = new Location("locationA");
                            startPoint.setLatitude(dostavuvacLat);
                            startPoint.setLongitude(dostavuvacLon);

                            Location endPoint = new Location("locationB");
                            endPoint.setLatitude(korisnikLat);
                            endPoint.setLongitude(korisnikLon);
                            DecimalFormat f = new DecimalFormat("##.00");
                            distance = startPoint.distanceTo(endPoint);

                            intent.putExtra("Naracatel", postSnapshot.child("ImeKorisnik").getValue().toString());
                            intent.putExtra("Telefon", postSnapshot.child("TelefonKorisnik").getValue().toString());
                            intent.putExtra("TipProizvod", postSnapshot.child("TipProizvod").getValue().toString());
                            intent.putExtra("ImeProizvod", postSnapshot.child("ImeProizvod").getValue().toString());
                            intent.putExtra("CenaProizvod", postSnapshot.child("VkupnaCena").getValue().toString());
                            intent.putExtra("TipPlakjanje", postSnapshot.child("TipPlakjanje").getValue().toString());
                            intent.putExtra("EmailKorisnik", postSnapshot.child("EmailKorisnik").getValue().toString());
                            intent.putExtra("Komentar", postSnapshot.child("Komentar").getValue().toString());
                            intent.putExtra("ID", postSnapshot.child("ID").getValue().toString());
                            intent.putExtra("Rastojanie", f.format(distance/1000));
                            intent.putExtra("KorisnikLat", korisnikLat);
                            intent.putExtra("KorisnikLon", korisnikLon);
                            intent.putExtra("DostavuvacLat", dostavuvacLat);
                            intent.putExtra("DostavuvacLon", dostavuvacLon);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                startActivity(new Intent(DostavuvacListaActivity.this, MainActivity.class));
            }
        });
    }
}